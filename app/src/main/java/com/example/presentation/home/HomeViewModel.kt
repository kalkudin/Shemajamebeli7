package com.example.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.Resource
import com.example.domain.datastore.repository.UserDataStoreRepository
import com.example.domain.home.model.DataObject
import com.example.domain.home.repository.RecyclerDataRepository
import com.example.presentation.home.model.HomeKeyClickEvent
import com.example.presentation.home.model.Key
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//normally saving the password would happen elsewhere, but i am forced to do it in the here since i dont have any other fragments to work with
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recyclerDataRepository: RecyclerDataRepository,
    private val userDataStoreRepository: UserDataStoreRepository
) : ViewModel() {
    private val _recyclerItemList = MutableStateFlow<List<Key>>(emptyList())
    val recyclerItemList: StateFlow<List<Key>> = _recyclerItemList.asStateFlow()

    private val _successFlow = MutableStateFlow<Resource>(Resource.Nothing)
    val successFlow : StateFlow<Resource> = _successFlow.asStateFlow()

    private val passwordKeys = mutableListOf<String>()

    init {
        getData()
        savePasswordToDataStore()
    }

    fun onEvent(event : HomeKeyClickEvent){
        when(event){
            is HomeKeyClickEvent.AddKeyToPassword -> addKeyAndCheckPassword(event.number.toString())
            is HomeKeyClickEvent.DeletePasswordKey -> removeLastKey()
            is HomeKeyClickEvent.ScanUserFingerPrint -> futureScanFunction()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            recyclerDataRepository.getData().collect { dataObjects ->
                val keyList = dataObjects.map { dataObject ->
                    DataObjectToKeyMapper.map(dataObject)
                }

                _recyclerItemList.value = keyList
            }
        }
    }

    private fun savePasswordToDataStore(){
        viewModelScope.launch(){
            if(!userDataStoreRepository.isPasswordSaved()){
                userDataStoreRepository.savePassword("0934")
            }
        }
    }

    private fun addKeyAndCheckPassword(number: String) {
        viewModelScope.launch {
            if (passwordKeys.size < 4) {
                passwordKeys.add(number)
                if (passwordKeys.size == 4) {
                    checkPassword()
                }
            }
        }
    }

    private fun checkPassword() {
        viewModelScope.launch {
            val password = passwordKeys.joinToString("")
            userDataStoreRepository.comparePasswords(password).collect { resource ->
                _successFlow.value = resource
            }
            clearPassword()
            resetFlowValue()
        }
    }

    private fun removeLastKey(){
        viewModelScope.launch {
            if (passwordKeys.isNotEmpty()) {
                Log.d("HomeViewModel", "password before removal : ${passwordKeys.joinToString("")}")
                passwordKeys.removeAt(passwordKeys.size - 1)
                Log.d("HomeViewModel", "password after removal : ${passwordKeys.joinToString("")}")
            }
        }
    }

    private fun futureScanFunction(){
        viewModelScope.launch {
            _successFlow.value = Resource.Nothing
        }
    }

    private fun clearPassword() {
        passwordKeys.clear()
    }

    private fun resetFlowValue(){
        viewModelScope.launch(){
            _successFlow.value = Resource.Nothing
        }
    }

    object DataObjectToKeyMapper {
        fun map(dataObject: DataObject): Key {
            return Key(
                number = dataObject.number,
                icon = dataObject.iconPath,
                itemFunctionality = when (dataObject.functionality) {
                    DataObject.ItemFunctionality.SCAN -> Key.ItemFunction.SCAN
                    DataObject.ItemFunctionality.DELETE -> Key.ItemFunction.DELETE
                    DataObject.ItemFunctionality.NONE -> Key.ItemFunction.NONE
                }
            )
        }
    }
}