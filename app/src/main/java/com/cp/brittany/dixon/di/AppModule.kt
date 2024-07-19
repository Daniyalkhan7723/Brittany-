package com.cp.brittany.dixon.di

import android.app.Application
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.cp.brittany.dixon.data.api.ApiInterface
import com.cp.brittany.dixon.data.dataSource.RemoteDataSource
import com.cp.brittany.dixon.data.dataSource.RemoteDataSourceIml
import com.cp.brittany.dixon.utills.ChooseSubscription
import com.cp.brittany.dixon.utills.Constants.Companion.BASE_URL
import com.cp.brittany.dixon.utills.Constants.Companion.CLIENT_ID
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(15, TimeUnit.MINUTES)
            .connectTimeout(15, TimeUnit.MINUTES).build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory).build()
    }

    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun getSharedPreference(context: Application): SharePreferenceHelper =
        SharePreferenceHelper.getInstance(context)


    @Singleton
    @Provides
    fun providesDataSource(apiService: ApiInterface): RemoteDataSource {
        return RemoteDataSourceIml(apiService)
    }


    @Singleton
    @Provides
    fun getGoogleSignInClient(context: Application): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }

    @Singleton
    @Provides
    fun getLoginManager(): LoginManager {
        return LoginManager.getInstance()
    }

    
//    @Provides
//    @Singleton
//    fun provideVideoPlayer(context: Application): ExoPlayer {
//        return ExoPlayer.Builder(context)
//            .build()
//    }


}