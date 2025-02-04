package com.alaturing.incidentify.authentication.data.repository

import com.alaturing.incidentify.authentication.model.User
import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepositoryFirebase @Inject constructor(
    private val auth: FirebaseAuth
):UserRepository {
    override suspend fun login(identifier: String, password: String): Result<User> {
        try {
            val result = auth.signInWithEmailAndPassword(identifier, password).await()
            return Result.success(
                User(0,result.user!!.toString(),result.user!!.toString(),"")
            )
        }
        catch (ex:FirebaseAuthInvalidCredentialsException) {
            return Result.failure(UserNotAuthorizedException())
        }
    }

    override suspend fun register(user: String, email: String, password: String): Result<User> {
        TODO()
        /**val result = auth.createUserWithEmailAndPassword(email,password).await()
        if (result.user != null) {

        }*/
    }

    override suspend fun logout() {
        auth.signOut()
    }
}