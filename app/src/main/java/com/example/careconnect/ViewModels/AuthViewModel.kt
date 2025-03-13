package com.example.careconnect.ViewModels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careconnect.Database.*
import com.example.careconnect.Network.Calls.*
import com.example.careconnect.Network.Models.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    // Authorization state
    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    // User data
    private val _user = MutableStateFlow(UserData())
    val user: StateFlow<UserData> = _user.asStateFlow()

    private val repository = UserRepository()
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    // Live chat messages
    private val _messages = MutableStateFlow<List<ChatMessageDto>>(emptyList())
    val messages: StateFlow<List<ChatMessageDto>> = _messages.asStateFlow()

    /**
     * Listens to chat updates from Firestore in real-time.
     */
    fun listenToChat(chatId: String) {
        listenerRegistration?.remove() // Remove previous listener to avoid duplication

        listenerRegistration = db.collection("chats").document(chatId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Firestore listener error: ${error.localizedMessage}")
                    return@addSnapshotListener
                }

                snapshot?.let {
                    if (it.exists()) {
                        val firestoreMsg = it.toObject(FirestoreMessage::class.java)
                        firestoreMsg?.messages?.mapNotNull { messageItem ->
                            _user.value.id?.let { userId -> messageItem.toChatMessageDto(userId) }
                        }?.let { newMessages ->
                            _messages.update { newMessages }
                        }
                    } else {
                        Log.w(TAG, "Chat document does not exist")
                    }
                }
            }
    }

    /**
     * Sends a message and updates the chat list.
     */
    fun sendMessage(message: String, receiverId: String, onResponse: (String) -> Unit) {
        viewModelScope.launch {
            _user.value.token?.let { token ->
                try {
                    val resp = sendMessageCall(SendMessageDto(message, receiverId), token)
                    resp?.chatid?.let { chatId ->
                        onResponse(chatId)

                        // Append the new message to the existing list
                        _messages.update { currentMessages ->
                            currentMessages + ChatMessageDto(
                                text = message,
                                isSent = true
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending message: ${e.localizedMessage}")
                }
            }
        }
    }

    /**
     * Updates the chat messages both locally and in Firestore.
     */
    fun updateChats(chatId: String, messages: List<ChatMessageDto>, receiverId: String? = null,recieverName : String? = null) {
        viewModelScope.launch {
            repository.updateChats(chatId, messages, receiverId,recieverName)
            _messages.update { messages } // Ensure UI updates
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

    /**
     * Fetches nearby hospitals based on user location.
     */
    suspend fun getHospitals(): List<HospitalDto> {
        return _user.value.token?.let { authToken ->
            getHospitalsNearMe(authToken).orEmpty()
        } ?: emptyList()
    }

    /**
     * Fetches doctors from a hospital.
     */
    suspend fun getHospitalDoctors(id: String): List<DoctorDto> {
        return _user.value.token?.let { token ->
            getHospitalCareGivers(id, token)?.map { it.toDto() }.orEmpty()
        } ?: emptyList()
    }

    /**
     * Updates user's location on the server.
     */
    suspend fun updateLocation(location: Location): Int {
        return _user.value.token?.let { token ->
            updateLocation(location.latitude, location.longitude, token)
        } ?: 0
    }

    /**
     * Fetches previous chat history from local database.
     */
    fun getChats(id: String): List<ChatMessageDto> {
        return repository.getChats(id)
    }

    /**
     * Sets the user in the local database and updates ViewModel state.
     */
    fun setUser(user: UserData) {
        viewModelScope.launch {
            Log.d(TAG, "Setting user in DB: $user")
            repository.setUser(user)
            _user.value = user
            login()
        }
    }

    /**
     * Retrieves the currently logged-in user from local storage.
     */
    fun getUser(): UserData? {
        val storedUser = repository.getUser()?.toUserdata()
        storedUser?.let { _user.value = it }
        return storedUser
    }

    /**
     * Fetches list of user's chat conversations.
     */
    fun getChatList(): List<ChatsDto> {
        return repository.getAllChats().map { it.toDto() }
    }

    /**
     * Deletes all user data from the local database.
     */
    fun deleteAllUsers() {
        viewModelScope.launch {
            repository.deleteAllUsers()
        }
    }

    /**
     * Handles user login.
     */
    fun login() {
        _isAuthorized.update { true }
    }

    /**
     * Handles user logout.
     */
    fun logout() {
        _isAuthorized.update { false }
    }

    init {
        viewModelScope.launch {
            repository.getUser()?.let {
                _user.value = it.toUserdata()
                Log.d(TAG, "Found user: ${it.username}")
                login()
            }
        }
    }
}
