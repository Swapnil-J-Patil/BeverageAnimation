package com.swapnil.beverageanimation.presentation.bubble_pager

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlin.math.absoluteValue
import kotlin.math.abs
import kotlin.math.sign

fun calculateBubbleDimensions(
    swipeProgress: Float,
    easing: Easing,
    minRadius: Dp,
    maxRadius: Dp
): Pair<Dp, Dp> {
    // swipe value ranges between 0 to 1.0 for half of the swipe
    // and 1.0 to 0 for the other half of the swipe
    val swipeValue = lerp(0f, 2f, swipeProgress.absoluteValue)
    val radius = lerp(
        minRadius,
        maxRadius,
        easing.transform(swipeValue)
    )
    var centerX = lerp(
        0.dp,
        maxRadius,
        easing.transform(swipeValue)
    )
    if (swipeProgress < 0) {
        centerX = -centerX
    }
    return Pair(radius, centerX)
}
@Composable
fun BubblePager(
    pagerState: androidx.compose.foundation.pager.PagerState,
    modifier: Modifier = Modifier,
    bubbleMinRadius: Dp = 40.dp,
    bubbleMaxRadius: Dp = 12000.dp,
    bubbleBottomPadding: Dp = 0.dp,
    bubbleColors: List<Color>,
    content: @Composable PagerScope.(Int) -> Unit,
) {
    val colors= listOf(
        Color(0xFFF69A9A),
        Color(0xFFFCC485),
        Color(0xFFAAA5FD)
    )
    Box(modifier = modifier) {
        HorizontalPager(
           // count = pageCount,
            state = pagerState,
            flingBehavior = bubblePagerFlingBehavior(pagerState),
            modifier = Modifier.drawBehind {
                val current = pagerState.currentPage
                val offset = pagerState.currentPageOffsetFraction
                val fromColor = bubbleColors.getOrElse(current) { Color.Gray }
                val toColor = bubbleColors.getOrElse(
                    (current + offset.sign.toInt()).coerceIn(bubbleColors.indices)
                ) { fromColor }

                val blendedColor = lerp(fromColor, toColor, abs(offset))

                drawRect(color = blendedColor, size = size)
                val (radius, centerX) = calculateBubbleDimensions(
                    swipeProgress = pagerState.currentPageOffsetFraction,
                    easing = CubicBezierEasing(1f, 0f, .92f, .62f),
                    minRadius = bubbleMinRadius,
                    maxRadius = bubbleMaxRadius
                )
                drawBubble(
                    radius = radius,
                    centerX = centerX,
                    bottomPadding = bubbleBottomPadding,
                    color = colors[pagerState.currentPage]
                )
            }
        ) { page ->
            content(page)
        }
    }
}