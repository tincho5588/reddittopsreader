package com.tincho5588.reddittopsreader.util

object Utils {
    fun calculateCreatedTimeHours(created_utc_seconds: Long): Int {
        return ((System.currentTimeMillis() / 1000 - created_utc_seconds) / 3600).toInt()
    }
}