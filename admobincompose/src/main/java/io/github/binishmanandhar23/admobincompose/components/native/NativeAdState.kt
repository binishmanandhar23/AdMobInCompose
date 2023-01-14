package io.github.binishmanandhar23.admobincompose.components.native

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import io.github.binishmanandhar23.admobincompose.utils.LibraryUtils.getActivity

@Composable
fun rememberCustomNativeAdState(
    adUnit: String,
    adListener: AdListener? = null,
    nativeAdOptions: NativeAdOptions? = null
) = LocalContext.current.getActivity()?.let {
    remember(adUnit) {
        NativeAdState(
            activity = it,
            adUnit = adUnit,
            adListener = adListener,
            adOptions = nativeAdOptions
        )
    }
}


class NativeAdState(
    activity: Activity,
    adUnit: String,
    adListener: AdListener?,
    adOptions: NativeAdOptions?
) {
    val nativeAd = MutableLiveData<NativeAd?>()

    init {
        AdLoader.Builder(activity, adUnit).let {
            if (adOptions != null)
                it.withNativeAdOptions(adOptions)
            else
                it
        }.let {
            if (adListener != null)
                it.withAdListener(adListener)
            else
                it
        }
            .forNativeAd { nativeAd ->
                if (activity.isDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                this.nativeAd.postValue(nativeAd)
            }.build().loadAd(AdRequest.Builder().build())
    }
}