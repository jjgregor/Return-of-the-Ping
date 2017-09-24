package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.SignInRegisterResponse


/**
 * Created by Jason on 9/24/17.
 */
class ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.name
    private lateinit var app: LOTPApp

    companion object {
        fun newInstance(profile: SignInRegisterResponse): ProfileFragment {
            val args = Bundle()
            args.putSerializable(Constants.PLAYER, profile.player)
            args.putSerializable(Constants.MATCHES, profile.matches as? ArrayList)
            args.putSerializable(Constants.STATS, profile.stats)
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_profile, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}