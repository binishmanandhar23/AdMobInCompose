package io.github.binishmanandhar23.admobincompose.components.reward

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import io.github.binishmanandhar23.admobincompose.utils.LibraryUtils.getActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun rememberCustomRewardAd(adUnit: String,onAdFailedToLoad:((LoadAdError) -> Unit)? = null,onAdLoaded:(() -> Unit)? =null,  fullScreenContentCallback: FullScreenContentCallback? = null) =
    LocalContext.current.getActivity()?.let {
        remember(adUnit) {
            RewardAdState(
                it,
                adUnit = adUnit,
                fullScreenContentCallback = fullScreenContentCallback,
                onAdLoaded = onAdLoaded,
                onAdFailedToLoad = onAdFailedToLoad
            )
        }
    }

class RewardAdState(private val activity: Activity, val adUnit: String, val fullScreenContentCallback: FullScreenContentCallback?, val  onAdFailedToLoad:((LoadAdError) -> Unit)?, val onAdLoaded:(() -> Unit)?) {
    private var mRewardedAd: RewardedAd? = null

    init {
        init(adUnit)
    }

    private fun init(adUnit: String){
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            activity,
            adUnit,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                    onAdFailedToLoad?.invoke(adError)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    mRewardedAd?.fullScreenContentCallback = fullScreenContentCallback
                    onAdLoaded?.invoke()
                }
            })
    }

    suspend fun showAsync() = suspendCoroutine { cont ->
        mRewardedAd?.show(activity) {
            cont.resume(it)
        }
    }

    fun show(onUserEarnedRewardListener: OnUserEarnedRewardListener) =
        mRewardedAd?.show(activity, onUserEarnedRewardListener)

    fun refresh(adUnit: String? = null) = init(adUnit = adUnit?: this.adUnit)
}