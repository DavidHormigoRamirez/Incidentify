package com.alaturing.incidentify.common.exception

class UserNotRegisteredException ():RuntimeException() {
    override fun toString() = "User cannot be registered"

}