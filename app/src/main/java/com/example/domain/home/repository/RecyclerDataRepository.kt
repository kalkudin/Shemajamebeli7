package com.example.domain.home.repository

import com.example.domain.home.model.DataObject
import kotlinx.coroutines.flow.Flow

interface RecyclerDataRepository {
    fun getData() : Flow<List<DataObject>>
}