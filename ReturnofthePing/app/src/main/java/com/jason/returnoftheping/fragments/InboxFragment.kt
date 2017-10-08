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
import com.jason.returnoftheping.adapters.InboxMatchesAdapter
import com.jason.returnoftheping.adapters.InboxMatchesAdapter.OnMatchConfirmationItemClickedListener
import com.jason.returnoftheping.models.Match
import com.jason.returnoftheping.models.MatchConfirmationRequest
import com.jason.returnoftheping.services.LOTPService
import kotlinx.android.synthetic.main.fragment_inbox.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by Jason on 10/1/17.
 */
class InboxFragment : Fragment() {

    val TAG = InboxFragment::class.java.name

    @Inject lateinit var service: LOTPService

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_inbox, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LOTPApp.component.inject(this)

        swipe_refresh.setOnRefreshListener({ getPendingMatches() })

        getPendingMatches()
    }

    private fun getPendingMatches() {
        service.getPendingMatches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        inbox_progress?.visibility = View.GONE
                        if (response.pendingMatches.isEmpty()) {
                            inbox_empty?.visibility = View.VISIBLE
                        } else {
                            inbox_recycler?.visibility = View.VISIBLE
                            bindInbox(response.pendingMatches)
                        }
                    }
                }, { t: Throwable? ->
                    Log.d(TAG, "Error retrieving inbox: ", t)
                    inbox_progress?.visibility = View.GONE
                    inbox_empty?.visibility = View.VISIBLE
                    inbox_empty?.text = getString(R.string.inbox_error_message)
                })
    }

    private fun bindInbox(messages: List<Match>) {
        inbox_recycler.isNestedScrollingEnabled = false
        inbox_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        inbox_recycler.layoutManager = LinearLayoutManager(context)
        inbox_recycler.setHasFixedSize(true)
        inbox_recycler.adapter = InboxMatchesAdapter(messages, object : OnMatchConfirmationItemClickedListener {
            override fun onItemClicked(item: MatchConfirmationRequest)  = sendMatchConfirmation(item)
        })
    }

    private fun sendMatchConfirmation(item: MatchConfirmationRequest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}