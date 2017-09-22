package com.jason.returnoftheping.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jason.returnoftheping.R
import com.jason.returnoftheping.databinding.LeaderBoardItemBinding
import com.jason.returnoftheping.models.LeaderBoardItem


/**
 * Created by Jason on 9/17/17.
 */
class LeaderBoardAdapter(val items: List<LeaderBoardItem>) : RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardAdapter.ViewHolder
            = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.leader_board_item, parent, false))

    inner class ViewHolder(val binding: LeaderBoardItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: LeaderBoardItem) {
            binding.item = item
        }
    }
}
