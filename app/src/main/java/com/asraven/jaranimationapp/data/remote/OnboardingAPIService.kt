package com.asraven.jaranimationapp.data.remote

import retrofit2.http.GET


interface OnboardingApiService {
    @GET("_assets/shared/education-metadata.json")
    suspend fun getEducationMetadata(): OnboardingApiResponse
}