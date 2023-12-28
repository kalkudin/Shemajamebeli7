package com.example.presentation.home

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.Resource
import com.example.domain.datastore.repository.UserDataStoreRepository
import com.example.domain.home.model.DataObject
import com.example.domain.home.repository.RecyclerDataRepository
import com.example.presentation.home.events.HomeKeyClickEvent
import com.example.presentation.home.model.DotBelowThePasscode
import com.example.presentation.home.model.Key
import com.example.shemajamebeli7.R
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

    private val _filledKeysFlow = MutableStateFlow<List<DotBelowThePasscode>>(emptyList())
    val filledKeysFlow : StateFlow<List<DotBelowThePasscode>> = _filledKeysFlow.asStateFlow()

    private val passwordKeys = mutableListOf<String>()

    private val listOfFilledKeys = mutableListOf(
        DotBelowThePasscode(id = 1, imagePath = R.drawable.ic_grey_circle),
        DotBelowThePasscode(id = 2, imagePath = R.drawable.ic_grey_circle),
        DotBelowThePasscode(id = 3, imagePath = R.drawable.ic_grey_circle),
        DotBelowThePasscode(id = 4, imagePath = R.drawable.ic_grey_circle),
    )

    init {
        getData()
        savePasswordToDataStore()
        initializeSmallDots()
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
                updateFilledKeys()
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
                d("HomeViewModel", "password before removal : ${passwordKeys.joinToString("")}")

                val indexToRemove = passwordKeys.size - 1

                updateFilledKeyState(indexToRemove, false)

                passwordKeys.removeAt(indexToRemove)

                d("HomeViewModel", "password after removal : ${passwordKeys.joinToString("")}")
            }
        }
    }

    private fun futureScanFunction(){
        viewModelScope.launch {
            _successFlow.value = Resource.Nothing
        }
    }

    private fun clearPassword() {
        _filledKeysFlow.value = listOfFilledKeys
        passwordKeys.clear()
    }

    private fun resetFlowValue(){
        viewModelScope.launch(){
            _successFlow.value = Resource.Nothing
        }
    }

    private fun initializeSmallDots(){
        viewModelScope.launch {
            _filledKeysFlow.value = listOfFilledKeys
        }
    }

    private fun updateFilledKeys() {
        val currentPasswordLength = passwordKeys.size
        val updatedList = _filledKeysFlow.value.toMutableList()

        if (currentPasswordLength <= updatedList.size) {
            updatedList[currentPasswordLength - 1] =
                updatedList[currentPasswordLength - 1].copy(imagePath = R.drawable.ic_green_circle)

            _filledKeysFlow.value = updatedList
            d("HomeViewModel", "_filledKeysFlow values: ${_filledKeysFlow.value}")
        }
    }

    private fun updateFilledKeyState(index: Int, isFilled: Boolean) {
        val updatedList = _filledKeysFlow.value.toMutableList()

        // Ensure the index is within bounds
        if (index < updatedList.size) {
            // Update the imagePath property at the specified index
            updatedList[index] = updatedList[index].copy(imagePath = if (isFilled) R.drawable.ic_green_circle else R.drawable.ic_grey_circle)

            // Update the _filledKeysFlow
            _filledKeysFlow.value = updatedList

            // Log the values of _filledKeysFlow
            Log.d("HomeViewModel", "_filledKeysFlow values: ${_filledKeysFlow.value}")
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