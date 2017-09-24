package com.jason.returnoftheping.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import kotlinx.android.synthetic.main.fragment_auth.*
import org.apache.commons.validator.routines.EmailValidator
import java.security.MessageDigest


/**
 * Created by Jason on 9/24/17.
 */

class AuthFragment : Fragment() {

    val TAG = AuthFragment::class.java.name
    private lateinit var app: LOTPApp

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_auth, container, false)
        app = activity?.application as LOTPApp

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addListeners()
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
//            val fragment = SignInFragment.newInstance(email, password)
//            fragment.setSignInCallbacks(this@AuthFragment)
//            fragmentManager
//                    .beginTransaction()
//                    .add(fragment, SignInFragment::class.java.getName())
//                    .commit()
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

    fun sha256(input: String?): String? {
        var result: String? = input
        input?.let {
            try {
                val md = MessageDigest.getInstance("SHA-256")
                md.update(input.toByteArray(charset("UTF-8")))
                val digest = md.digest()
                result = String.format("%064x", java.math.BigInteger(1, digest))
            } catch (e: Exception) {
                Log.e(TAG, "Error hashing password: ", e)
            }
        }

        return result
    }

    private fun dismissKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(sign_in_email.windowToken, 0)
    }
}