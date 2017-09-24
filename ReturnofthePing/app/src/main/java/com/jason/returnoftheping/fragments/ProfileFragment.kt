package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.SignInRegisterResponse


/**
 * Created by Jason on 9/24/17.
 */
class ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.name

    companion object {
        fun newInstance(player: SignInRegisterResponse): ProfileFragment {
            val args = Bundle()
            args.putSerializable(Constants.PLAYER, player)
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }

    }
}