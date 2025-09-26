package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.asraven.jaranimationapp.data.remote.EducationCard
import com.asraven.jaranimationapp.utils.toComposeColorOrUnspecified

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnboardingCardExpended(
    modifier: Modifier = Modifier,
    cardData: EducationCard,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val gradientColors = listOf(
        cardData.startGradient.toComposeColorOrUnspecified(),
        cardData.endGradient.toComposeColorOrUnspecified()
    )

    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .sharedBounds(
                    rememberSharedContentState(key = "card-${cardData.hashCode()}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .wrapContentHeight()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            cardData.strokeStartColor.toComposeColorOrUnspecified(),
                            cardData.strokeEndColor.toComposeColorOrUnspecified()
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cardData.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image-${cardData.hashCode()}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = cardData.expandStateText ?: "",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = "title-${cardData.hashCode()}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun FinancialCardPreview() {
    Text("Preview not available for shared elements in isolation. See OnboardingScreen preview.")
}
