package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnboardingCardExpended(
    modifier: Modifier = Modifier,
    cardData: EducationCard,
    onClick: (() -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .sharedBounds(
                    rememberSharedContentState(key = "card-${cardData.hashCode()}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .height(444.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6A4C93), // Purple
                            Color(0xFF4A90E2)  // Blue
                        )
                    )
                )

        ) {
            // Image with rounded corners
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
                    .height(340.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
//                placeholder = painterResource(id = R.drawable.placeholder), // Add your placeholder
//                error = painterResource(id = R.drawable.error_placeholder) // Add your error placeholder
            )

            // Text content at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
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

// Usage example
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun FinancialCardPreview() {
    Text("Preview not available for shared elements in isolation. See OnboardingScreen preview.")
}
