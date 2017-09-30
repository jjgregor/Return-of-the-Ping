package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.adapters.LeaderBoardAdapter
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.LeaderBoard
import com.jason.returnoftheping.services.LOTPService
import kotlinx.android.synthetic.main.fragment_leader_board.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Jason on 9/17/17.
 */
class LeaderBoardFragment : Fragment() {

    private val TAG = LeaderBoardFragment::class.java.simpleName
    private lateinit var mLeaderBoard: LeaderBoard

    @Inject lateinit var service: LOTPService

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_leader_board, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LOTPApp.component.inject(this)


        leader_board_progress?.visibility = View.VISIBLE
        if (savedInstanceState?.containsKey(Constants.EXTRA_LEADER_BOARD) == true) {
            mLeaderBoard = savedInstanceState.getSerializable(Constants.EXTRA_LEADER_BOARD) as LeaderBoard
            setLeaderBoardVisible()
            bindLeaderBoard(mLeaderBoard)
        } else {
            refreshData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            it.putSerializable(Constants.EXTRA_LEADER_BOARD, mLeaderBoard)
        }
    }

    private fun refreshData() {
        service.getLeaderBoard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.leaderboard.isNotEmpty()) {
                        mLeaderBoard = response
                        setLeaderBoardVisible()
                        bindLeaderBoard(response)
                    } else {
                        displayEmptyView()
                        leader_board_empty.text = getString(R.string.leader_board_empty)
                    }
                }, { t: Throwable? ->
                    displayEmptyView()
                    Log.d(TAG, "Exception: ", t)
                })
    }

    private fun displayEmptyView() {
        leader_board_progress?.visibility = View.GONE
        leader_board_empty?.visibility = View.VISIBLE
    }

    private fun setLeaderBoardVisible() {
        leader_board_progress.visibility = View.GONE
        leader_board_recycler.visibility = View.VISIBLE
    }

    private fun bindLeaderBoard(items: LeaderBoard) {
        leader_board_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        leader_board_recycler.layoutManager = LinearLayoutManager(context)
        leader_board_recycler.adapter = LeaderBoardAdapter(items.leaderboard)
    }

}