package com.jason.returnoftheping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.R
import com.jason.returnoftheping.models.RegistrationConfirmationRequest
import com.jason.returnoftheping.models.RegistrationRequest
import kotlinx.android.synthetic.main.inbox_registration_item.view.*


/**
 * Created by Jason on 10/8/17.
 */
class InboxRegistrationAdapter(val messages: List<RegistrationRequest>, val listener: OnRegistrationConfirmationItemClickedListener) : RecyclerView.Adapter<InboxRegistrationAdapter.ViewHolder>() {

    interface OnRegistrationConfirmationItemClickedListener {
        fun onDenyRequest(item: RegistrationConfirmationRequest)
        fun onConfirmRequest(item: RegistrationConfirmationRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxRegistrationAdapter.ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inbox_match_item, parent, false))

    override fun onBindViewHolder(holder: InboxRegistrationAdapter.ViewHolder, position: Int) = holder.bindItem(messages[position])

    override fun getItemCount(): Int = messages.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view.rootView) {

        fun bindItem(registrationRequest: RegistrationRequest) {
            view.inbox_registration_confirmation_name.text = registrationRequest.name
            view.inbox_registration_confirmation_email.text = registrationRequest.email

            view.inbox_registration_confirm_btn.setOnClickListener { listener.onConfirmRequest(RegistrationConfirmationRequest(registrationRequest.token)) }
            view.inbox_registration_decline_btn.setOnClickListener { listener.onDenyRequest(RegistrationConfirmationRequest(registrationRequest.token)) }
        }
    }
}