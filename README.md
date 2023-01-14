# AdMobInCompose

AdMob components converted to be used with compose

![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)

Easily add Ads for Android using the power of compose.

# Installation

Add the following dependencies in the gradle file of your app module to get started:

Gradle

```groovy
/**Main library**/
implementation 'io.github.binishmanandhar23.admobincompose:admobincompose:1.1.0'

/**Other necessary libraries**/
//Compose
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
implementation "androidx.activity:activity-compose:$activity_compose_version"
implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_runtime_version"
implementation "androidx.compose.material:material:$compose_version"
//-------------------------//

//Google AdMob
implementation "com.google.android.gms:play-services-ads:$ads_version"
/*****************************/
```

Maven

```xml

<dependency>
    <groupId>io.github.binishmanandhar23.admobincompose</groupId>
    <artifactId>admobincompose</artifactId>
    <version>1.1.0</version>
    <type>aar</type>
</dependency>
```

or if you want to further customize the module, simply import it.

### Note

If there are any confusions just clone github repository for proper use cases & to get the example
app shown in the gifs below.

## Initialization:

It is important that you follow the
AdMob's [Get Started documentation](https://developers.google.com/admob/android/quick-start) for
proper configuration of Mobile Ads SDK On further note, To keep libraries' versions consistent
with the main application you'll need to add versions in the **build.gradle** [**Project level**]

```groovy
buildscript {
    ext {
        compose_version = '1.3.1'
        ads_version = '21.1.0'
        activity_compose_version = '1.5.1'
        lifecycle_runtime_version = '2.5.1'
    }
    //...Othercodes
}
```

# Implementation

## BannerAds

```kotlin
BannerAds(
    modifier = Modifier,
    adUnit = "ca-app-pub-3940256099942544/6300978111",
    adSize = AdSize.BANNER,
    adListener = object : AdListener() {
        override fun onAdLoaded() {
            //Success
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            //Failure
        }
    }
)
```
**Image**
![](https://i.imgur.com/6wbX6Hs.jpg)

## Interstitial Ads

```kotlin
val rememberInterstitialAdState =
    rememberInterstitialAdsState(
        adUnit = "ca-app-pub-3940256099942544/1033173712",
        onAdLoaded = {
            //Ad loaded
        }, onAdLoadFailed = { loadAdError ->
            //Error loading
        }, fullScreenContentCallback = object : FullScreenContentCallback() {
            //Add callbacks
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {

            }
        })
//.....Other codes
rememberInterstitialAdState?.show() // For showing the interstitial ad
```

Calling `show()` is enough for showing the full interstitial ad.
**NOTE:** Google limits the number of Interstitial ads that can be shown.

**Image**

![](https://i.imgur.com/jiJ3Pea.gif)

## Native Ads

```kotlin
val nativeAdOptions = NativeAdOptions.Builder()
    .setVideoOptions(
        VideoOptions.Builder()
            .setStartMuted(true).setClickToExpandRequested(true)
            .build()
    ).setRequestMultipleImages(true)
    .build()
val rememberCustomNativeAdState = rememberCustomNativeAdState(
    adUnit = "ca-app-pub-3940256099942544/2247696110" /*For video ads we need to setup test device configuration*/,
    nativeAdOptions = nativeAdOptions /*Optional*/,
    adListener = object : AdListener() {
        override fun onAdLoaded() {
            //Ad has been loaded successfully
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            //Failed to load ads
        }
    }
)
val nativeAd by rememberCustomNativeAdState.nativeAd.observeAsState() //Getting NativeAd object using observe
if (nativeAd != null)
    NativeAdViewCompose(nativeAd = nativeAd) { nativeAdView ->
        /**VERY IMPORTANT**/
        nativeAdView.setNativeAd(nativeAd)
        //Add your compose codes
    }
```

### Inner components useful for building NativeAds

```kotlin
//For Icon
NativeAdView(getView = {
    nativeAdView.iconView = it
}) {
    /**For images use NativeAdImage**/
    NativeAdImage(
        drawable = nativeAd.icon?.drawable,
        contentDescription = "Icon",
        modifier = Modifier.fillMaxWidth()
    )
}
//For Headline
NativeAdView(getView = {
    nativeAdView.headlineView = it
}) {
    //Add your view code in compose
}
//......Similarly add codes for body, ratings, price, etc.

//For MediaView
NativeAdMediaView(
    modifier = Modifier,
    nativeAdView = nativeAdView,
    mediaContent = nativeAd.mediaContent,
    scaleType = ImageView.ScaleType.FIT_CENTER
)
```
**Image**
![](https://i.imgur.com/87MWyoH.jpg)


## Reward Ads

```kotlin
val rememberCustomRewardAdState =
    rememberCustomRewardAd(adUnit = "ca-app-pub-3940256099942544/5224354917", onAdFailedToLoad = {
        //Ad failed to load
    }, onAdLoaded = {
        //Ad loaded successfully
    }, fullScreenContentCallback = object : FullScreenContentCallback() {
        //Add other full screen callbacks
        override fun onAdFailedToShowFullScreenContent(p0: AdError) {

        }
    })

//Add a user action to show Reward ads like on button click
val coroutineScope = rememberCoroutineScope()
Button(
    onClick = {
        /**First method using coroutine**/
        coroutineScope.launch {
            val rewardItem = rememberCustomRewardAdState.showAsync()
            Log.i(
                "RewardItem",
                "Amount: ${rewardItem.amount} Type: ${rewardItem.type}"
            )
        }
        //OR,
        /**Second method using callbacks**/
        rememberCustomRewardAdState.show(object : OnUserEarnedRewardListener {
            override fun onUserEarnedReward(p0: RewardItem) {

            }
        })
    }) {
    //Design button
}
```
***Image***

![](https://i.imgur.com/P0YSZKn.gif)

## Contributions

If you want to contribute or just wanna say Hi!, you can find me at:

1. [LinkedIn](https://www.linkedin.com/in/binish-manandhar-3136621b2/)
2. [Facebook](https://www.facebook.com/binish.manandhar)
3. [Twitter](https://twitter.com/NotBinish)

