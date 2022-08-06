package io.github.binishmanandhar23.admobincomposeexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.binishmanandhar23.admobincomposeexample.states.AdState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private var _bannerAdState = MutableStateFlow(AdState())
    val bannerAdState = _bannerAdState.asStateFlow()
    fun updateBannerAdState(bannerAdState: AdState) = viewModelScope.launch {
        _bannerAdState.update { bannerAdState }
    }

    private var _interstitialAdState = MutableStateFlow(AdState())
    val interstitialAdState = _interstitialAdState.asStateFlow()
    fun updateInterstitialAdState(interstitialAdState: AdState) = viewModelScope.launch {
        _interstitialAdState.update { interstitialAdState }
    }

    private var _nativeAdState = MutableStateFlow(AdState())
    val nativeAdState = _nativeAdState.asStateFlow()
    fun updateNativeAdState(nativeAdState: AdState) = viewModelScope.launch {
        _nativeAdState.update { nativeAdState }
    }

    private var _rewardAdState = MutableStateFlow(AdState())
    val rewardAdState = _rewardAdState.asStateFlow()
    fun updateRewardAdState(rewardAdState: AdState) = viewModelScope.launch {
        _rewardAdState.update { rewardAdState }
    }
}