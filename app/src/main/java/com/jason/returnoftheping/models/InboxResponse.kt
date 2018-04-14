package com.jason.returnoftheping.models

/**
 * Created by Jason on 10/1/17.
 */
data class InboxResponse(val matches: ArrayList<Match>,
                         val registrationRequests: ArrayList<RegistrationRequest>)