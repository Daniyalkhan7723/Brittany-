package com.cp.brittany.dixon.data.api

import android.content.Intent
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.cp.brittany.dixon.di.MyApplication
import com.cp.brittany.dixon.ui.activities.MainActivity
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse {
    @OptIn(ExperimentalCoilApi::class)
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.code() == 401) {
                val preference = SharePreferenceHelper.getInstance(MyApplication.appContext)
                preference.clearAllPreferenceData()
                MyApplication.appContext.imageLoader.diskCache?.clear()
                MyApplication.appContext.imageLoader.memoryCache?.clear()
                val sIntent = Intent(MyApplication.appContext, MainActivity::class.java)
                sIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                MyApplication.appContext.startActivity(sIntent)
                return error("${response.code()} ${response.message()}")
            }
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return try {
                val jObjError =
                    JSONObject((response as Response<*>).errorBody()!!.string())
                return error(jObjError.toString())
            } catch (e: Exception) {
                error("${response.code()} ${response.message()}")
            }
            //return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String, data: T? = null): NetworkResult<T> =
        NetworkResult.Error(errorMessage, data)
//        NetworkResult.Error("Api call failed $errorMessage",data)
}