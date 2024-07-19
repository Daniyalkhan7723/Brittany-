package com.cp.brittany.dixon.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,


    ) : AndroidViewModel(applicationContext) {


}