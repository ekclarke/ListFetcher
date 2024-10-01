package com.example.listfetcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfetcher.data.DataObj
import com.example.listfetcher.data.Repository
import com.example.listfetcher.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        syncData()
    }

    fun syncData() {
        viewModelScope.launch {
            repo.syncDataList()
            val syncedList = repo.getCleanedData().first()
            _uiState.value = _uiState.value.copy(dataList = syncedList)
        }
    }
}