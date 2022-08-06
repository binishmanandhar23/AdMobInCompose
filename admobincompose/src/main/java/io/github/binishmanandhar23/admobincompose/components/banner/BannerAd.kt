package io.github.binishmanandhar23.admobincompose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.lang.Exception


@Composable
fun BannerAds(
    modifier: Modifier,
    adUnit: String,
    adSize: AdSize = AdSize.BANNER,
    adListener: AdListener? = null
) {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode)
        Text(
            "Banner Ad",
            modifier = modifier,
            style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        )
    else
        AndroidView(factory = {
            AdView(it)
        }, modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp), update = {
            if (it.adSize == null)
                it.setAdSize(adSize)
            try {
                it.adUnitId = adUnit //Can only be set once
            } catch (e: Exception){
                e.printStackTrace()
            }
            if (adListener != null)
                it.adListener = adListener

            it.loadAd(AdRequest.Builder().build())
        })
}
