package com.example.careconnect.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careconnect.Network.Models.RecievedUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SuperViewModel : ViewModel() {
    var state by mutableStateOf(State())
    var remote by mutableStateOf("")
    var tk by mutableStateOf(false)
    var autht = mutableStateOf("")
    private var usr = mutableStateOf(RecievedUser())
    fun setUsr(r : RecievedUser){
        usr.value = r
    }
    fun getUsr(): RecievedUser {return usr.value}
    fun authTAdd(s : String){
        autht.value = s
    }





    init {
        fetchRemoteToken() // Fetch token asynchronously
    }

    private fun fetchRemoteToken() {
        lateinit var remoteToken: String
        viewModelScope.launch {
            try {
                remoteToken = Firebase.messaging.token.await() // Wait for token
                onRemoteTokenChange(remoteToken)
                remote = remoteToken
                tk = true

            } catch (e: Exception) {
                println("Failed to get FCM token: ${e.localizedMessage}")
            }
        }
    }

    fun onRemoteTokenChange(t: String) {
        state = state.copy(remoteToken = t)
    }
}

data class State(
    val remoteToken: String = "",
    val message: String = ""
)
