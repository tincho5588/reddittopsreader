package com.tincho5588.reddittopsreader.injection.managers

import android.content.Context
import com.tincho5588.reddittopsreader.domain.usecase.SyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.login.AnonymousLoginUseCase
import com.tincho5588.reddittopsreader.manager.login.LoginManager
import com.tincho5588.reddittopsreader.manager.login.LoginManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {
    @Provides
    @Singleton
    fun provideLoginManager(
        @ApplicationContext context: Context,
        @Named("AnonymousLoginUseCase") anonymousLoginUseCase: SyncUseCase<AnonymousLoginUseCase.RequestValues, String>
    ): LoginManager {
        return LoginManagerImpl(context, anonymousLoginUseCase)
    }
}