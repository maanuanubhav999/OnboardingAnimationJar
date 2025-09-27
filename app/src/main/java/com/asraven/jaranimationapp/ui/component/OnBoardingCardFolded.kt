package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.asraven.jaranimationapp.R
import com.asraven.jaranimationapp.data.remote.EducationCard
import com.asraven.jaranimationapp.utils.toComposeColorOrUnspecified

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnBoardingCardFolded(
    modifier: Modifier = Modifier,
    cardData: EducationCard,
    onClick: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val gradientColors = listOf(
        cardData.startGradient.toComposeColorOrUnspecified(),
        cardData.endGradient.toComposeColorOrUnspecified()
    )

    val borderColor = cardData.strokeStartColor.toComposeColorOrUnspecified()

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .sharedBounds(
                    rememberSharedContentState(key = "card-${cardData.hashCode()}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = gradientColors,
                    )
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(16.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FoldedCardImage(
                    imageUrl = cardData.image,
                    contentDescription = "Character avatar",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    imageKey = cardData.hashCode().toString()
                )

                FoldedCardTitle(
                    text = cardData.collapsedStateText ?: "",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = Color.White,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    titleKey = cardData.hashCode().toString()
                )

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_down_arrow),
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClick() }
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoldedCardImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageKey: String
) {
    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .size(34.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image-$imageKey"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoldedCardTitle(
    text: String,
    style: TextStyle,
    color: Color,
    maxLines: Int,
    textAlign: TextAlign,
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
            maxLines = maxLines,
            modifier = modifier
                .sharedElement(
                    rememberSharedContentState(key = "title-$titleKey"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            textAlign = textAlign
        )
    }
}



@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun GoldPurchaseCardPreview() {
    Text("Preview not available for shared elements in isolation. See OnboardingScreen preview.")
}
