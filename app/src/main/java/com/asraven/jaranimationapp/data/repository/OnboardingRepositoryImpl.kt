package com.asraven.jaranimationapp.data.repository

import com.asraven.jaranimationapp.data.remote.OnboardingApiResponse
import com.asraven.jaranimationapp.data.remote.OnboardingApiService
import com.asraven.jaranimationapp.domain.repository.OnboardingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingApiService: OnboardingApiService
) : OnboardingRepository {

    override suspend fun getEducationMetadata(): Flow<Result<OnboardingApiResponse>> = flow {
        val response = onboardingApiService.getEducationMetadata()
        emit(Result.success(response))
    }.catch { exception ->
        emit(Result.failure(exception))
    }.flowOn(Dispatchers.IO) // Execute on IO dispatcher
}