package io.github.binishmanandhar23.admobincomposeexample

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import io.github.binishmanandhar23.admobincompose.components.banner.BannerAds
import io.github.binishmanandhar23.admobincompose.components.interstitial.InterstitialAdsState
import io.github.binishmanandhar23.admobincompose.components.interstitial.rememberInterstitialAdsState
import io.github.binishmanandhar23.admobincompose.components.native.*
import io.github.binishmanandhar23.admobincompose.components.reward.RewardAdState
import io.github.binishmanandhar23.admobincompose.components.reward.rememberCustomRewardAd
import io.github.binishmanandhar23.admobincomposeexample.states.AdState
import io.github.binishmanandhar23.admobincomposeexample.ui.theme.AdMobInComposeTheme
import io.github.binishmanandhar23.admobincomposeexample.viewmodel.MainViewModel
import kotlinx.coroutines.launch

const val BANNER_AD_UNIT = "ca-app-pub-3940256099942544/6300978111"
const val INTERSTITIAL_AD_UNIT = "ca-app-pub-3940256099942544/1033173712"
const val NATIVE_AD_AD_UNIT = "ca-app-pub-3940256099942544/2247696110"
const val NATIVE_AD_AD_UNIT_VIDEO = "ca-app-pub-3940256099942544/1044960115"
const val REWARD_AD_AD_UNIT = "ca-app-pub-3940256099942544/5224354917"

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bannerAdState by mainViewModel.bannerAdState.collectAsState()
            val interstitialAdState by mainViewModel.interstitialAdState.collectAsState()
            val nativeAdState by mainViewModel.nativeAdState.collectAsState()
            val rewardAdState by mainViewModel.rewardAdState.collectAsState()
            val rememberInterstitialAdState =
                rememberInterstitialAdsState(
                    adUnit = INTERSTITIAL_AD_UNIT,
                    onAdLoaded = {
                        mainViewModel.updateInterstitialAdState(AdState(isSuccess = true))
                    }, onAdLoadFailed = {
                        mainViewModel.updateInterstitialAdState(
                            AdState(
                                isError = true,
                                errorMessage = it.message
                            )
                        )
                    }, fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mainViewModel.updateInterstitialAdState(
                                AdState(
                                    isError = true,
                                    errorMessage = p0.message
                                )
                            )
                        }
                    })
            val rememberCustomNativeAdState = rememberCustomNativeAdState(
                adUnit = NATIVE_AD_AD_UNIT /*For video need to use set test device configuration*/,
                nativeAdOptions = NativeAdOptions.Builder()
                    .setVideoOptions(
                        VideoOptions.Builder()
                            .setStartMuted(true).setClickToExpandRequested(true)
                            .build()
                    ).setRequestMultipleImages(true)
                    .build(),
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        mainViewModel.updateNativeAdState(AdState(isSuccess = true))
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        mainViewModel.updateNativeAdState(
                            nativeAdState = AdState(
                                isError = true,
                                errorMessage = p0.message
                            )
                        )
                    }
                }
            )
            val rememberCustomRewardAdState =
                rememberCustomRewardAd(adUnit = REWARD_AD_AD_UNIT, onAdFailedToLoad = {
                    mainViewModel.updateRewardAdState(
                        AdState(
                            isError = true,
                            errorMessage = it.message
                        )
                    )
                }, onAdLoaded = {
                    mainViewModel.updateRewardAdState(AdState(isSuccess = true))
                }, fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        mainViewModel.updateRewardAdState(
                            AdState(
                                isError = true,
                                errorMessage = p0.message
                            )
                        )
                    }
                })

            AdMobInComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    //LazySection() /*Lazy Column example*/
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
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.Black.copy(0.3f)
                        )
                        RewardAdsSection(
                            rewardAdState = rewardAdState,
                            rememberCustomRewardAdState = rememberCustomRewardAdState
                        )
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.Black.copy(0.3f)
                        )
                        NativeAdsSection(
                            nativeAdState = nativeAdState,
                            rememberNativeAdState = rememberCustomNativeAdState
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun LazySection(){
        /*Uses same Native Ad*/
        /*val nativeAdState by mainViewModel.nativeAdState.collectAsState()
        val rememberCustomNativeAdState = rememberCustomNativeAdState(
            adUnit = NATIVE_AD_AD_UNIT *//*For video need to use set test device configuration*//*,
            nativeAdOptions = NativeAdOptions.Builder()
                .setVideoOptions(
                    VideoOptions.Builder()
                        .setStartMuted(true).setClickToExpandRequested(true)
                        .build()
                ).setRequestMultipleImages(true)
                .build(),
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    mainViewModel.updateNativeAdState(AdState(isSuccess = true))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mainViewModel.updateNativeAdState(
                        nativeAdState = AdState(
                            isError = true,
                            errorMessage = p0.message
                        )
                    )
                }
            }
        )*/
        LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            items(30){
                /*Creates different Native Ads*/
                val nativeAdState by mainViewModel.nativeAdState.collectAsState()
                val rememberCustomNativeAdState = rememberCustomNativeAdState(
                    adUnit = NATIVE_AD_AD_UNIT, //For video need to use set test device configuration
                    nativeAdOptions = NativeAdOptions.Builder()
                        .setVideoOptions(
                            VideoOptions.Builder()
                                .setStartMuted(true).setClickToExpandRequested(true)
                                .build()
                        ).setRequestMultipleImages(true)
                        .build(),
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            mainViewModel.updateNativeAdState(AdState(isSuccess = true))
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            mainViewModel.updateNativeAdState(
                                nativeAdState = AdState(
                                    isError = true,
                                    errorMessage = p0.message
                                )
                            )
                        }
                    }
                )
                if(it % 10 == 0)
                    NativeAdsSection(nativeAdState = nativeAdState, rememberNativeAdState = rememberCustomNativeAdState)
                else
                    Box {
                        Text(text = "Items", modifier =  Modifier.align(Alignment.Center).fillMaxWidth().padding(20.dp))
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
                    mainViewModel.updateBannerAdState(
                        AdState(
                            isError = true,
                            errorMessage = p0.message
                        )
                    )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                interstitialAdState.isSuccess -> Text(
                    "InterstitialAd loaded successfully",
                    textAlign = TextAlign.Center
                )
                interstitialAdState.isError -> Text(
                    "InterstitialAd load failed: ${interstitialAdState.errorMessage}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    private fun NativeAdsSection(
        nativeAdState: AdState,
        rememberNativeAdState: NativeAdState?
    ) {
        rememberNativeAdState?.let {
            val nativeAd by it.nativeAd.observeAsState()
            Text("Native Ad", style = TextStyle(fontWeight = FontWeight.Bold))
            NativeAdsDesign(nativeAd)
            when {
                nativeAdState.isSuccess -> Text("Native ad loaded successfully")
                nativeAdState.isError -> Text("Native Ad load failed: ${nativeAdState.errorMessage}")
            }
        }
    }


    @Composable
    private fun NativeAdsDesign(nativeAd: NativeAd?) {
        if (nativeAd != null)
            NativeAdViewCompose { nativeAdView ->
                nativeAdView.setNativeAd(nativeAd)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Icon
                        NativeAdView(getView = {
                            nativeAdView.iconView = it
                        }, modifier = Modifier.weight(0.3f)) {
                            NativeAdImage(
                                drawable = nativeAd.icon?.drawable,
                                contentDescription = "Icon",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Column(
                            modifier = Modifier.weight(0.7f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            //Headline
                            NativeAdView(getView = {
                                nativeAdView.headlineView = it
                            }) {
                                Text(
                                    text = nativeAd.headline ?: "-",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    fontSize = 20.sp
                                )
                            }
                            //Body
                            NativeAdView(getView = {
                                nativeAdView.bodyView = it
                            }) {
                                Text(text = nativeAd.body ?: "-", fontSize = 15.sp)
                            }
                        }
                    }
                    //Video
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        nativeAd.mediaContent?.let { mediaContent ->
                            NativeAdMediaView(
                                modifier = Modifier.fillMaxWidth(),
                                nativeAdView = nativeAdView,
                                mediaContent = mediaContent,
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            )
                            /**Alternate method**/
                            /*NativeAdMediaView(
                                modifier = Modifier.fillMaxWidth()
                            ){
                                nativeAdView.mediaView = it
                                nativeAdView.mediaView?.setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                                nativeAdView.mediaView?.setMediaContent(mediaContent)
                            }*/
                        }
                    }
                }
            }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun RewardAdsSection(
        rewardAdState: AdState,
        rememberCustomRewardAdState: RewardAdState?
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        val coroutineScope = rememberCoroutineScope()
        rememberCustomRewardAdState?.let {
            Text("Reward Ad", style = TextStyle(fontWeight = FontWeight.Bold))
            AnimatedContent(targetState = rewardAdState.isSuccess) { success ->
                if (success)
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            coroutineScope.launch {
                                val rewardItem = rememberCustomRewardAdState.showAsync()
                                Log.i(
                                    "RewardItem",
                                    "Amount: ${rewardItem.amount} Type: ${rewardItem.type}"
                                )
                            }
                        },
                        shape = RoundedCornerShape(40.dp),
                        elevation = ButtonDefaults.elevation(5.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text(text = "Show Reward ad")
                    }
                else
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
            }
            when {
                rewardAdState.isSuccess -> Text("Reward ad loaded successfully")
                rewardAdState.isError -> Text("Reward Ad load failed: ${rewardAdState.errorMessage}")
            }
        }
    }
}

