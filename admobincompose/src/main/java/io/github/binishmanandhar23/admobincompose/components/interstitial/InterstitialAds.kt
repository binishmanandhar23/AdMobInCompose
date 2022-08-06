package io.github.binishmanandhar23.admobincompose.components.interstitial

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.github.binishmanandhar23.admobincompose.utils.LibraryUtils.getActivity

@Composable
fun rememberInterstitialAdsState(adUnit: String,onAdLoaded:(() -> Unit)? = null, onAdLoadFailed: ((adError: LoadAdError) -> Unit)? = null, fullScreenContentCallback: FullScreenContentCallback? = null) =
    LocalContext.current.getActivity()?.let {
        InterstitialAdsState(activity = it, adUnit = adUnit, onAdLoadFailed = onAdLoadFailed, onAdLoaded = onAdLoaded, fullScreenContentCallback = fullScreenContentCallback)
    }

class InterstitialAdsState(private val activity: Activity, adUnit: String,onAdLoaded:(() -> Unit)?, onAdLoadFailed: ((adError: LoadAdError) -> Unit)?, fullScreenContentCallback: FullScreenContentCallback?){
    private var mInterstitialAd: InterstitialAd? = null
    init {
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
}