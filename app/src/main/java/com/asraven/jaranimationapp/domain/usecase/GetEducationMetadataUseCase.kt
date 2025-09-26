package com.asraven.jaranimationapp.domain.usecase

import com.asraven.jaranimationapp.data.remote.OnboardingApiResponse
import com.asraven.jaranimationapp.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEducationMetadataUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    /**
     * Fetches the education metadata.
     */
    suspend operator fun invoke(): Flow<Result<OnboardingApiResponse>> {
        return onboardingRepository.getEducationMetadata()
    }
}