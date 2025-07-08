package com.swapnil.beverageanimation.presentation.bubble_pager

import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

@Composable
fun bubblePagerFlingBehavior(pagerState: PagerState) =
    PagerDefaults.flingBehavior(
        state = pagerState,
        snapAnimationSpec = spring(dampingRatio = 1.9f, stiffness = 600f),
    )

fun PagerState.getBubbleColor(bubbleColors: List<Color>): Color {
    val index = if (currentPageOffsetFraction < 0) {
        currentPage - 1
    } else nextSwipeablePageIndex
    return bubbleColors[index]
}

fun PagerState.getNextBubbleColor(bubbleColors: List<Color>): Color {
    return bubbleColors[nextSwipeablePageIndex]
}

fun PagerState.shouldHideBubble(isDragged: Boolean): Boolean = derivedStateOf {
    var b = false
    if (isDragged) {
        b = true
    }
    if (currentPageOffsetFraction.absoluteValue > 0.1) {
        b = true
    }
    b
}.value

val PagerState.nextSwipeablePageIndex: Int
    get() = if ((currentPage + 1) == pageCount) currentPage - 1 else currentPage + 1

fun lerp(start: Float, end: Float, value: Float): Float {
    return start + (end - start) * value
}