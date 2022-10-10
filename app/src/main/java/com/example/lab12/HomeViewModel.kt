package com.example.lab12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.lab12.Constants.Companion.DEAFULT_MESSAGE
import com.example.lab12.Constants.Companion.EMPTY_MESSAGE
import com.example.lab12.Constants.Companion.FAILURE_MESSAGE
import com.example.lab12.Constants.Companion.SUCCESS_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val _status = MutableStateFlow<Status>(Status.default(DEAFULT_MESSAGE))
    val status : StateFlow<Status> = _status

    private val _progessBar = MutableStateFlow<Boolean>(false)
    val progressBar : StateFlow<Boolean> = _progessBar

    sealed class Status{

        class default(val message: String): Status()
        class success(val message: String): Status()
        class failure(val message: String): Status()
        class empty(val message: String): Status()

    }

    fun progressBar(){
        viewModelScope.launch {
            _progessBar.value = true
            delay(2000L)
            _progessBar.value = false
        }
    }

    fun Default(){
        progressBar()
        _status.value = Status.default(DEAFULT_MESSAGE)
    }

    fun Success(){
        progressBar()
        _status.value = Status.success(SUCCESS_MESSAGE)
    }

    fun Failure(){
        progressBar()
        _status.value = Status.failure(FAILURE_MESSAGE)
    }

    fun Empty(){
        progressBar()
        _status.value = Status.empty(EMPTY_MESSAGE)
    }
}