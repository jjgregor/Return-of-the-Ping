package com.jason.returnoftheping.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.fragments.*
import com.jason.returnoftheping.models.Player
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.util.*


class MainActivity : AppCompatActivity(), AuthFragment.AuthCallbacks {

    val TAG = MainActivity::class.java.name
    private lateinit var authFrag: AuthFragment
    private lateinit var app: LOTPApp
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as LOTPApp
        setSupportActionBar(toolbar)

        fab.setOnClickListener { NewMatchDialogFragment().show(supportFragmentManager, "fragment_new_match") }

        setupViewPager()
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)

        if (app.getCurrentPlayer() == null) {
            fab.visibility = View.GONE
            authFrag = AuthFragment()
            authFrag.setAuthCallbacks(this)
            adapter.addFragment(authFrag, "Sign In/ Register")
        } else {
            fab.visibility = View.VISIBLE
            adapter.addFragment(LeaderBoardFragment(), "Leader Board")
            adapter.addFragment(ProfileFragment.newInstance(app.getCurrentPlayer() as Player), "Profile")
            adapter.addFragment(InboxFragment(), "Inbox")
        }
        tabs.setupWithViewPager(viewpager)
        viewpager.adapter = adapter
    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager), Serializable {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment = mFragmentList[position]

        override fun getCount(): Int = mFragmentList.size

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence = mFragmentTitleList[position]
    }

    override fun playerSignedIn(player: Player) {
        supportFragmentManager
                .beginTransaction()
                .remove(authFrag)
                .commitAllowingStateLoss()
        this.recreate()
    }

    override fun authCancelled() {
        Log.i(TAG, "sign in failed")
    }

}
