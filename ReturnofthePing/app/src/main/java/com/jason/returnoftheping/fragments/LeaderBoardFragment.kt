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
import com.jason.returnoftheping.models.LeaderBoard
import kotlinx.android.synthetic.main.fragment_leader_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




/**
 * Created by Jason on 9/17/17.
 */
class LeaderBoardFragment : Fragment() {

    private val TAG = LeaderBoardFragment::class.java.simpleName
    private lateinit var app: LOTPApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_leader_board, container, false)
        app = activity?.application as LOTPApp
        refreshData()

        return root
    }

    private fun refreshData() {

        leader_board_progress?.visibility = View.VISIBLE

        try {
            val items = app.getPingPongService().getLeaderBoard()
            items.enqueue(object : Callback<LeaderBoard> {
                override fun onResponse(leaderboard: Call<LeaderBoard>, response: Response<LeaderBoard>) {
                    Log.d(TAG, "Received a leader board!")
                    // Why does this want to be casted?
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body().leaderboard.isNotEmpty()) {
                            leader_board_progress.visibility = View.INVISIBLE
                            leader_board_recycler.visibility = View.VISIBLE
                            bindLeaderBoard(response.body())
                        } else {
                            leader_board_progress.visibility = View.INVISIBLE
                            leader_board_empty.visibility = View.VISIBLE
                            leader_board_empty.text = getString(R.string.leader_board_empty)
                        }
                    } else {
                        leader_board_progress.visibility = View.INVISIBLE
                        leader_board_empty.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<LeaderBoard>, t: Throwable) {
                    Log.d(TAG, "FAILURE!!!")
                    leader_board_progress?.visibility = View.INVISIBLE
                    leader_board_empty?.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ", e)
        }

    }

    private fun bindLeaderBoard(items: LeaderBoard) {
        leader_board_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        leader_board_recycler.layoutManager = LinearLayoutManager(context)
        leader_board_recycler.adapter = LeaderBoardAdapter(items.leaderboard)

    }

}