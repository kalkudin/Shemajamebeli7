package com.example.domain.datastore.repository

import com.example.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface UserDataStoreRepository {
    fun readPassword() : Flow<String>
    suspend fun savePassword(password : String)
    suspend fun isPasswordSaved() : Boolean
    fun comparePasswords(input: String): Flow<Resource>
}