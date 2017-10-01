package com.jason.returnoftheping.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.R
import com.jason.returnoftheping.fragments.AuthFragment
import com.jason.returnoftheping.fragments.LeaderBoardFragment
import com.jason.returnoftheping.fragments.ProfileFragment
import com.jason.returnoftheping.models.Player
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var app: LOTPApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as LOTPApp
        setSupportActionBar(toolbar)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LeaderBoardFragment(), "Leader Board")
        if(app.getCurrentPlayer() == null){
            adapter.addFragment(AuthFragment(), "Profile")
        } else {
            adapter.addFragment(ProfileFragment.newInstance(app.getCurrentPlayer() as Player), "Profile")
        }
        tabs.setupWithViewPager(viewpager)
        viewpager.adapter = adapter
    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList.get(position)
        }
    }
}
