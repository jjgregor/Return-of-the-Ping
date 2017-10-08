package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
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
import com.jason.returnoftheping.adapters.InboxRegistrationAdapter
import com.jason.returnoftheping.models.Match
import com.jason.returnoftheping.models.MatchConfirmationRequest
import com.jason.returnoftheping.models.RegistrationConfirmationRequest
import com.jason.returnoftheping.models.RegistrationRequest
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
        service.getInbox()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        inbox_progress?.visibility = View.GONE
                        if (it.matches.isEmpty() && it.registrationRequests.isEmpty()) {
                            inbox_empty?.visibility = View.VISIBLE
                        } else {
                            if (it.matches.isNotEmpty()) {
                                bindInboxMatches(response.matches)
                            }
                            if (it.registrationRequests.isNotEmpty()) {
                                bindInboxRegistrationRequests(response.registrationRequests)
                            }
                        }
                        swipe_refresh.isRefreshing = false
                    }
                }, { t: Throwable? ->
                    Log.d(TAG, "Error retrieving inbox: ", t)
                    inbox_progress?.visibility = View.GONE
                    inbox_empty?.visibility = View.VISIBLE
                    inbox_empty?.text = getString(R.string.inbox_error_message)
                })
    }

    private fun bindInboxRegistrationRequests(registrationRequests: ArrayList<RegistrationRequest>) {
        inbox_registration_recycler.visibility = View.VISIBLE
        inbox_registration_recycler.isNestedScrollingEnabled = false
        inbox_registration_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        inbox_registration_recycler.layoutManager = LinearLayoutManager(context)
        inbox_registration_recycler.setHasFixedSize(true)
        inbox_matches_recycler.adapter = InboxRegistrationAdapter(registrationRequests, object : InboxRegistrationAdapter.OnRegistrationConfirmationItemClickedListener {
            override fun onDenyRequest(item: RegistrationConfirmationRequest) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onConfirmRequest(item: RegistrationConfirmationRequest) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun bindInboxMatches(messages: List<Match>) {
        inbox_matches_recycler?.visibility = View.VISIBLE
        inbox_matches_recycler.isNestedScrollingEnabled = false
        inbox_matches_recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        inbox_matches_recycler.layoutManager = LinearLayoutManager(context)
        inbox_matches_recycler.setHasFixedSize(true)
        inbox_matches_recycler.adapter = InboxMatchesAdapter(messages, object : OnMatchConfirmationItemClickedListener {
            override fun onItemClicked(item: MatchConfirmationRequest) = sendMatchConfirmation(item)
        })
    }

    private fun sendMatchConfirmation(item: MatchConfirmationRequest) {
        service.confirmMatch(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        if (it.success.contains("success")) {
                            view?.let { Snackbar.make(it, getString(R.string.match_confirmation_failed), Snackbar.LENGTH_LONG).show() }
                            getPendingMatches()
                        } else {
                            view?.let { Snackbar.make(it, getString(R.string.match_confirmation_failed), Snackbar.LENGTH_LONG).show() }
                        }
                    }
                }, {
                    t: Throwable? ->
                    Log.d(TAG, "Error confirming match: ", t)
                    view?.let { Snackbar.make(it, getString(R.string.match_confirmation_failed), Snackbar.LENGTH_LONG).show() }
                })
    }
}