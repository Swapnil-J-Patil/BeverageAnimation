package com.swapnil.beverageanimation.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.swapnil.beverageanimation.R
import com.swapnil.beverageanimation.presentation.bubble_pager.BubblePagerContent
import com.swapnil.beverageanimation.presentation.bubble_pager.MaskedImage
import com.swapnil.beverageanimation.presentation.slide_anim.CanAnimation
import com.swapnil.beverageanimation.presentation.ui.theme.BeverageAnimationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeverageAnimationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    CanAnimation(
                        imageResId = R.drawable.can_image, // background label image
                        maskResId = R.drawable.img_1,     // can-shaped alpha mask
                    )
                }

            }
        }
    }
}

