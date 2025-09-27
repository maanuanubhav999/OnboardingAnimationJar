package com.asraven.jaranimationapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


@Composable
internal fun RadialGradientBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xAAffdbf6),    // Adjusted opacity for ffdbf6 at gradient center
                        0.5f to Color(0x00ffdbf6),    // Transparent at 50% of radius
                        1.0f to Color(0x00ffdbf6)     // Ensure fully transparent beyond 0.5f
                    ),
                    center = Offset(size.width / 2f, size.height), // Bottom-center of this Box
                    radius = size.height * 0.75f // Gradient extends 75% of Box's height
                )
                drawRect(brush = brush)
            }
    )
}