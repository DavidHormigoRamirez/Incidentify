package com.alaturing.incidentify.common.exception



class UserNotAuthorizedException :RuntimeException() {

    override fun toString() = "Incorrect identifier or password"
}