package com.tincho5588.reddittopsreader.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.repository.TopsRepository
import com.tincho5588.reddittopsreader.util.Constants.DATA_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity: AppCompatActivity(R.layout.splash_screen) {
    @Inject
    lateinit var topsRepository: TopsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topsRepository.fetchForItems("", DATA_PAGE_SIZE) {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                this.addFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }
}