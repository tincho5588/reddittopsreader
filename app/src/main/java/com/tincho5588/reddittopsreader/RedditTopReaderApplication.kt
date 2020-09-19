package com.tincho5588.reddittopsreader

import android.app.Application
import com.tincho5588.reddittopsreader.data.repository.TopsRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class RedditTopReaderApplication : Application() {
    @Inject
    lateinit var topsRepository: TopsRepository

    override fun onCreate() {
        super.onCreate()
        topsRepository.refreshPosts()
    }
}