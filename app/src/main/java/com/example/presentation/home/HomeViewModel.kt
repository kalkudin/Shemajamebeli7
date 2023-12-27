package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.home.model.DataObject
import com.example.domain.home.RecyclerDataRepository
import com.example.presentation.home.model.Key
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val recyclerDataRepository: RecyclerDataRepository) : ViewModel() {
    private val _recyclerItemList = MutableStateFlow<List<Key>>(emptyList())
    val recyclerItemList : StateFlow<List<Key>> = _recyclerItemList.asStateFlow()

    init{
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            recyclerDataRepository.getData().collect { dataObjects ->
                val keyList = dataObjects.map { dataObject ->
                    DataObjectToKeyMapper.map(dataObject)
                }
                _recyclerItemList.value = keyList
            }
        }
    }

    object DataObjectToKeyMapper {
        fun map(dataObject: DataObject): Key {
            return Key(
                number = dataObject.number,
                icon = dataObject.iconPath ,
                itemFunctionality = when (dataObject.functionality) {
                    DataObject.ItemFunctionality.SCAN -> Key.ItemFunction.SCAN
                    DataObject.ItemFunctionality.DELETE -> Key.ItemFunction.DELETE
                    DataObject.ItemFunctionality.NONE -> Key.ItemFunction.NONE
                },
            )
        }
    }
}