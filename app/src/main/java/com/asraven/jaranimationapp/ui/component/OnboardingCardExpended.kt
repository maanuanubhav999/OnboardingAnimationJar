package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.TextStyle
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
            ExpandedCardImage(
                imageUrl = cardData.image,
                contentDescription = null,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                imageKey = cardData.hashCode().toString()
            )

            Column(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                ExpandedCardTitle(
                    text = cardData.expandStateText ?: "",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    titleKey = cardData.hashCode().toString()
                )
            }
        }
    }
}



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExpandedCardImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageKey: String
) {
    with(sharedTransitionScope) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier
                .sharedElement(
                    rememberSharedContentState(key = "image-$imageKey"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExpandedCardTitle(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    titleKey: String
) {
    with(sharedTransitionScope) {
        Text(
            text = text,
            style = style,
            color = color,
            modifier = modifier.sharedElement(
                rememberSharedContentState(key = "title-$titleKey"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        )
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun FinancialCardPreview() {
    Text("Preview not available for shared elements in isolation. See OnboardingScreen preview.")
}
