package com.jason.returnoftheping.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.models.Player

/**
 * Created by Jason on 10/8/17.
 */
class OpponentAutoCompleteAdapter(context: Context, private var mOpponents: List<Player>) : ArrayAdapter<Player>(context, LAYOUT_RES_ID, mOpponents), Filterable {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var v = convertView
        if (v == null) {
            v = mInflater.inflate(R.layout.new_match_opponent, parent, false)
        }
        val avatar = v?.findViewById<ImageView>(R.id.opponent_avatar)
        val name = v?.findViewById<TextView>(R.id.opponent_name)
        val email = v?.findViewById<TextView>(R.id.opponent_email)

        val opponent = getItem(position)
        name?.text = opponent?.name
        email?.text = opponent?.email
        return v
    }

    override fun getFilter(): Filter {

        val myFilter = object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val filteredPlayers = ArrayList<Player>()
                val currentPlayer = (context.applicationContext as LOTPApp).getCurrentPlayer()
                mOpponents
                        .filterNot { currentPlayer != null && currentPlayer == it }
                        .forEach { player ->
                            constraint?.let {
                                if (player.name?.toUpperCase()?.startsWith(it.toString().toUpperCase()) == true
                                        || player.email?.toUpperCase()?.contains(it.toString().toUpperCase()) == true ) {
                                    filteredPlayers.add(player)
                                }
                            }
                        }
                filterResults.values = filteredPlayers
                filterResults.count = filteredPlayers.size

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                clear()

                results.values?.let {
                    (results.values as ArrayList<*>).forEach { player -> add(player as? Player) }
                    if (results.count > 0) {
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return if (resultValue == null) "" else (resultValue as Player).name.toString()
            }
        }
        return myFilter
    }

    companion object {
        private val LAYOUT_RES_ID = R.layout.item_opponent
    }
}