package com.example.listfetcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfetcher.data.DataObj
import com.example.listfetcher.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val dataList: List<DataObj?> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private var shouldShowNulls: Boolean = false

    init {
        syncData()
        refreshData(shouldShowNulls)
    }

    fun syncData() {
        viewModelScope.launch {
            repo.syncDataList()
        }
    }

    fun toggleNullData() {
        refreshData(!shouldShowNulls)
    }

    fun refreshData(showNulls: Boolean) {
        viewModelScope.launch {
            val syncedList = if (showNulls) {
                repo.getAllData()
            } else {
                repo.getCleanedData()
            }
            _uiState.value = _uiState.value.copy(dataList = syncedList.first())
            shouldShowNulls = showNulls
        }
    }

}