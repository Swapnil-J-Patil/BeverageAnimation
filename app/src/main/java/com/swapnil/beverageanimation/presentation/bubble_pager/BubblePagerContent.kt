package com.swapnil.beverageanimation.presentation.bubble_pager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.swapnil.beverageanimation.R

@Composable
fun BubblePagerContent(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    val colors= listOf(
        Color(0xFFF69A9A),
        Color(0xFFFCC485),
        Color(0xFFAAA5FD)
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BubblePager(
            pagerState = pagerState,
            modifier = Modifier.fillMaxSize(),
            bubbleColors = colors,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                //Content to be changed
            }
        }
        MaskedImage(
            imageResId = R.drawable.can_image, // background label image
            maskResId = R.drawable.img_1,     // can-shaped alpha mask
            pagerState = pagerState
        )

    }
}