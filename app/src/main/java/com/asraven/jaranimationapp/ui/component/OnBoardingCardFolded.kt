package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.IconButton
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
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.asraven.jaranimationapp.data.remote.EducationCard

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
        Color(cardData.startGradient?.toColorInt() ?: 0xFF00000),
        Color(cardData.endGradient?.toColorInt() ?: 0xFF00000)
    )

    val borderColor = Color(0xFF4A9EFF) // Blue border

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
                        radius = 800f
                    )
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(cardData.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Character avatar",
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "image-${cardData.hashCode()}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = cardData.collapsedStateText ?: "",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "title-${cardData.hashCode()}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }

                IconButton(
                    onClick = { onClick() }
                ) {
//                    Icon(
////                        imageVector = Icons.Default.KeyboardArrowDown,
//                        imageVector = Icons.Filled.KeyboardArrowDown,
//                        contentDescription = "Expand",
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
                    Box {
                        Text("^")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun GoldPurchaseCardPreview() {
    // This Preview will not work correctly without a SharedTransitionLayout ancestor
    // and proper AnimatedVisibilityScope.
    /*
    SharedTransitionLayout {
        AnimatedContent(targetState = false, label = "") { targetState ->
            OnBoardingCardFolded(
                cardData = CardData("1", "https://example.com/some-image.jpg", "Buy gold anytime, anywhere", "Description"),
                onClick = { /* Handle click */ },
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedContent
            )
        }
    }
    */
    Text("Preview not available for shared elements in isolation. See OnboardingScreen preview.")
}
