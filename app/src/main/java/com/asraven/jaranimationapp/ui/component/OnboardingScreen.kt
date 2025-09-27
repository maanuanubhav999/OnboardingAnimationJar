package com.asraven.jaranimationapp.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
    val introData = uiState.intro
    val saveButtonCta = uiState.saveButtonCta

    val animationEnter = remember(uiState.bottomToCenterInterval) {
        uiState.bottomToCenterInterval ?: 1500
    }

    val animationExit = remember(uiState.collapseExpandIntroInterval) {
        uiState.collapseExpandIntroInterval ?: 500
    }

    var currentExpandedIndex by remember { mutableIntStateOf(0) }
    var showIntro by rememberSaveable(introData) { mutableStateOf(introData?.title != null) }

    val shouldShowCta by remember(currentExpandedIndex, cards.size, saveButtonCta) {
        derivedStateOf {
            val isLastCard = cards.isNotEmpty() && currentExpandedIndex == cards.size - 1
            val ctaAvailable = saveButtonCta != null
            isLastCard && ctaAvailable
        }
    }

    val rememberedOnNavigateToLanding = rememberUpdatedLambda(onNavigateToLanding)
    val rememberedOnCardClick = remember<(Int) -> Unit> { { index -> currentExpandedIndex = index } }
    val rememberedOnIndexChangeByDrag = remember<(Int) -> Unit> { { newIndex -> currentExpandedIndex = newIndex } }

    when(uiState.isLoadingEducationData && cards.isEmpty()) {
        true -> {
            Box(Modifier.fillMaxSize()) {
                LoadingScreen()
            }
        }

        false -> {
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
                                slideInVertically(animationSpec = tween(animationEnter)) { fullHeight -> fullHeight } + fadeIn(
                                    animationSpec = tween(300)
                                ) togetherWith
                                        slideOutVertically(animationSpec = tween(animationExit)) { fullHeight -> fullHeight } + fadeOut(
                                    animationSpec = tween(300)
                                )
                            }
                        ) { targetIndex ->
                            DraggableCardsContent(
                                cards = cards,
                                currentExpandedIndex = targetIndex,
                                onCardClick = rememberedOnCardClick,
                                onIndexChangeByDrag = rememberedOnIndexChangeByDrag,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        }
                    }

                    if (shouldShowCta && saveButtonCta != null) {
                        FloatingButton(
                            text = saveButtonCta.text ?: "",
                            onClick = rememberedOnNavigateToLanding,
                            backGroundColor = saveButtonCta.backgroundColor.toComposeColorOrUnspecified(),
                            modifier = Modifier.align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp),
                            textColor = saveButtonCta.textColor.toComposeColorOrUnspecified()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DraggableCardsContent(
    cards: List<EducationCard>,
    currentExpandedIndex: Int,
    onCardClick: (Int) -> Unit,
    onIndexChangeByDrag: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(cards.size) { // Key on cards.size or a stable key if cards can change
                detectDragGestures(
                    onDragEnd = {
                        val threshold = 100f
                        val newIndex = when {
                            dragOffset > threshold && currentExpandedIndex > 0 -> currentExpandedIndex - 1
                            dragOffset < -threshold && currentExpandedIndex < cards.size - 1 -> currentExpandedIndex + 1
                            else -> currentExpandedIndex
                        }
                        if (newIndex != currentExpandedIndex) {
                            onIndexChangeByDrag(newIndex)
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
            currentExpandedIndex = currentExpandedIndex,
            onCardClick = onCardClick,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )

        if (cards.isNotEmpty() && currentExpandedIndex < cards.size) { // Ensure index is valid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                OnboardingCardExpended(
                    isSkewed  = currentExpandedIndex != 0,
                    cardData = cards[currentExpandedIndex],
                    modifier = Modifier.fillMaxWidth(),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
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
                val rememberedOnCardClickedForItem = remember(card, index, onCardClick) { { onCardClick(index) } }
                OnBoardingCardFolded(
                    cardData = card,
                    onClick = rememberedOnCardClickedForItem,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@Composable
fun <T> rememberUpdatedLambda(value: T): T {
    val valueRef = remember { mutableStateOf(value) }
    valueRef.value = value
    return valueRef.value
}
