package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Matrix




@Composable
fun AnimatedSkewContainer(
    modifier: Modifier = Modifier,
    initialSkewX: Float = 0f,
    initialSkewY: Float = 0.3f,
    targetSkewX: Float = 0f,
    targetSkewY: Float = 0f,
    animationDurationMs: Int = 1000,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = animationDurationMs),
    autoStart: Boolean = true,
    content: @Composable () -> Unit
) {
    var animate by remember { mutableStateOf(!autoStart) }

    val skewXAnimation by animateFloatAsState(
        targetValue = if (animate) targetSkewX else initialSkewX,
        animationSpec = animationSpec,
        label = "skewXAnimation"
    )

    val skewYAnimation by animateFloatAsState(
        targetValue = if (animate) targetSkewY else initialSkewY,
        animationSpec = animationSpec,
        label = "skewYAnimation"
    )

    if (autoStart) {
        LaunchedEffect(Unit) {
            animate = true
        }
    }

    Box(
        modifier = modifier
            .drawWithContent {
                val matrix = Matrix()
                // Set both skew factors
                matrix.values[1] = skewYAnimation // Y-skew based on X
                matrix.values[3] = skewXAnimation // X-skew based on Y

                drawContext.canvas.save()
                drawContext.canvas.concat(matrix)
                drawContent()
                drawContext.canvas.restore()
            }
    ) {
        content()
    }
}