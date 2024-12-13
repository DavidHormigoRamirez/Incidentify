package com.alaturing.incidentify.remote

import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.alaturing.incidentify.common.exception.UserNotRegisteredException
import com.alaturing.incidentify.login.data.User
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

private const val FAKE_DELAY = 1000L
/**
 * Clase "fake" que implementa [RemoteDatasource] para simular el login remoto
 */
class RemoteDatasourceMock @Inject constructor():RemoteDatasource {
    private val _users = mutableListOf<User>()

    /**
     *
     * @param identifier
     * @param password
     * @return [Result]
     */
    override suspend fun login(identifier: String, password: String): Result<User> {
        delay(FAKE_DELAY)
        // Existe el usuario?
        val user = getUser(identifier)

        // Si no es nulo, le generamos un token nuevo y lo almacenamos en el remoto
        user?.let {
            if (it.password==password) {
                val token = UUID.randomUUID()
                val position = _users.indexOf(it)
                _users[position].token = token.toString()
                return Result.success(_users[position])
            }
            else{
                // el pass no coincide
                return Result.failure(UserNotAuthorizedException())
            }
        }
        // no existe el usuario, fallo
        return Result.failure(UserNotAuthorizedException())

    }

    /**
     *
     * @param userName
     * @param email
     * @param password
     * @return [Result]
     */
    override suspend fun register(userName: String, email: String, password: String): Result<User> {

        delay(FAKE_DELAY)
        return if (userExists(userName,email)) {

            val newUser = User(_users.size+1,userName,email,password,null)
            _users.add(newUser)
            Result.success(newUser)
        }
        else Result.failure(UserNotRegisteredException())
    }

    private fun userExists(userName:String,email:String) = getUser(userName,email)==null

    private fun getUser(userName:String,email:String) =
        _users.find { u -> (u.userName == userName || u.email == email)}

    private fun getUser(identifier:String) =
        _users.find { u -> (u.userName == identifier || u.email == identifier)}
}