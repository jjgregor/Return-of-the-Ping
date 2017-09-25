package com.jason.returnoftheping.dagger

import com.jason.returnoftheping.services.LOTPService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Jason on 9/24/17.
 */
@Module
class LOTPServiceMod {

    @Provides
    internal fun provideLOTPService(restAdapter: Retrofit) : LOTPService {
        return restAdapter.create(LOTPService::class.java)
    }
}