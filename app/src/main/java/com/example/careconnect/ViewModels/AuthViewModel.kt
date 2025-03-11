package com.example.careconnect.ViewModels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.careconnect.Database.ChatsDto
import com.example.careconnect.Database.UserData
import com.example.careconnect.Database.UserRepository
import com.example.careconnect.Database.toDto
import com.example.careconnect.Database.toUserdata
import com.example.careconnect.Pages.Hidden.ChatMessageDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    // Authorization state as StateFlow.
    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    // User state as StateFlow for Compose observation.
    private val _user = MutableStateFlow(UserData())
    val user: StateFlow<UserData> = _user.asStateFlow()

    // Instance of the UserRepository.
    private val repository = UserRepository()

    fun getChats(id : String) : List<ChatMessageDto>{
        return repository.getChats(id)
    }

    fun updateChats(chatid: String, messages : List<ChatMessageDto>){
        viewModelScope.launch {
            repository.updateChats(chatid,messages)
        }
    }

    // Save the user to Realm and update state.
    fun setUser(u: UserData) {
        viewModelScope.launch {
            println("Setting db user to : ${ u }")
            repository.setUser(u)
            _user.value = u
            login()
        }
    }

    // Retrieve the user from Realm and update state.
    fun getUser(): UserData? {
        // Fetch from the database.
        val storedUser = repository.getUser()?.toUserdata()
        storedUser?.let { _user.value = it }
            return storedUser

    }

    fun getChatList() : List<ChatsDto>{
        return repository.getAllChats().map { it.toDto() }
    }

    fun delete(){
        viewModelScope.launch {
            repository.deleteAllUsers()
        }
    }

    init {
        // Optionally load the user from the database when the view model is created.
        viewModelScope.launch {
            repository.getUser()?.let {
                _user.value = it.toUserdata()
                println("Found user : ${it.username}")
                login()
            }
        }
    }

    // Simulate login by setting the authorization state to true.
    fun login() {
        _isAuthorized.value = true
    }

    // Simulate logout by setting the authorization state to false.
    fun logout() {
        _isAuthorized.value = false
    }
}
