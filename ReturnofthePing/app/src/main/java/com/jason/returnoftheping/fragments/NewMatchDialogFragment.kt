package com.jason.returnoftheping.fragments

import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.services.LOTPService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Jason on 10/8/17.
 */
class NewMatchDialogFragment : DialogFragment() {

    private val TAG = NewMatchDialogFragment::class.java.simpleName

    private var allPlayers: ArrayList<Player> = ArrayList()

    @Inject lateinit var service: LOTPService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_new_match, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);
        LOTPApp.component.inject(this)

        if (allPlayers.isEmpty()) {
            getAllPlayer()
        }
    }

    private fun getAllPlayer() {
        service.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        allPlayers = response
                    }
                }, {
                    t: Throwable? ->
                    Log.d(TAG, "Failed to get all players: ", t)
                })
    }
}