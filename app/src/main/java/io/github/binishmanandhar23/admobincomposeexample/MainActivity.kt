package io.github.binishmanandhar23.admobincomposeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.github.binishmanandhar23.admobincompose.components.BannerAds
import io.github.binishmanandhar23.admobincompose.components.interstitial.InterstitialAdsState
import io.github.binishmanandhar23.admobincompose.components.interstitial.rememberInterstitialAdsState
import io.github.binishmanandhar23.admobincomposeexample.states.AdState
import io.github.binishmanandhar23.admobincomposeexample.ui.theme.AdMobInComposeTheme
import io.github.binishmanandhar23.admobincomposeexample.viewmodel.MainViewModel

const val BANNER_AD_UNIT = "ca-app-pub-3940256099942544/6300978111"
const val INTERSTITIAL_AD_UNIT = "ca-app-pub-3940256099942544/1033173712"

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bannerAdState by mainViewModel.bannerAdState.collectAsState()
            val interstitialAdState by mainViewModel.interstitialAdState.collectAsState()
            val rememberInterstitialAdState =
                rememberInterstitialAdsState(
                    adUnit = INTERSTITIAL_AD_UNIT,
                    onAdLoaded = {
                        mainViewModel.updateInterstitialAdState(AdState(isSuccess = true))
                    }, onAdLoadFailed = {
                        mainViewModel.updateInterstitialAdState(AdState(isError = true, errorMessage = it.message))
                    }, fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mainViewModel.updateInterstitialAdState(AdState(isError = true, errorMessage = p0.message))
                        }
                    })
            AdMobInComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        BannerAdsSection(bannerAdState = bannerAdState)
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.Black.copy(0.3f)
                        )
                        InterstitialAdsSection(
                            interstitialAdState = interstitialAdState,
                            rememberInterstitialAdState = rememberInterstitialAdState
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun BannerAdsSection(bannerAdState: AdState) {
        Text("Banner Ad", style = TextStyle(fontWeight = FontWeight.Bold))
        LoadingBannerAds()
        when {
            bannerAdState.isSuccess -> Text("BannerAd loaded successfully")
            bannerAdState.isError -> Text("BannerAd load failed: ${bannerAdState.errorMessage}")
        }
    }

    @Composable
    private fun LoadingBannerAds() {
        BannerAds(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), adUnit = BANNER_AD_UNIT,
            adSize = AdSize.LARGE_BANNER,
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    mainViewModel.updateBannerAdState(AdState(isSuccess = true))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mainViewModel.updateBannerAdState(AdState(isError = true, errorMessage = p0.message))
                }
            }
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun InterstitialAdsSection(
        interstitialAdState: AdState,
        rememberInterstitialAdState: InterstitialAdsState?
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Interstitial Ad", style = TextStyle(fontWeight = FontWeight.Bold))
            AnimatedContent(targetState = interstitialAdState.isSuccess) { success ->
                if (success)
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            rememberInterstitialAdState?.show()
                        },
                        shape = RoundedCornerShape(40.dp),
                        elevation = ButtonDefaults.elevation(5.dp)
                    ) {
                        Text(text = "Show Interstitial ad")
                    }
                else
                    CircularProgressIndicator()
            }
            when {
                interstitialAdState.isSuccess -> Text("InterstitialAd loaded successfully", textAlign = TextAlign.Center)
                interstitialAdState.isError -> Text("InterstitialAd load failed: ${interstitialAdState.errorMessage}", textAlign = TextAlign.Center)
            }
        }
    }
}

