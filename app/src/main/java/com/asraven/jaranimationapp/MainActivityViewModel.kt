package com.asraven.jaranimationapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : ViewModel() {


    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(
        MainActivityUiState(      listOf(
            CardData(1, "Adventure Awaits", "Discover breathtaking landscapes and hidden gems around the world. From mountain peaks to ocean depths, every journey tells a story worth sharing.", "https://img.myjar.app/yXXRNLKGxWkoWRyyzurnZr3UJNHbqVaCHiLk1VYGlDM/rs:fit:0:0/q:60/format:webp/plain/https://cdn.myjar.app/Homefeed/engagement_card/buyGoldEducation2.webp"),
            CardData(2, "Culinary Delights", "Explore flavors from different cultures and master the art of cooking. Each recipe is a gateway to understanding traditions and creating memorable experiences.", "https://img.myjar.app/yXXRNLKGxWkoWRyyzurnZr3UJNHbqVaCHiLk1VYGlDM/rs:fit:0:0/q:60/format:webp/plain/https://cdn.myjar.app/Homefeed/engagement_card/buyGoldEducation2.webp"),
            CardData(3, "Tech Innovation", "Stay ahead with the latest technology trends and breakthroughs. Innovation shapes our future and transforms the way we live, work, and connect.", "https://img.myjar.app/yXXRNLKGxWkoWRyyzurnZr3UJNHbqVaCHiLk1VYGlDM/rs:fit:0:0/q:60/format:webp/plain/https://cdn.myjar.app/Homefeed/engagement_card/buyGoldEducation2.webp"),
            CardData(4, "Wellness Journey", "Embrace a healthier lifestyle with mindful practices and balanced living. Your well-being is the foundation for achieving all your dreams and aspirations.", "https://img.myjar.app/yXXRNLKGxWkoWRyyzurnZr3UJNHbqVaCHiLk1VYGlDM/rs:fit:0:0/q:60/format:webp/plain/https://cdn.myjar.app/Homefeed/engagement_card/buyGoldEducation2.webp"),
            CardData(5, "Creative Arts", "Express yourself through various forms of artistic creation. Art has the power to inspire, heal, and bring people together across all boundaries.", "https://img.myjar.app/fLqaeBj3sX4VtpjdRrj2_HxHPJ4YmqaFX9jPcm563cc/rs:fit:0:0/q:60/format:webp/plain/https://cdn.myjar.app/Homefeed/engagement_card/buyGoldEducation3.webp")
        )),
    )
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()


}



data class MainActivityUiState(
    val cards : List<CardData> = listOf()
)

data class CardData(
    val id: Int,
    val title: String,
    val description: String,
    val image: String
)