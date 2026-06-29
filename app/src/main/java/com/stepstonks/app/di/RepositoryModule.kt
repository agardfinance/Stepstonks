package com.stepstonks.app.di

import com.stepstonks.app.data.repository.*
import com.stepstonks.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindStepRepository(impl: StepRepositoryImpl): StepRepository

    @Binds @Singleton
    abstract fun bindChallengeRepository(impl: ChallengeRepositoryImpl): ChallengeRepository

    @Binds @Singleton
    abstract fun bindTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    @Binds @Singleton
    abstract fun bindSneakerRepository(impl: SneakerRepositoryImpl): SneakerRepository
}
