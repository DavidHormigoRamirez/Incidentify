package com.alaturing.incidentify.authentication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alaturing.incidentify.authentication.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserLocalDatasourceDS @Inject constructor(
    private val preferences: DataStore<Preferences>
): UserLocalDatasource {

    private val tokenKey = stringPreferencesKey("token")
    private val userNameKey = stringPreferencesKey("username")
    private val emailKey = stringPreferencesKey("email")
    private val idKey = intPreferencesKey("id")

    override suspend fun saveUser(user: User) {
        preferences.edit {
            p ->
            p[idKey] = user.id
            p[userNameKey] = user.userName
            p[emailKey] = user.email
            user.token?.let {
                p[tokenKey] = it
            }
        }
    }

    override suspend fun retrieveUser(): User? {
        val tokenFlow = preferences.data.map { p ->
            p[tokenKey]
        }

        val token = tokenFlow.firstOrNull()
        token?.let {
            return User(
                id = 0,
                userName = "",
                email = "",
                token = token
            )
        }
        return null

    }

    override suspend fun clearUser() {

        preferences.edit {
                p ->
            p[idKey] = 0
            p[userNameKey] = ""
            p[emailKey] = ""
            p[tokenKey] = ""

        }
    }
}