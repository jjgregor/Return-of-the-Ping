package com.jason.returnoftheping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jason.returnoftheping.R
import com.jason.returnoftheping.models.Match
import kotlinx.android.synthetic.main.inbox_match_item.view.*

/**
 * Created by Jason on 10/1/17.
 */

class InboxMatchesAdapter(val messages: List<Match>, val  listener: OnMatchConfirmationItemClickedListener) : RecyclerView.Adapter<InboxMatchesAdapter.ViewHolder>() {
    interface OnMatchConfirmationItemClickedListener {
        fun onItemClicked(item: Boolean, id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inbox_match_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(messages[position])

    override fun getItemCount(): Int = messages.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view.rootView) {

        fun bindItem(item: Match) {
            val win = item.isWin
            view.inbox_match_confirmation_result_lbl.text = if (win) "W" else "L"
            view.inbox_match_confirmation_result_lbl.setBackgroundResource(if (win) R.drawable.win_background else R.drawable.loss_background)

            view.inbox_match_match.text = StringBuilder()
                    .append(item.opponentName)
                    .append(" (")
                    .append(item.wins)
                    .append("-")
                    .append(item.losses)
                    .append(") ")
                    .append(item.date)

            view.inbox_registration_decline_btn.setOnClickListener { listener.onItemClicked(false, item.id) }
            view.inbox_registration_confirm_btn.setOnClickListener { listener.onItemClicked(true, item.id) }
        }
    }
}