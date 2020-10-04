package com.tincho5588.reddittopsreader.manager.login

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.preference.PreferenceManager
import com.tincho5588.reddittopsreader.domain.usecase.SyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.login.AnonymousLoginUseCase
import com.tincho5588.reddittopsreader.util.Constants
import java.util.*

class LoginManagerImpl(
    val context: Context,
    val anonymousLoginUseCase: SyncUseCase<AnonymousLoginUseCase.RequestValues, String>
) : LoginManager {
    @VisibleForTesting
    internal val deviceId: String by lazy {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var uuid: String = sharedPreferences.getString(Constants.DEVICE_ID_PREF_KEY, "") ?: ""
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(Constants.DEVICE_ID_PREF_KEY, uuid).apply()
        }
        uuid
    }

    // ToDo: handle errors here
    override fun getDeviceAccessToken(): String {
        anonymousLoginUseCase.requestValues = AnonymousLoginUseCase.RequestValues(deviceId)

        return anonymousLoginUseCase.run().data ?: ""
    }

}