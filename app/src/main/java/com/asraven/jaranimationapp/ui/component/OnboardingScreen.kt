package com.asraven.jaranimationapp.ui.component

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asraven.jaranimationapp.MainActivityViewModel
import com.asraven.jaranimationapp.data.remote.EducationCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val cards = viewModel.uiState.collectAsStateWithLifecycle().value.educationItems
    var currentExpandedIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    SharedTransitionLayout {
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
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
                    // Sticky headers for previous cards
                    StickyHeaders2(
                        cards = cards,
                        currentExpandedIndex = targetIndex,
                        onCardClick = { index ->
                            currentExpandedIndex = index
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )

                    // Main content area - only shows the current expanded card
                    if (cards.isNotEmpty()) { // Ensure cards list is not empty
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OnboardingCardExpended(
                                cardData = cards[targetIndex],
                                onClick = { /* Handle click */ },
                                modifier = Modifier.fillMaxWidth(),
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        }
                    }
                }
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
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    if (currentExpandedIndex > 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
                )
                .zIndex(2f)
        ) {
            cards.take(currentExpandedIndex).forEachIndexed { index, card ->
                OnBoardingCardFolded(
                    cardData = card,
                    onClick = { onCardClick(index) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }

            // Subtle divider
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 1.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableCardsPreview() {
    MaterialTheme {
        OnboardingScreen(
            paddingValues = PaddingValues(0.dp)
        )
    }
}
