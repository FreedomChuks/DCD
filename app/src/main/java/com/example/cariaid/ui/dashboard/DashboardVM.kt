package com.example.cariaid.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cariaid.data.DashboardRepository
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.data.model.CharityHistory
import com.example.cariaid.data.model.History
import com.example.cariaid.data.model.UserResponse
import com.example.cariaid.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(private val dashboardRepository: DashboardRepository):ViewModel() {

    private var _uiState = MutableStateFlow<ResultState<List<CharityData?>>>(ResultState.Empty())
    val uiState : StateFlow<ResultState<List<CharityData?>>> = _uiState.asStateFlow()

    private var _dashboardUiState = MutableStateFlow<ResultState<List<CharityData?>>>(ResultState.Empty())
    val dashboardUiState : StateFlow<ResultState<List<CharityData?>>> = _dashboardUiState.asStateFlow()

    private var _userUiState = MutableStateFlow<ResultState<UserResponse>>(ResultState.Empty())
    val userUiState : StateFlow<ResultState<UserResponse>> = _userUiState.asStateFlow()

    private var _updateUserUiState = MutableStateFlow<ResultState<List<CharityHistory>>>(ResultState.Empty())
    val updateUserUiState : StateFlow<ResultState<List<CharityHistory>>> = _updateUserUiState.asStateFlow()

    private var _historyUiState = MutableStateFlow<ResultState<List<CharityHistory>>>(ResultState.Empty())
    val historyUiState : StateFlow<ResultState<List<CharityHistory>>> = _historyUiState.asStateFlow()

    private var _historyMetaUiState = MutableStateFlow<ResultState<History>>(ResultState.Empty())
    val historyMetaUiState : StateFlow<ResultState<History>> = _historyMetaUiState.asStateFlow()

    init {
        fetchCharities()
        fetchCharitiesWithLimit()
        fetchUser()
        fetchHistoryList()

    }

    private fun fetchCharities(){
        viewModelScope.launch {
            dashboardRepository.fetchCharities().collect {
                _uiState.value = it
            }
        }
    }

     private fun fetchCharitiesWithLimit(){
        viewModelScope.launch {
            dashboardRepository.fetchCharityWithLimit().collect {
                _dashboardUiState.value = it
            }
        }
    }

    private fun fetchUser(){
        viewModelScope.launch {
            dashboardRepository.fetchUser().collect {
                _userUiState.value = it
            }
        }
    }

     fun updateHistory(history:CharityHistory){
        viewModelScope.launch {
            dashboardRepository.fetchHistory(history).collect {
                _updateUserUiState.value = it
            }
        }
    }

    private fun fetchHistoryList(){
        viewModelScope.launch {
            dashboardRepository.fetchHistoryList().collect {
                _historyUiState.value = it
            }
        }
    }

     fun fetchHistoryMetadata(){
        viewModelScope.launch {
            dashboardRepository.fetchHistoryCount().collect {
                _historyMetaUiState.value = it
            }
        }
    }

    fun updateDonationAmount(donationRaised:String,key:String){
        dashboardRepository.updateCharity(donationRaised, key)
    }

}