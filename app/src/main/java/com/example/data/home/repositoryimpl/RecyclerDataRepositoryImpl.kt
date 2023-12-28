package com.example.data.home.repositoryimpl

import com.example.data.home.dto.RecyclerDataObjectDto
import com.example.domain.home.model.DataObject
import com.example.domain.home.repository.RecyclerDataRepository
import com.example.shemajamebeli7.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecyclerDataRepositoryImpl : RecyclerDataRepository {
    override fun getData(): Flow<List<DataObject>> {
        return flow {
            val dummyDataList = generateDummyData()

            delay(500)

            emit(dummyDataList)
        }
    }

    private fun generateDummyData(): List<DataObject> {
        val recyclerDataList = listOf(
            RecyclerDataObjectDto(number = 1, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 2, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 3, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 4, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 5, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 6, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 7, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 8, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = 9, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = null, iconPath = R.drawable.ic_touch, functionality = RecyclerDataObjectDto.ItemFunctionality.SCAN),
            RecyclerDataObjectDto(number = 0, iconPath = null, functionality = RecyclerDataObjectDto.ItemFunctionality.NONE),
            RecyclerDataObjectDto(number = null, iconPath = R.drawable.ic_delete, functionality = RecyclerDataObjectDto.ItemFunctionality.DELETE)
        )

        return recyclerDataList.map { recyclerData ->
            DataObject(
                number = recyclerData.number,
                iconPath = recyclerData.iconPath,
                functionality = mapFunctionality(recyclerData.functionality)
            )
        }
    }

    private fun mapFunctionality(dtoFunctionality: RecyclerDataObjectDto.ItemFunctionality): DataObject.ItemFunctionality {
        return when (dtoFunctionality) {
            RecyclerDataObjectDto.ItemFunctionality.SCAN -> DataObject.ItemFunctionality.SCAN
            RecyclerDataObjectDto.ItemFunctionality.DELETE -> DataObject.ItemFunctionality.DELETE
            RecyclerDataObjectDto.ItemFunctionality.NONE -> DataObject.ItemFunctionality.NONE
        }
    }
}