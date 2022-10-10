package com.example.lab12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel:ViewModel() {
    private val _logger = MutableStateFlow<logStatus>(logStatus.notLogged)
    val logger: StateFlow<logStatus> = _logger

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private lateinit var correctData : String

    private val _validAuthToken = MutableStateFlow(false)
    val validAuthToken: StateFlow<Boolean> = _validAuthToken

    private lateinit var job: Job

    sealed class logStatus{
        object notLogged: logStatus()
        object checkData: logStatus()
        object failure: logStatus()
        object logged: logStatus()
    }

    fun checkUser(){
        _logger.value = logStatus.checkData
        viewModelScope.launch {
            delay(2000L)
            if (_username.value == correctData && _password.value == correctData){
                _validAuthToken.value = true
                _logger.value = logStatus.logged
            }
            else{
                _logger.value = logStatus.failure
            }
        }
    }

    fun setDataUser(username: String, password: String){
        _username.value = username
        _password.value = password
        checkUser()
    }

    fun regresiveCount(){
        job = viewModelScope.launch {
            delay(30000L)
            //delay(5000L) //tiempo para testear
            _validAuthToken.value = false
            _logger.value = logStatus.notLogged
        }
    }

    fun setCorrectDataUser(data: String){
        correctData = data
    }

    fun stop() {
        if (this::job.isInitialized && job.isActive) {
            job.cancel()
            _logger.value = logStatus.notLogged

        }
    }
}