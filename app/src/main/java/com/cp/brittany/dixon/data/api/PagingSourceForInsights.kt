package com.cp.brittany.dixon.data.api

import android.content.Intent
import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.cp.brittany.dixon.di.MyApplication
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.ui.activities.MainActivity
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import retrofit2.HttpException
import java.io.IOException

class PagingSourceForInsights(
    private val apiInterface: ApiInterface,
    private val bearerToken: String,
    private val query: String,
    private val screenType: String
) : PagingSource<Int, AllInsight>() {
    @OptIn(ExperimentalCoilApi::class)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllInsight> {
        return try {
            val requestPage = params.key ?: 1 // default page to 1


            val response =
                when (screenType) {
                    Constants.SEARCH_ALL_INSIGHT_FAVOURITES -> apiInterface.allFavouriteInsights(
                        token = "Bearer $bearerToken",
                        search = query,
                        page = requestPage
                    )

                    Constants.VIEW_ALL_INSIGHT_FAVOURITES -> {
                        apiInterface.allFavouriteInsights(
                            token = "Bearer $bearerToken",
                            search = query,
                            page = requestPage
                        )
                    }

                    else -> apiInterface.allInsights(
                        token = "Bearer $bearerToken",
                        search = query,
                        page = requestPage
                    )
                }

            val body = response.body()

            if (response.code() == 401) {
                val preference = SharePreferenceHelper.getInstance(MyApplication.appContext)
                preference.clearAllPreferenceData()
                MyApplication.appContext.imageLoader.diskCache?.clear()
                MyApplication.appContext.imageLoader.memoryCache?.clear()
                val sIntent = Intent(MyApplication.appContext, MainActivity::class.java)
                sIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                MyApplication.appContext.startActivity(sIntent)
            }

            if (response.isSuccessful && body?.data != null) {
                val prePage = if (requestPage == 1) null else requestPage - 1

                LoadResult.Page(
                    data = body.data,
                    prevKey = prePage,
                    nextKey = if (requestPage == body.pagination.last_page) null else requestPage + 1
                )
            } else {
                LoadResult.Error(Exception("Response body Invalid"))
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllInsight>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.minus(1) ?: anchorPage?.nextKey?.plus(1)
        }
    }

}