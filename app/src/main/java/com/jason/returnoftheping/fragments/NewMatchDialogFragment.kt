package com.jason.returnoftheping.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.services.LOTPService
import kotlinx.android.synthetic.main.fragment_inbox.*
import kotlinx.android.synthetic.main.fragment_new_match.*
import kotlinx.android.synthetic.main.new_match_opponent.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Jason on 10/8/17.
 */
class NewMatchDialogFragment : DialogFragment(), AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private val TAG = NewMatchDialogFragment::class.java.simpleName

    private var mOpponent: Player? = null
    private lateinit var app: LOTPApp
    private lateinit var adapter: ArrayAdapter<Player>
    private var allPlayers: ArrayList<String> = ArrayList()

    @Inject lateinit var service: LOTPService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_new_match, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);
        LOTPApp.component.inject(this)
        app = context.applicationContext as LOTPApp

        if (allPlayers.isEmpty()) {
            getAllPlayer()
        }
        if (mOpponent != null) {
            bindOpponent()
        } else {
            new_match_wins.setSelection(3)
        }

        setAdapters()
        setClickListeners()
        setVisibilities()
        updateOpponentState()
        updateWinsSpinnerState()
    }

    private fun setVisibilities() {
        new_match_opponent.visibility = if (mOpponent != null) View.GONE else View.VISIBLE
        new_match_confirmed_opponent.visibility = if (mOpponent != null) View.VISIBLE else View.GONE
    }

    private fun setAdapters() {
        adapter = ArrayAdapter<Player>(context, android.R.layout.simple_list_item_1, allPlayers)
//        adapter = OpponentAutoCompleteAdapter(context, allPlayers)
        new_match_opponent.setAdapter(adapter)
        new_match_wins.adapter = ArrayAdapter<String>(activity, R.layout.spinner_win_losses_dropdown_item, Constants.WINS_ARRAY)
    }

    private fun setClickListeners() {
        new_match_wins.onItemSelectedListener = this
        new_match_losses.onItemSelectedListener = this
        new_match_opponent.onItemClickListener = this

        new_match_clear_opponent.setOnClickListener({ clearOpponent() })
        new_match_submit_btn.setOnClickListener { submitMatch() }
        new_match_result.setOnClickListener({
            if (getTotalWins() >= 3) {
                new_match_wins.setSelection(0)
            } else {
                new_match_wins.setSelection(3)
            }
        })

    }

    private fun submitMatch() {
        new_match_cont
                .animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator) {
                        new_match_submitting_cont.alpha = 0f
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        new_match_cont.visibility = View.GONE
                        new_match_submitting_cont.visibility = View.VISIBLE
                        new_match_submitting_cont
                                .animate()
                                .alpha(1f)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(anim: Animator) {
                                        submit()
                                    }
                                })
                                .start()
                    }
                })
                .start()
    }

    private fun submit() {
        mOpponent?.let {
            service.saveMatch(it.id, getTotalWins(), getTotalLosses())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        response?.let {
                            if (it.success.contains("success")) {
                                view?.let {
                                    Snackbar.make(it, getString(R.string.new_match_match_submitted_lbl), Snackbar.LENGTH_LONG).show()
                                }
                                this.dismiss()
                                inbox_matches_recycler.adapter.notifyDataSetChanged()
                            } else {
                                view?.let { Snackbar.make(it, getString(R.string.new_match_match_submission_failed_lbl), Snackbar.LENGTH_LONG).show() }
                            }
                        }

                    }, { t: Throwable? ->
                        Log.d(TAG, "Exception: ", t)
                    })
        }
    }

    private fun clearOpponent() {
        mOpponent = null
        new_match_opponent.text = null
        new_match_confirmed_opponent
                .animate()
                .alpha(0f)
                .setListener(
                        object : AnimatorListenerAdapter() {

                            override fun onAnimationStart(animation: Animator) {
                                new_match_clear_opponent.visibility = View.GONE
                            }

                            override fun onAnimationEnd(animation: Animator) {
                                opponent_name.text = null
                                opponent_email.text = null
                                new_match_confirmed_opponent.visibility = View.GONE
                                new_match_opponent.visibility = View.VISIBLE
                                new_match_opponent
                                        .animate()
                                        .alpha(1f)
                                        .setListener(null)
                                        .start()
                            }
                        }
                )
                .start()
        updateOpponentState()
        updateWinsSpinnerState()
    }

    private fun updateResult() {
        val win = getTotalWins() > getTotalLosses()
        new_match_result.text = if (win) "W" else "L"
        new_match_result.setBackgroundResource(if (win) R.drawable.win_background else R.drawable.loss_background)
    }

    private fun getTotalLosses(): Int = Integer.valueOf((new_match_losses.adapter as ArrayAdapter<String>).getItem(new_match_losses.selectedItemPosition))

    private fun getTotalWins(): Int = Integer.valueOf((new_match_wins.adapter as? ArrayAdapter<String>)?.getItem(new_match_wins.selectedItemPosition))

    private fun updateOpponentState() {
        new_match_opponent.isEnabled = !isValidOpponent()
    }

    private fun updateWinsSpinnerState() {
        new_match_wins.isEnabled = isValidOpponent()
        updateLossesSpinnerState()
        updateSubmitBtnState()
    }

    private fun updateSubmitBtnState() {
        new_match_submit_btn.isEnabled = isValidOpponent()
    }

    private fun updateLossesSpinnerState() {
        new_match_losses.isEnabled = isValidOpponent()
    }

    private fun isValidOpponent(): Boolean = mOpponent != null && (mOpponent as Player) != app.getCurrentPlayer()

    private fun getAllPlayer() {
        service.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        val playerNames = ArrayList<String>()
                        it.players.mapTo(playerNames) { it.name }
                        allPlayers = playerNames
                    }
                }, {
                    t: Throwable? -> Log.d(TAG, "Failed to get all players: ", t)
                })
    }

    private fun bindOpponent() {
        if (mOpponent != null) {
            opponent_name.text = (mOpponent as Player).name
            opponent_email.text = (mOpponent as Player).email
            new_match_clear_opponent.visibility = View.VISIBLE
        } else {
            new_match_opponent.visibility = View.VISIBLE
            new_match_confirmed_opponent.visibility = View.GONE
            new_match_clear_opponent.visibility = View.GONE
        }
    }

    private fun updateConfirmedOpponent() {
        new_match_opponent
                .animate()
                .alpha(0f)
                .setListener(
                        object : AnimatorListenerAdapter() {

                            override fun onAnimationStart(animation: Animator) {
                                new_match_confirmed_opponent.alpha = 0f
                                bindOpponent()
                            }

                            override fun onAnimationEnd(animation: Animator) {
                                new_match_opponent.visibility = View.GONE
                                new_match_confirmed_opponent.visibility = View.VISIBLE
                                new_match_confirmed_opponent
                                        .animate()
                                        .alpha(1f)
                                        .setListener(
                                                object : AnimatorListenerAdapter() {

                                                    override fun onAnimationEnd(anim: Animator) {
                                                        new_match_clear_opponent.visibility = View.VISIBLE
                                                    }
                                                }
                                        )
                                        .start()
                            }
                        }
                )
                .start()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent === new_match_wins) {
            new_match_losses.adapter = ArrayAdapter<String>(
                    activity,
                    R.layout.spinner_win_losses_dropdown_item,
                    Constants.LOSSES_ARRAYS[Integer.valueOf(Constants.WINS_ARRAY[position])]
            )
        }
        updateResult()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { /* no-op */
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mOpponent = adapter.getItem(position)
        Log.d(TAG, "Selected player $mOpponent as opponent")
        updateConfirmedOpponent()
        updateOpponentState()
        updateWinsSpinnerState()
    }

}