package com.stepstonks.app.di

import android.content.Context
import com.stepstonks.app.data.local.database.StepstonksDatabase
import com.stepstonks.app.data.local.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StepstonksDatabase =
        StepstonksDatabase.getInstance(context)

    @Provides fun provideUserDao(db: StepstonksDatabase): UserDao = db.userDao()
    @Provides fun provideStepDao(db: StepstonksDatabase): StepDao = db.stepDao()
    @Provides fun provideChallengeDao(db: StepstonksDatabase): ChallengeDao = db.challengeDao()
    @Provides fun provideTokenDao(db: StepstonksDatabase): TokenDao = db.tokenDao()
    @Provides fun provideSneakerDao(db: StepstonksDatabase): SneakerDao = db.sneakerDao()
}
