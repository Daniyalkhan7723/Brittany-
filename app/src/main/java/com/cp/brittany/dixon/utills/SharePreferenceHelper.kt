package com.cp.brittany.dixon.utills


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.cp.brittany.dixon.model.auth.User
import com.cp.brittany.dixon.utills.Constants.Companion.IS_FROM_SIGN_UP
import com.cp.brittany.dixon.utills.Constants.Companion.IS_LOGGED_IN
import com.cp.brittany.dixon.utills.Constants.Companion.TOKEN
import com.cp.brittany.dixon.utills.Constants.Companion.USER_MODEL
import com.cp.brittany.dixon.utills.Constants.Companion.USER_SHARED_PREFERENCE
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharePreferenceHelper private constructor(private val sharedPreferences: SharedPreferences) {

    private val dispatcher = Dispatchers.Default

    companion object {
        private var INSTANCE: SharePreferenceHelper? = null

        fun getInstance(context: Context): SharePreferenceHelper {
            val sharedPreference =
                context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE)
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharePreferenceHelper(sharedPreference).also { INSTANCE = it }
            }
        }
    }


    private suspend fun put(key: String, value: String) = withContext(dispatcher) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    private suspend fun put(key: String, value: Boolean) = withContext(dispatcher) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }


    suspend fun saveUserLogIn() {
        put(IS_LOGGED_IN, true)
    }

    suspend fun saveIsFromSignUp(check: Boolean) {
        put(IS_FROM_SIGN_UP, check)
    }

    fun IsFromSignUp(): Boolean {
        if (!sharedPreferences.contains(IS_FROM_SIGN_UP)) {
            return false
        }
        return sharedPreferences.getBoolean(IS_FROM_SIGN_UP, false)
    }

    fun isUserLoggedIn(): Boolean {
        if (!sharedPreferences.contains(IS_LOGGED_IN)) {
            return false
        }
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    suspend fun saveUser(user: User?) {
        if (user == null) {
            return
        }
        try {
            put(USER_MODEL, Gson().toJson(user))
        } catch (ex: Exception) {
            Log.e("saveUser error", ex.message.toString())
        }
    }

    fun getUser(): User? {
        return try {
            Gson().fromJson(sharedPreferences.getString(USER_MODEL, ""), User::class.java)
        } catch (ex: Exception) {
            Log.e("getUser error", ex.message.toString())
            null
        }
    }

    suspend fun saveToken(token: String) {
        put(TOKEN, token)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun clearAllPreferenceData() {
        sharedPreferences.edit().clear().apply()
    }

}
