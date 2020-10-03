package com.tincho5588.reddittopsreader.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.domain.usecase.Status
import com.tincho5588.reddittopsreader.presentation.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity: AppCompatActivity(R.layout.splash_screen) {
    private val viewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshTopPosts().observe(this) { result ->
            if (result.status != Status.LOADING) {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }
    }
}