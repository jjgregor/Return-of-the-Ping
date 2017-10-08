package com.jason.returnoftheping.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jason.returnoftheping.R
import com.jason.returnoftheping.databinding.MatchHistoryItemBinding
import com.jason.returnoftheping.models.Match


/**
 * Created by Jason on 9/30/17.
 */
class MatchHistoryAdapter(val matches: List<Match>) : RecyclerView.Adapter<MatchHistoryAdapter.ViewHolder>() {

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(matches[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHistoryAdapter.ViewHolder
            = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.match_history_item, parent, false))

    inner class ViewHolder(val binding: MatchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: Match) {

            binding.match = item

            val win = item.isWin
            binding.matchHistoryResult.text = if (win) "W" else "L"
            binding.matchHistoryResult.setBackgroundResource(if (win) R.drawable.win_background else R.drawable.loss_background)
        }
    }
}