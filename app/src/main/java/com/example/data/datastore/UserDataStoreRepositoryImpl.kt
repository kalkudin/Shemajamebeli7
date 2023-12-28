package com.example.data.datastore

import android.util.Log
import android.util.Log.d
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.data.common.Resource
import com.example.domain.datastore.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserDataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    UserDataStoreRepository {

    private object PreferencesKey {
        val PASSWORD = stringPreferencesKey("password")
        val IS_PASSWORD_SAVED = booleanPreferencesKey("is_password_saved")
    }

    override fun readPassword(): Flow<String> {
        return dataStore.data.map { preferences ->
            d("DataStore", "Password Read")
            preferences[PreferencesKey.PASSWORD] ?: ""
        }
    }

    override suspend fun savePassword(password: String) {
        dataStore.edit { preferences ->
            d("DataStore", "Password Saved")
            preferences[PreferencesKey.PASSWORD] = password
            preferences[PreferencesKey.IS_PASSWORD_SAVED] = true
        }
    }

    override suspend fun isPasswordSaved(): Boolean {
        d("DataStore", "Password Checked")
        return dataStore.data.first()[PreferencesKey.IS_PASSWORD_SAVED] ?: false
    }

    override fun comparePasswords(input: String): Flow<Resource> {
        return flow {
            val savedPassword = readPassword().first()

            when {
                input.length < 4 -> {
                    emit(Resource.Nothing)
                }
                input == savedPassword -> {
                    emit(Resource.Success("Success"))
                }
                else -> {
                    emit(Resource.Failure("Incorrect Password"))
                }
            }
        }
    }
}