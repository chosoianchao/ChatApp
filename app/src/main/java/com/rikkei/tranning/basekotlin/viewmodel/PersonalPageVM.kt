package com.rikkei.tranning.basekotlin.viewmodel

import com.rikkei.tranning.basekotlin.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalPageVM @Inject constructor() : BaseViewModel() {

    fun logOutAccount() {
        auth?.signOut()
    }
}
