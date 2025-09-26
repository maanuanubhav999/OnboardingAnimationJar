package com.asraven.jaranimationapp.di

import com.asraven.jaranimationapp.data.repository.OnboardingRepositoryImpl
import com.asraven.jaranimationapp.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
abstract class AppDiModule {

    @Binds
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository


}



