package com.swapnil.beverageanimation.presentation.bubble_pager

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun MaskedImage(
    imageResId: Int,
    maskResId: Int,
    pagerState: PagerState,
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
    val infiniteTransition = rememberInfiniteTransition()
    val hoverOffset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine ),
            repeatMode = RepeatMode.Reverse
        )
    )
    /*val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )*/
    val target = remember(pagerState.currentPage, pagerState.currentPageOffsetFraction) {
        val base = steps.getOrElse(pagerState.currentPage) { 0f }
        val next = steps.getOrElse((pagerState.currentPage + 1).coerceAtMost(steps.lastIndex)) { base }
        base + (next - base) * pagerState.currentPageOffsetFraction
    }

    val animatedStartPercent by remember {
        derivedStateOf {
            val base = steps.getOrElse(pagerState.currentPage) { 0f }
            val next = steps.getOrElse((pagerState.currentPage + 1).coerceAtMost(steps.lastIndex)) { base }
            base + (next - base) * pagerState.currentPageOffsetFraction
        }
    }

    /*LaunchedEffect(target) {
        animatedStartPercent.animateTo(
            targetValue = target,
            animationSpec = tween(durationMillis = 400, easing = LinearEasing)
        )
    }*/

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = modifier
            .graphicsLayer {
                translationY = hoverOffset
               /* scaleX = scale
                scaleY = scale*/
            }) {
            val paint = Paint()
            val canvasWidth = size.width
            val canvasHeight = size.height
            val maskWidth = maskBitmap.width
            val maskHeight = maskBitmap.height

            //to display image part by part
            val srcStartX = (maskWidth * animatedStartPercent).toInt()
            val srcEndX = (srcStartX + (maskWidth * 0.33f)).coerceAtMost(maskWidth.toFloat()).toInt()
            val srcWidth = srcEndX - srcStartX

            //To get the fitting size of the mask image
            val shrinkFactor = 0.98f
            val shrinkedWidth = (canvasWidth * shrinkFactor).toInt()
            val shrinkedHeight = (canvasHeight * shrinkFactor).toInt()
            val offsetX = ((canvasWidth - shrinkedWidth) / 2f).toInt()
            val offsetY = ((canvasHeight - shrinkedHeight) / 2f).toInt()

            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, canvasWidth, canvasHeight), paint)

                canvas.drawImageRect(
                    image = imageBitmap,
                    srcOffset = IntOffset.Zero,
                    srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                    dstOffset = IntOffset.Zero,
                    dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                    paint = paint
                )

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
