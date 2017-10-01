package com.jason.returnoftheping.dagger

import com.jason.returnoftheping.LOTPApp
import com.jason.returnoftheping.activities.MainActivity
import com.jason.returnoftheping.fragments.InboxFragment
import com.jason.returnoftheping.fragments.LeaderBoardFragment
import com.jason.returnoftheping.fragments.ProfileFragment
import com.jason.returnoftheping.fragments.SignInFragment
import com.jason.returnoftheping.services.LOTPService
import dagger.Component
import javax.inject.Singleton


/**
 * Created by Jason on 9/24/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, LOTPServiceMod::class))
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(leaderBoardFragment: LeaderBoardFragment)

    fun inject(signInFragment: SignInFragment)

    fun inject(service: LOTPService)

    fun inject(app: LOTPApp)

    fun inject(profileFragment: ProfileFragment)

    fun inject(inboxFragment: InboxFragment)
}