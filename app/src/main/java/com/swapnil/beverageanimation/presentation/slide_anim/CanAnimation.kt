package com.swapnil.beverageanimation.presentation.slide_anim

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.swapnil.beverageanimation.R
import com.swapnil.beverageanimation.presentation.ui.theme.Poppins
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CanAnimation(
    imageResId: Int,
    maskResId: Int,
    modifier: Modifier = Modifier.size(210.dp, 350.dp)
) {
    val context = LocalContext.current

    val imageBitmap = remember(imageResId) {
        ImageBitmap.imageResource(context.resources, imageResId)
    }

    val maskBitmap = remember(maskResId) {
        ImageBitmap.imageResource(context.resources, maskResId)
    }

    val steps = listOf(0f, 0.33f, 0.66f)
    var index by remember { mutableStateOf(0) }
    val animatedStartPercent = remember { Animatable(steps[index]) }
    val colors = listOf(
        Color(0xFFEB7D7C),
        Color(0xFFFCBD77),
        Color(0xFF8C85FD)
    )
    val animatedBgColor by animateColorAsState(
        targetValue = colors[index],
        animationSpec = tween(durationMillis = 800, easing = EaseInOutSine)
    )

    // Accumulate drag distance across the gesture
    var totalDrag by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    val hoverOffset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    val imageList = listOf(
        listOf(
            R.drawable.melon_middle_img,
            R.drawable.watermelon,
            R.drawable.watermelon,
            R.drawable.watermelon,
        ),
        listOf(
            R.drawable.peach_middle_img,
            R.drawable.peach,
            R.drawable.peach,
            R.drawable.peach,
        ),
        listOf(
            R.drawable.exotic_middle_img,
            R.drawable.peach,
            R.drawable.exotic_fruit1,
            R.drawable.exotic_fruit2,

            )
    )

    val textList = listOf(
        listOf(
            "Summer Splash",
            "A juicy burst of sweet watermelon fizz that keeps you refreshed all day. Perfect for hot afternoons and cool vibes."
        ),
        listOf(
            "Peachy Pop",
            "Crisp, smooth, and lightly sweet — this peach soda brings sunshine to every sip with a mellow, fruity finish."
        ),
        listOf(
            "Tropical Zing",
            "A bold fusion of tropical flavors with a tangy twist. Every can pops with a mysterious fruit explosion you’ll crave again and again."
        )
    )
    val hover1 by rememberHoverOffset(-15f, 15f)
    val hover2 by rememberHoverOffset(-30f, 20f)
    val hover3 by rememberHoverOffset(-10f, 10f)
    val hover4 by rememberHoverOffset(-25f, 25f)

    val scope = rememberCoroutineScope()

    var animateEntry by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animateEntry = true
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTab =configuration.screenWidthDp >= 600 && configuration.screenHeightDp >= 600
    when {
        isTab && isPortrait -> {
        //Tab and Vertical
        }
        isTab && !isPortrait -> {
        //Tab and horizontal
        }
        !isTab && isPortrait -> {
        //Phone and vertical
            Box(
                Modifier
                    .fillMaxSize()
                    .background(animatedBgColor)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val threshold = 80f // minimum drag distance to trigger switch

                                val nextIndex = when {
                                    totalDrag < -threshold && index < steps.lastIndex -> index + 1
                                    totalDrag > threshold && index > 0 -> index - 1
                                    else -> index
                                }

                                if (nextIndex != index) {
                                    index = nextIndex
                                    scope.launch {
                                        animatedStartPercent.animateTo(
                                            targetValue = steps[index],
                                            animationSpec = tween(800, easing = LinearEasing)
                                        )
                                    }
                                }

                                // Reset drag
                                totalDrag = 0f
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                totalDrag += dragAmount
                                change.consume()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = index to animateEntry,
                    transitionSpec = {
                        (slideInVertically(
                            initialOffsetY = { -it }, // New images come from top
                            animationSpec = tween(800)
                        ) + fadeIn()) togetherWith
                                (slideOutVertically(
                                    targetOffsetY = { -it }, // Old images go to top
                                    animationSpec = tween(800)
                                ) + fadeOut())
                    },
                    contentAlignment = Alignment.Center,
                    label = "ImageTransition"
                ) { (targetIndex, hasAnimated) ->

                    // Image Group based on targetIndex
                    Box(modifier = Modifier.fillMaxSize()) {

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][0],
                            modifier = Modifier
                                .padding(top = 100.dp)
                                .fillMaxWidth()
                                .height(150.dp)
                                .graphicsLayer { translationY = hover1 },
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillBounds
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][1],
                            modifier = Modifier
                                .padding(start = 50.dp, bottom = 250.dp)
                                .size(200.dp)
                                .graphicsLayer { translationY = hover2 }
                                .rotate(-30f),
                            alignment = Alignment.CenterStart
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][2],
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(150.dp)
                                .graphicsLayer { translationY = hover3 },
                            alignment = Alignment.TopStart
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][3],
                            modifier = Modifier
                                .padding(top = 100.dp)
                                .size(150.dp)
                                .graphicsLayer { translationY = hover4 }
                                .rotate(-30f),
                            alignment = Alignment.TopEnd
                        )
                    }
                }
                AnimatedContent(
                    targetState = index to animateEntry,
                    transitionSpec = {
                        (slideInVertically(
                            initialOffsetY = { it }, // New images come from top
                            animationSpec = tween(800)
                        ) + fadeIn()) togetherWith
                                (slideOutVertically(
                                    targetOffsetY = { it }, // Old images go to top
                                    animationSpec = tween(800)
                                ) + fadeOut())
                    },
                    contentAlignment = Alignment.Center,
                    label = "TextTransition"
                ) { (targetIndex, hasAnimated) ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedContentFromTop(
                            visibility = hasAnimated,
                            modifier = Modifier,
                            title = textList[targetIndex][0],
                            content = textList[targetIndex][1],
                            alignment = Alignment.BottomStart,
                        )
                    }
                }

                Canvas(
                    modifier = modifier
                        .graphicsLayer {
                            translationY = hoverOffset
                        })
                {
                    val paint = Paint()
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val maskWidth = maskBitmap.width
                    val maskHeight = maskBitmap.height

                    // Animate visible 33% window of mask
                    val srcStartX = (maskWidth * animatedStartPercent.value).toInt()
                    val srcEndX =
                        (srcStartX + (maskWidth * 0.33f)).coerceAtMost(maskWidth.toFloat()).toInt()
                    val srcWidth = srcEndX - srcStartX

                    // Slightly shrink and center mask image
                    val shrinkFactor = 0.98f
                    val shrinkedWidth = (canvasWidth * shrinkFactor).toInt()
                    val shrinkedHeight = (canvasHeight * shrinkFactor).toInt()
                    val offsetX = ((canvasWidth - shrinkedWidth) / 2f).toInt()
                    val offsetY = ((canvasHeight - shrinkedHeight) / 2f).toInt()

                    drawIntoCanvas { canvas ->
                        canvas.saveLayer(Rect(0f, 0f, canvasWidth, canvasHeight), paint)

                        // Draw base image stretched to full canvas
                        canvas.drawImageRect(
                            image = imageBitmap,
                            srcOffset = IntOffset.Zero,
                            srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                            dstOffset = IntOffset.Zero,
                            dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                            paint = paint
                        )
                        // Apply the mask using BlendMode.DstIn
                        paint.blendMode = BlendMode.Modulate

                        canvas.drawImageRect(
                            image = maskBitmap,
                            srcOffset = IntOffset(srcStartX, 0),
                            srcSize = IntSize(srcWidth, maskHeight),
                            dstOffset = IntOffset(offsetX, offsetY),
                            dstSize = IntSize(shrinkedWidth, shrinkedHeight),
                            paint = paint
                        )


                        canvas.restore()
                    }
                }
            }
        }
        else -> {
        //Phone and horizontal
            Box(
                Modifier
                    .fillMaxSize()
                    .background(animatedBgColor)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val threshold = 80f // minimum drag distance to trigger switch

                                val nextIndex = when {
                                    totalDrag < -threshold && index < steps.lastIndex -> index + 1
                                    totalDrag > threshold && index > 0 -> index - 1
                                    else -> index
                                }

                                if (nextIndex != index) {
                                    index = nextIndex
                                    scope.launch {
                                        animatedStartPercent.animateTo(
                                            targetValue = steps[index],
                                            animationSpec = tween(800, easing = LinearEasing)
                                        )
                                    }
                                }

                                // Reset drag
                                totalDrag = 0f
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                totalDrag += dragAmount
                                change.consume()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = index to animateEntry,
                    transitionSpec = {
                        (slideInVertically(
                            initialOffsetY = { -it }, // New images come from top
                            animationSpec = tween(800)
                        ) + fadeIn()) togetherWith
                                (slideOutVertically(
                                    targetOffsetY = { -it }, // Old images go to top
                                    animationSpec = tween(800)
                                ) + fadeOut())
                    },
                    contentAlignment = Alignment.Center,
                    label = "ImageTransition"
                ) { (targetIndex, hasAnimated) ->

                    // Image Group based on targetIndex
                    Box(modifier = Modifier.fillMaxSize()) {

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][0],
                            modifier = Modifier
                                .padding(top = 100.dp)
                                .fillMaxWidth(0.3f)
                                .height(150.dp)
                                .graphicsLayer { translationY = hover1 },
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillBounds
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][1],
                            modifier = Modifier
                                .padding(start = 50.dp, bottom = 250.dp)
                                .size(200.dp)
                                .graphicsLayer { translationY = hover2 }
                                .rotate(-30f),
                            alignment = Alignment.CenterStart
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][2],
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(150.dp)
                                .graphicsLayer { translationY = hover3 },
                            alignment = Alignment.TopStart
                        )

                        AnimatedImageFromTop(
                            imageResId = imageList[targetIndex][3],
                            modifier = Modifier
                                .padding(top = 100.dp)
                                .size(150.dp)
                                .graphicsLayer { translationY = hover4 }
                                .rotate(-30f),
                            alignment = Alignment.TopEnd
                        )
                    }
                }
                AnimatedContent(
                    targetState = index to animateEntry,
                    transitionSpec = {
                        (slideInVertically(
                            initialOffsetY = { it }, // New images come from top
                            animationSpec = tween(800)
                        ) + fadeIn()) togetherWith
                                (slideOutVertically(
                                    targetOffsetY = { it }, // Old images go to top
                                    animationSpec = tween(800)
                                ) + fadeOut())
                    },
                    contentAlignment = Alignment.Center,
                    label = "TextTransition"
                ) { (targetIndex, hasAnimated) ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedContentFromTop(
                            visibility = hasAnimated,
                            modifier = Modifier,
                            title = textList[targetIndex][0],
                            content = textList[targetIndex][1],
                            alignment = Alignment.BottomStart,
                        )
                    }
                }

                Canvas(
                    modifier = modifier
                        .graphicsLayer {
                            translationY = hoverOffset
                        })
                {
                    val paint = Paint()
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val maskWidth = maskBitmap.width
                    val maskHeight = maskBitmap.height

                    // Animate visible 33% window of mask
                    val srcStartX = (maskWidth * animatedStartPercent.value).toInt()
                    val srcEndX =
                        (srcStartX + (maskWidth * 0.33f)).coerceAtMost(maskWidth.toFloat()).toInt()
                    val srcWidth = srcEndX - srcStartX

                    // Slightly shrink and center mask image
                    val shrinkFactor = 0.98f
                    val shrinkedWidth = (canvasWidth * shrinkFactor).toInt()
                    val shrinkedHeight = (canvasHeight * shrinkFactor).toInt()
                    val offsetX = ((canvasWidth - shrinkedWidth) / 2f).toInt()
                    val offsetY = ((canvasHeight - shrinkedHeight) / 2f).toInt()

                    drawIntoCanvas { canvas ->
                        canvas.saveLayer(Rect(0f, 0f, canvasWidth, canvasHeight), paint)

                        // Draw base image stretched to full canvas
                        canvas.drawImageRect(
                            image = imageBitmap,
                            srcOffset = IntOffset.Zero,
                            srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                            dstOffset = IntOffset.Zero,
                            dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                            paint = paint
                        )
                        // Apply the mask using BlendMode.DstIn
                        paint.blendMode = BlendMode.Modulate

                        canvas.drawImageRect(
                            image = maskBitmap,
                            srcOffset = IntOffset(srcStartX, 0),
                            srcSize = IntSize(srcWidth, maskHeight),
                            dstOffset = IntOffset(offsetX, offsetY),
                            dstSize = IntSize(shrinkedWidth, shrinkedHeight),
                            paint = paint
                        )


                        canvas.restore()
                    }
                }
            }
        }
    }

}



@Composable
fun rememberHoverOffset(
    initial: Float,
    target: Float,
    duration: Int = 1200
): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "hover")
    return infiniteTransition.animateFloat(
        initialValue = initial,
        targetValue = target,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "hoverOffset"
    )
}

@Composable
fun AnimatedContentFromTop(
    visibility: Boolean,
    modifier: Modifier,
    title: String,
    content: String,
    alignment: Alignment,
) {
    var animateDivider by remember { mutableStateOf(false) }

    // Trigger divider animation when visibility becomes true
    LaunchedEffect(visibility) {
        if (visibility) {
            animateDivider = true
        } else {
            animateDivider = false
        }
    }

    val progress by animateFloatAsState(
        targetValue = if (animateDivider) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = EaseInOutSine),
        label = "DividerProgress"
    )

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = alignment
    )
    {
        Column(
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = Poppins,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .padding(start = 10.dp, top = 8.dp, end = 10.dp),
                thickness = 3.dp,
                color = Color.White

            )
            Text(
                text = content,
                color = Color.White,
                fontFamily = Poppins,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 15.dp)
            )
        }
    }
}


@Composable
fun AnimatedImageFromTop(
    imageResId: Int,
    modifier: Modifier,
    alignment: Alignment,
    contentScale: ContentScale = ContentScale.Fit
) {

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = alignment
    )
    {

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )

    }
}