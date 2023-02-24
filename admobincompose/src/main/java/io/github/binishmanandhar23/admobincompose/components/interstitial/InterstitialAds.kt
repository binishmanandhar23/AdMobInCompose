package io.github.binishmanandhar23.admobincompose.components.interstitial

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.github.binishmanandhar23.admobincompose.utils.LibraryUtils.getActivity

@Composable
fun rememberInterstitialAdsState(
    adUnit: String,
    onAdLoaded: (() -> Unit)? = null,
    onAdLoadFailed: ((adError: LoadAdError) -> Unit)? = null,
    fullScreenContentCallback: FullScreenContentCallback? = null
) = LocalContext.current.getActivity()?.let {
    remember(adUnit) {
        InterstitialAdsState(
            activity = it,
            adUnit = adUnit,
            onAdLoadFailed = onAdLoadFailed,
            onAdLoaded = onAdLoaded,
            fullScreenContentCallback = fullScreenContentCallback
        )
    }
}

class InterstitialAdsState(
    private val activity: Activity,
    val adUnit: String,
    val onAdLoaded: (() -> Unit)?,
    val onAdLoadFailed: ((adError: LoadAdError) -> Unit)?,
    val fullScreenContentCallback: FullScreenContentCallback?
) {
    private var mInterstitialAd: InterstitialAd? = null

    init {
      init(adUnit)
    }

    private fun init(adUnit: String) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
            adUnit,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    onAdLoadFailed?.invoke(adError)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = fullScreenContentCallback
                    onAdLoaded?.invoke()
                }
            })
    }

    fun show() = mInterstitialAd?.show(activity)

    fun refresh(adUnit: String? = null) = init(adUnit = adUnit?: this.adUnit)
}