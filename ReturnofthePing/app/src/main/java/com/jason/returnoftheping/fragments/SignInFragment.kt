package com.jason.returnoftheping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.constants.Constants
import com.jason.returnoftheping.models.SignInRegisterResponse
import com.jason.returnoftheping.services.LOTPService
import com.jason.returnoftheping.util.HelperUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by Jason on 9/24/17.
 */
class SignInFragment : Fragment() {

    private val TAG = SignInFragment::class.java.name
    private var callbacks: SignInCallbacks? = null

    @Inject lateinit var service: LOTPService

    companion object {
        fun newInstance(email: String, password: String): SignInFragment {
            val args = Bundle()
            args.putString(Constants.EMAIL, email)
            args.putString(Constants.PASSWORD, password)
            val fragment = SignInFragment()
            fragment.arguments = args
            return fragment
        }
    }

    interface SignInCallbacks {
        fun signInSuccessful(response: SignInRegisterResponse)

        fun signInFailed(error: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LOTPApp.component.inject(this)

        val email = arguments.getString(Constants.EMAIL)
        val password = HelperUtil().sha256(arguments.getString(Constants.PASSWORD)) as String

        signIn(email, password)
    }

    private fun signIn(email: String, password: String) {
        service.signIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response != null) {
                        callbacks?.signInSuccessful(response)
                    } else {
                        callbacks?.signInFailed(getString(R.string.sign_in_error))
                    }
                    detach()
                }, { t: Throwable? ->
                    Log.e(TAG, "Sign In failed: ", t)
                    callbacks?.signInFailed(getString(R.string.sign_in_error))
                })
    }

    fun setSignInCallbacks(callback: SignInCallbacks) {
        callbacks = callback
    }

    private fun detach() {
        if (isAdded) {
            fragmentManager
                    .beginTransaction()
                    .remove(this@SignInFragment)
                    .commitAllowingStateLoss()
        }
    }
}