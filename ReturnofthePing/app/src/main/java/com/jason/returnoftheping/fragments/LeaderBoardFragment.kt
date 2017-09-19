package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.adapters.LeaderBoardAdapter
import com.jason.returnoftheping.models.LeaderBoardItem
import kotlinx.android.synthetic.main.fragment_leader_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Jason on 9/17/17.
 */
class LeaderBoardFragment : Fragment() {

    private val TAG = LeaderBoardFragment::class.java.simpleName
    private var app = LOTPApp()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_leader_board, container, false)
        leader_board_progress.visibility = View.VISIBLE

        refreshData()

        return root
    }

    private fun refreshData() {
        try {
            val items = app.getPingPongService().getLeaderBoard()
            items.enqueue(object : Callback<List<LeaderBoardItem>> {
                override fun onResponse(leaderboard: Call<List<LeaderBoardItem>>, response: Response<List<LeaderBoardItem>>) {
                    Log.d(TAG, "Received a leader board!")
                    // Why does this want to be casted?
                    if (response.isSuccessful && response.body() != null) {
                        if(response.body().isNotEmpty()){
                            leader_board_progress.visibility = View.INVISIBLE
                            leader_board_recycler.visibility = View.VISIBLE
                            bindLeaderBoard(ArrayList<LeaderBoardItem>(response.body()))
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

                override fun onFailure(call: Call<List<LeaderBoardItem>>, t: Throwable) {
                    Log.d(TAG, "FAILURE!!!")
                    leader_board_progress.visibility = View.INVISIBLE
                    leader_board_empty.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ", e)
        }

    }

    private fun bindLeaderBoard(items: ArrayList<LeaderBoardItem>) {
        leader_board_recycler.layoutManager = LinearLayoutManager(context)
        leader_board_recycler.adapter = LeaderBoardAdapter(items)

    }

}