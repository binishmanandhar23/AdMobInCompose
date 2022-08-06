package io.github.binishmanandhar23.admobincomposeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import io.github.binishmanandhar23.admobincompose.components.BannerAds
import io.github.binishmanandhar23.admobincomposeexample.states.AdState
import io.github.binishmanandhar23.admobincomposeexample.ui.theme.AdMobInComposeTheme
import io.github.binishmanandhar23.admobincomposeexample.viewmodel.MainViewModel

const val BANNER_AD_UNIT = "ca-app-pub-3940256099942544/6300978111"

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bannerAdState by mainViewModel.bannerAdState.collectAsState()
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BannerAdsSection(bannerAdState = bannerAdState)
                    }
                }
            }
        }
    }

    @Composable
    private fun BannerAdsSection(bannerAdState: AdState){
        Text("Banner Ad", style = TextStyle(fontWeight = FontWeight.Bold))
        LoadingBannerAds()
        when {
            bannerAdState.isSuccess -> Text("BannerAd loaded successfully")
            bannerAdState.isError -> Text("BannerAd load failed: ${bannerAdState.error?.cause}")
        }
    }

    @Composable
    private fun LoadingBannerAds() {
        BannerAds(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), adUnit = BANNER_AD_UNIT,
            adSize = AdSize.LARGE_BANNER,
            adListener = object : AdListener(){
                override fun onAdLoaded() {
                    mainViewModel.updateBannerAdState(AdState(isSuccess = true))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mainViewModel.updateBannerAdState(AdState(isError = true, error = p0))
                }
            }
        )
    }
}

