package com.asraven.jaranimationapp.data.remote

data class AnimationResponse(
    val `data`: Data?,
    val success: Boolean?
)

data class Data(
    val manualBuyEducationData: ManualBuyEducationData?
)

data class ManualBuyEducationData(
    val actionText: String?,
    val bottomToCenterTranslationInterval: Int?,
    val cohort: String?,
    val collapseCardTiltInterval: Int?,
    val collapseExpandIntroInterval: Int?,
    val combination: Any?,
    val ctaLottie: String?,
    val educationCardList: List<EducationCard>?,
    val expandCardStayInterval: Int?,
    val introSubtitle: String?,
    val introSubtitleIcon: String?,
    val introTitle: String?,
    val saveButtonCta: SaveButtonCta?,
    val screenType: String?,
    val seenCount: Any?,
    val shouldShowBeforeNavigating: Boolean?,
    val shouldShowOnLandingPage: Boolean?,
    val toolBarIcon: String?,
    val toolBarText: String?
)

data class EducationCard(
    val backGroundColor: String?,
    val collapsedStateText: String?,
    val endGradient: String?,
    val expandStateText: String?,
    val image: String?,
    val startGradient: String?,
    val strokeEndColor: String?,
    val strokeStartColor: String?
)

data class SaveButtonCta(
    val backgroundColor: String?,
    val deeplink: Any?,
    val icon: Any?,
    val order: Any?,
    val strokeColor: String?,
    val text: String?,
    val textColor: String?
)