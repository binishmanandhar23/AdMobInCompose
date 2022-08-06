package io.github.binishmanandhar23.admobincompose.components.native

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    nativeAd: NativeAd?,
    content: @Composable (nativeAdView: NativeAdView) -> Unit
) {
    if (nativeAd != null)
        AndroidView(modifier = modifier, factory = {
            NativeAdView(it).apply {
                nativeAd.run {
                    setNativeAd(this)
                }
            }
        }, update = {
            val composeView = ComposeView(it.context)
            it.removeAllViews()
            it.addView(composeView)
            composeView.setContent {
                content(it)
            }
        })
}


@Composable
fun NativeAdView(
    modifier: Modifier = Modifier, getView: (ComposeView) -> Unit, content: @Composable () -> Unit
) = AndroidView(modifier = modifier, factory = { ComposeView(it) }, update = {
    it.setContent {
        content()
    }
    getView(it)
})

@Deprecated(
    "Not recommended since loading URI from Async throws an IllegalArgumentException",
    replaceWith = ReplaceWith("NativeAdImage(drawable: Drawable?)")
)
@Composable
fun NativeAdImage(
    modifier: Modifier = Modifier,
    uri: Uri?,
    placeholder: Drawable?,
    contentDescription: String,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) = AsyncImage(
    model = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).placeholder(placeholder)
            .data(uri)
            .crossfade(true)
            .build(),
        transform = transform,
        onState = onState,
    ), contentDescription = contentDescription, contentScale = contentScale, modifier = modifier,
    colorFilter = colorFilter,
    alignment = alignment,
    alpha = alpha
)

@Composable
fun NativeAdImage(
    modifier: Modifier = Modifier,
    drawable: Drawable?,
    contentDescription: String,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,

    ) = Image(
    painter = rememberDrawablePainter(drawable = drawable),
    contentDescription = contentDescription,
    contentScale = contentScale,
    modifier = modifier,
    colorFilter = colorFilter,
    alpha = alpha,
    alignment = alignment,
)

@Composable
fun NativeAdMediaView(modifier: Modifier= Modifier, setup: (MediaView) -> Unit) = AndroidView(modifier = modifier, factory = { MediaView(it) }, update = {
    setup(it)
})

@Deprecated("Use 'NativeAdView' for better customization")
@Composable
fun NativeAdText(
    modifier: Modifier = Modifier, text: String, textStyle: TextStyle = TextStyle(
        color = Color.Black
    ), getView: (TextView) -> Unit
) =
    AndroidView(modifier = modifier, factory = { TextView(it) }, update = {
        it.textAlignment = when (textStyle.textAlign) {
            TextAlign.End -> TextView.TEXT_ALIGNMENT_TEXT_END
            TextAlign.Center -> TextView.TEXT_ALIGNMENT_CENTER
            else -> TextView.TEXT_ALIGNMENT_TEXT_START
        }
        it.setTextSize(TypedValue.COMPLEX_UNIT_SP, textStyle.fontSize.value)
        it.typeface =
            if (textStyle.fontWeight == FontWeight.Bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        it.setTextColor(
            android.graphics.Color.argb(
                textStyle.color.toArgb().alpha,
                textStyle.color.toArgb().red,
                textStyle.color.toArgb().green,
                textStyle.color.toArgb().blue,
            )
        )
        it.text = text
        getView(it)
    })

@Deprecated("Use 'NativeAdView' for better customization")
@Composable
fun NativeAdImageView(
    modifier: Modifier = Modifier,
    uri: Uri?,
    contentScale: ContentScale = ContentScale.None,
    getView: (ImageView) -> Unit
) =
    AndroidView(modifier = modifier, factory = { ImageView(it) }, update = {
        it.scaleType = when (contentScale) {
            ContentScale.Crop -> ImageView.ScaleType.CENTER_CROP
            ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
            else -> ImageView.ScaleType.CENTER
        }
        it.setImageURI(uri)
        getView(it)
    })

@Deprecated("Use 'NativeAdView' for better customization")
@Composable
fun NativeAdImageView(
    modifier: Modifier = Modifier,
    drawable: Drawable?,
    contentScale: ContentScale = ContentScale.None,
    getView: (ImageView) -> Unit
) =
    AndroidView(modifier = modifier, factory = { ImageView(it) }, update = {
        it.scaleType = when (contentScale) {
            ContentScale.Crop -> ImageView.ScaleType.CENTER_CROP
            ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
            else -> ImageView.ScaleType.CENTER
        }
        it.setImageDrawable(drawable)
        getView(it)
    })



