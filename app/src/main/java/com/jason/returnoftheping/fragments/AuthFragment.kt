package com.jason.returnoftheping.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.models.Player
import com.jason.returnoftheping.models.SignInRegisterResponse
import com.jason.returnoftheping.preferences.Preferences
import kotlinx.android.synthetic.main.fragment_auth.*
import org.apache.commons.validator.routines.EmailValidator


/**
 * Created by Jason on 9/24/17.
 */

class AuthFragment : Fragment(), SignInFragment.SignInCallbacks {

    val TAG = AuthFragment::class.java.name
    private var callBacks: AuthCallbacks? = null
    private lateinit var app: LOTPApp

    interface AuthCallbacks {
        fun playerSignedIn(player: Player)

        fun authCancelled()
    }

    fun setAuthCallbacks(callback: AuthCallbacks) { callBacks = callback }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_auth, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addListeners()
        app = activity.application as LOTPApp
    }

    private fun addListeners() {
        sign_in_register_btn.setOnClickListener({
            auth_sign_in_cont
                    .animate()
                    .alpha(0f)
                    .setListener(
                            object : AnimatorListenerAdapter() {

                                override fun onAnimationStart(animation: Animator) {
                                    auth_register_cont.alpha = 0f
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    auth_sign_in_cont.visibility = View.GONE
                                    auth_register_cont.visibility = View.VISIBLE
                                    auth_register_cont.animate()
                                            .alpha(1f)
                                            .setListener(null)
                                            .start()
                                }
                            }
                    )
                    .start()
        })

        register_sign_in_btn.setOnClickListener({
            auth_register_cont
                    .animate()
                    .alpha(0f)
                    .setListener(
                            object : AnimatorListenerAdapter() {

                                override fun onAnimationStart(animation: Animator) {
                                    auth_sign_in_cont.alpha = 0f
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    auth_register_cont.visibility = View.GONE
                                    auth_sign_in_cont.visibility = View.VISIBLE
                                    auth_sign_in_cont.animate()
                                            .alpha(1f)
                                            .setListener(null)
                                            .start()
                                }
                            }
                    )
                    .start()
        })

        sign_in_btn.setOnClickListener {
            dismissKeyboard()
            val email = sign_in_email.text.toString()
            val password = sign_in_password.text.toString()
            if (email.isNullOrEmpty()) {
                sign_in_email.error = getString(R.string.auth_email_blank)
                return@setOnClickListener
            } else {
                sign_in_email.error = null
            }
            if (!EmailValidator.getInstance().isValid(email)) {
                sign_in_email.error = getString(R.string.auth_email_invalid)
                return@setOnClickListener
            } else {
                sign_in_email.error = null
            }
            if (password.isNullOrEmpty()) {
                sign_in_password.error = getString(R.string.auth_password_invalid)
                return@setOnClickListener
            } else {
                sign_in_password.error = null
            }
            val fragment = SignInFragment.newInstance(email, password)
            fragment.setSignInCallbacks(this@AuthFragment)
            fragmentManager
                    .beginTransaction()
                    .add(fragment, SignInFragment::class.java.name)
                    .commit()
        }

        register_btn.setOnClickListener {
            dismissKeyboard()
            val name = register_name.text.toString()
            val email = register_email.text.toString()
            val password = register_password.text.toString()
            if (name.isNullOrEmpty()) {
                register_name.error = getString(R.string.auth_name_invalid)
                return@setOnClickListener
            } else {
                register_name.error = null
            }
            if (email.isNullOrEmpty()) {
                register_email.error = getString(R.string.auth_email_blank)
                return@setOnClickListener
            } else {
                register_email.error = null
            }
            if (!EmailValidator.getInstance().isValid(email)) {
                register_email.error = getString(R.string.auth_email_invalid)
                return@setOnClickListener
            } else {
                register_email.error = null
            }
            if (password.isNullOrEmpty()) {
                register_password.error = getString(R.string.auth_password_invalid)
                return@setOnClickListener
            } else {
                register_password.error = null
            }

//            val fragment = RegisterFragment.newInstance(
//                    register_name.text.toString(),
//                    register_email.text.toString(),
//                    registerPassword.getText().toString()
//            )
//
//            fragment.setRegisterCallbacks(this@AuthFragment)
//            fragmentManager
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
//                    .add(fragment, RegisterFragment::class.java.getName())
//                    .commit()
        }
    }

    override fun signInSuccessful(response: SignInRegisterResponse) {
        playerSignedIn(response)
    }

    private fun playerSignedIn(response: SignInRegisterResponse) {
        Preferences().setCurrentPlayer(response.player, activity)
        Preferences().setAccessToken(response.accessToken, activity)
        app.setCurrentPlayer(response.player)

        callBacks?.let { it.playerSignedIn(response.player) }
        view?.let { Snackbar.make(it, getString(R.string.sign_in_successful), Snackbar.LENGTH_LONG).show() }

    }

    override fun signInFailed(error: String) {
        authFailed(false, error);
        view?.let {
            Snackbar.make(it, getString(R.string.sign_in_error), Snackbar.LENGTH_LONG).show();
        }
    }

    private fun authFailed(isRegistration: Boolean, error: String) {
        if (isRegistration) {
            register_btn.text = getString(R.string.try_again)
        } else {
            sign_in_btn.text = getString(R.string.try_again)
        }

        Log.d(TAG, "Failed to sign in/register: " + error)
    }

    private fun dismissKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(sign_in_email.windowToken, 0)
    }
}