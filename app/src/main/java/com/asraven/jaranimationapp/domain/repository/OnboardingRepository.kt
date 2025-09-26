package com.asraven.jaranimationapp.domain.repository

import com.asraven.jaranimationapp.data.remote.OnboardingApiResponse
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun getEducationMetadata(): Flow<Result<OnboardingApiResponse>>
}