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
import kotlinx.android.synthetic.main.fragment_leader_board.*
import kotlinx.android.synthetic.main.leader_board_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Jason on 9/17/17.
 */
class LeaderBoardFragment : Fragment() {

    private val TAG = LeaderBoardFragment::class.java.simpleName
    private lateinit var app: LOTPApp
    private var mLeaderBoard: LeaderBoard? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_leader_board, container, false)
        app = activity?.application as LOTPApp

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState?.containsKey(Constants.EXTRA_LEADERBOARD) == true) {
            mLeaderBoard = savedInstanceState.getSerializable(Constants.EXTRA_LEADERBOARD) as LeaderBoard
            setLeaderBoardVisible()
            bindLeaderBoard(mLeaderBoard)
        } else {
            refreshData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            it.putSerializable(Constants.EXTRA_LEADERBOARD, mLeaderBoard)
        }
    }

    private fun refreshData() {
        leader_board_progress?.visibility = View.VISIBLE
        try {
            val items = app.getPingPongService().getLeaderBoard()
            items.enqueue(object : Callback<LeaderBoard> {

                override fun onResponse(leaderBoard: Call<LeaderBoard>, response: Response<LeaderBoard>) {
                    Log.d(TAG, "Received a leader board!")
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body().leaderboard.isNotEmpty()) {
                            mLeaderBoard = response.body()
                            setLeaderBoardVisible()
                            bindLeaderBoard(response.body())
                        } else {
                            displayEmptyView()
                            leader_board_empty.text = getString(R.string.leader_board_empty)
                        }
                    } else {
                        displayEmptyView()
                    }
                }

                override fun onFailure(call: Call<LeaderBoard>, t: Throwable) {
                    Log.d(TAG, "FAILURE getting a leader board!!", t.cause)
                    displayEmptyView()
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ", e.cause)
        }
    }

    private fun displayEmptyView() {
        leader_board_progress?.visibility = View.GONE
        leader_board_empty?.visibility = View.VISIBLE
    }

    private fun setLeaderBoardVisible(){
        leader_board_progress.visibility = View.GONE
        leader_board_header_container.visibility = View.VISIBLE
        leader_board_recycler.visibility = View.VISIBLE
    }

    private fun bindLeaderBoard(items: LeaderBoard?) {
        items?.let {
            leader_board_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            leader_board_recycler.layoutManager = LinearLayoutManager(context)
            leader_board_recycler.adapter = LeaderBoardAdapter(it.leaderboard)
        }
    }

}