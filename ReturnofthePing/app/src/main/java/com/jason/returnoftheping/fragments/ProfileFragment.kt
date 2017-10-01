package com.jason.returnoftheping.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.models.Profile
import com.jason.returnoftheping.services.LOTPService
import kotlinx.android.synthetic.main.fragment_profile.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject




/**
 * Created by Jason on 9/24/17.
 */
class ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.name
    private var profile: Profile? = null
    private lateinit var player: Player

    @Inject lateinit var service: LOTPService

    companion object {
        fun newInstance(player: Player): ProfileFragment {
            val args = Bundle()
            args.putSerializable(Constants.PLAYER, player)

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LOTPApp.component.inject(this)

        player = arguments.getSerializable(Constants.PLAYER) as Player
        profile = savedInstanceState?.get(Constants.PROFILE) as? Profile

        if (profile == null) {
            refreshData()
        } else {
            bindProfile()
        }

    }

    private fun refreshData() {
        service.getProfile(player.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        profile = it
                        bindProfile()
                    }
                }, { t: Throwable? -> Log.d(TAG, "Error retrieving profile: ", t) })
    }

    private fun bindProfile() {
        profile?.player?.gravatarImageUrl?.let {
            Glide.with(context)
                    .load(it)
                    .apply(RequestOptions().centerCrop())
                    .into(profile_avatar)
        }

        bindMatchTotals();
        bindGameTotals();
        bindMatchHistory();

    }

    private fun bindMatchTotals() {
        profile_matches_graph.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        profile_matches_graph.dragDecelerationFrictionCoef = 0.95f
        profile_matches_graph.centerText = profile?.stats?.matchCount.toString() + " Matches"
        profile_matches_graph.isDrawHoleEnabled = true
        profile_matches_graph.setHoleColor(Color.WHITE)
        profile_matches_graph.transparentCircleRadius = 0f
        profile_matches_graph.setDrawCenterText(true)
        profile_matches_graph.rotationAngle = 0f
        profile_matches_graph.holeRadius = 40f
        profile_matches_graph.isRotationEnabled = true
        profile_matches_graph.isHighlightPerTapEnabled = false
        profile_matches_graph.description.isEnabled = false
        profile_matches_graph.legend.isEnabled = false

        profile_games_graph.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        profile_games_graph.dragDecelerationFrictionCoef = 0.95f
        profile_games_graph.centerText = profile?.stats?.matchCount.toString() + " Matches"
        profile_games_graph.isDrawHoleEnabled = true
        profile_games_graph.setHoleColor(Color.WHITE)
        profile_games_graph.transparentCircleRadius = 0f
        profile_games_graph.setDrawCenterText(true)
        profile_games_graph.rotationAngle = 0f
        profile_games_graph.holeRadius = 40f
        profile_games_graph.isRotationEnabled = true
        profile_games_graph.isHighlightPerTapEnabled = false
        profile_games_graph.description.isEnabled = false
        profile_games_graph.legend.isEnabled = false

        val matchEntries = ArrayList<PieEntry>()
        matchEntries.add(PieEntry((profile?.stats?.matchWinPercentage ?: 0).toFloat(), "Wins"))
        matchEntries.add(PieEntry((100 - (profile?.stats?.matchWinPercentage ?: 0)).toFloat(), "Losses"))

        val gameEntries = ArrayList<PieEntry>()
        gameEntries.add(PieEntry((profile?.stats?.gameWinPercentage?: 0).toFloat(), "Wins"))
        gameEntries.add(PieEntry((100 - (profile?.stats?.gameWinPercentage ?: 0)).toFloat(), "Losses"))

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(context, R.color.green))
        colors.add(ContextCompat.getColor(context, R.color.red))

        val matchesDataSet = PieDataSet(matchEntries, "Matches")
        matchesDataSet.setDrawIcons(false)
        matchesDataSet.colors = colors
        matchesDataSet.sliceSpace = 3f
        matchesDataSet.iconsOffset = MPPointF(0f, 40f)
        matchesDataSet.selectionShift = 5f

        val gameDataSet = PieDataSet(gameEntries, "Games")
        gameDataSet.setDrawIcons(false)
        gameDataSet.colors = colors
        gameDataSet.sliceSpace = 3f
        gameDataSet.iconsOffset = MPPointF(0f, 40f)
        gameDataSet.selectionShift = 5f

        val matchesData = PieData(matchesDataSet)
        matchesData.setValueFormatter(PercentFormatter())
        matchesData.setValueTextSize(11f)
        matchesData.setValueTextColor(Color.WHITE)

        val gamesData = PieData(gameDataSet)
        gamesData.setValueFormatter(PercentFormatter())
        gamesData.setValueTextSize(11f)
        gamesData.setValueTextColor(Color.WHITE)

        profile_matches_graph.data = matchesData
        profile_games_graph.data = gamesData
    }

    private fun bindMatchHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun bindGameTotals() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


