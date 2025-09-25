package com.asraven.jaranimationapp.ui.component


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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asraven.jaranimationapp.CardData
import com.asraven.jaranimationapp.MainActivityViewModel


@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val cards = viewModel.uiState.collectAsStateWithLifecycle().value.cards
    var currentExpandedIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val threshold = 100f
                            when {
                                dragOffset > threshold && currentExpandedIndex > 0 -> {
                                    // Drag down - go to previous card
                                    currentExpandedIndex -= 1
                                }
                                dragOffset < -threshold && currentExpandedIndex < cards.size - 1 -> {
                                    // Drag up - go to next card
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
                currentExpandedIndex = currentExpandedIndex,
                onCardClick = { index ->
                    currentExpandedIndex = index
                }
            )

            // Main content area - only shows the current expanded card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                OnboardingCardExpended(
                    imageUrl = cards[currentExpandedIndex].image,
                    title = cards[currentExpandedIndex].title,
                    onClick = { /* Handle click */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StickyHeaders2(
    cards: List<CardData>,
    currentExpandedIndex: Int,
    onCardClick: (Int) -> Unit
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
                    imageUrl = card.image,
                    text = card.title,
                    onClick = { onCardClick(index) },
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