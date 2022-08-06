package io.github.binishmanandhar23.admobincomposeexample.states

import com.google.android.gms.ads.LoadAdError

data class AdState(val isSuccess: Boolean = false, val isError: Boolean = false, val errorMessage: String? = null)
