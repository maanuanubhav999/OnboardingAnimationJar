package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asraven.jaranimationapp.MainActivityViewModel
import com.asraven.jaranimationapp.data.remote.EducationCard
import com.asraven.jaranimationapp.utils.toComposeColorOrUnspecified

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    viewModel: MainActivityViewModel = hiltViewModel(),
    onNavigateToLanding: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cards = uiState.educationItems
    val introData = uiState.intro // Assuming introData is of a type that has .saveButtonCta and .title

    var currentExpandedIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var showIntro by remember(introData) { mutableStateOf(introData?.title != null) } // Show intro if title exists

    val shouldShowCta by remember(currentExpandedIndex) {
        derivedStateOf {
            val isLastCard = cards.isNotEmpty() && currentExpandedIndex == cards.size - 1
            val ctaAvailable = uiState.saveButtonCta != null
            isLastCard && ctaAvailable
        }
    }

    SharedTransitionLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cards.getOrNull(currentExpandedIndex)?.backGroundColor.toComposeColorOrUnspecified())
                .padding(paddingValues)

        ) {
            if (showIntro && introData != null) {
                IntroDisplay(
                    introData = introData,
                    onDismiss = { showIntro = false }
                )
            } else {
                RadialGradientBox()

                AnimatedContent(
                    targetState = currentExpandedIndex,
                    label = "onboardingCardTransition",
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    }
                ) { targetIndex ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        val threshold = 100f
                                        when {
                                            dragOffset > threshold && currentExpandedIndex > 0 -> {
                                                currentExpandedIndex -= 1
                                            }

                                            dragOffset < -threshold && currentExpandedIndex < cards.size - 1 -> {
                                                currentExpandedIndex += 1
                                            }
                                        }
                                        dragOffset = 0f
                                    }
                                ) { _, dragAmount ->
                                    dragOffset += dragAmount.y
                                }
                            }
                    ) {
                        StickyHeaders2(
                            cards = cards,
                            currentExpandedIndex = targetIndex,
                            onCardClick = { index ->
                                currentExpandedIndex = index
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                        )

                        if (cards.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                OnboardingCardExpended(
                                    cardData = cards[targetIndex],
                                    modifier = Modifier.fillMaxWidth(),
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                )
                            }
                        }
                    }
                }
            }

            if (shouldShowCta){
                FloatingButton(
                    text = uiState.saveButtonCta?.text ?: "",
                    onClick = { onNavigateToLanding() },
                    backGroundColor = uiState.saveButtonCta?.backgroundColor.toComposeColorOrUnspecified(),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
                    textColor = uiState.saveButtonCta?.textColor.toComposeColorOrUnspecified()
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StickyHeaders2(
    cards: List<EducationCard>,
    currentExpandedIndex: Int,
    onCardClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    if (currentExpandedIndex > 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            cards.take(currentExpandedIndex).forEachIndexed { index, card ->
                OnBoardingCardFolded(
                    cardData = card,
                    onClick = { onCardClick(index) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableCardsPreview() {
    MaterialTheme {
        OnboardingScreen(
            paddingValues = PaddingValues(0.dp),
            onNavigateToLanding = {} // Added for preview
        )
    }
}
