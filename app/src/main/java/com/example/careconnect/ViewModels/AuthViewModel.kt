package com.example.careconnect.ViewModels

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.careconnect.Database.ChatMessageDto
import com.example.careconnect.Database.ChatsDto
import com.example.careconnect.Database.UserData
import com.example.careconnect.Database.UserRepository
import com.example.careconnect.Database.toDto
import com.example.careconnect.Database.toUserdata
import com.example.careconnect.Network.Calls.getHospitalCareGivers
import com.example.careconnect.Network.Calls.getHospitalsNearMe
import com.example.careconnect.Network.Calls.sendMessageCall
import com.example.careconnect.Network.Calls.updateLocation
import com.example.careconnect.Network.Models.Doctor
import com.example.careconnect.Network.Models.DoctorDto
import com.example.careconnect.Network.Models.Hospital
import com.example.careconnect.Network.Models.HospitalDto
import com.example.careconnect.Network.Models.HospitalsDto
import com.example.careconnect.Network.Models.SendMessageDto
import com.example.careconnect.Network.Models.toDto
import com.example.careconnect.Pages.Hidden.FirestoreMessage
import com.example.careconnect.Pages.Hidden.toChatMessageDto
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    // Authorization state as StateFlow.
    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    // User state as StateFlow for Compose observation.
    private val _user = MutableStateFlow(UserData())
    val user: StateFlow<UserData> = _user.asStateFlow()

    // Instance of the UserRepository.
    private val repository = UserRepository()
    // Access a Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    // Live updates of chat messages (converted to your UI model)
    private val _messages = mutableStateOf<List<ChatMessageDto>>(emptyList())
    val messages = _messages.value

    fun listenToChat(chatId: String) {
        // Remove any existing listener
        listenerRegistration?.remove()

        listenerRegistration = db.collection("chats").document(chatId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _messages.value = repository.getChats(chatId)
                }
                snapshot?.let {
                    if (it.exists()) {
                        // Deserialize FirestoreMessage and convert each MessageItem to ChatMessageDto
                        val firestoreMsg = it.toObject(FirestoreMessage::class.java)
                        val updatedMessages = firestoreMsg?.messages
                            ?.map { messageItem -> _user.value.id?.let { it1 ->
                                messageItem.toChatMessageDto(
                                    it1
                                )
                            } }
                            ?: emptyList()
                        _messages.value = updatedMessages as List<ChatMessageDto>
                    } else {
                        println("Does not exist")
                    }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }


    suspend fun getHospitals(): List<HospitalDto>{
        var response : List<HospitalDto> = emptyList()
        _user.value.token?.let { autht ->
            val resp = getHospitalsNearMe(autht)
            resp?.let { response = it }
            println(" gotten : $response")
        }
        return  response
    }

    suspend fun getHospitalDoctors(id: String) : List<DoctorDto>{
        var response = emptyList<Doctor>()
        _user.value.token?.let {
            val resp = getHospitalCareGivers(id = id,it)
            response = resp ?: emptyList()
            println(" gotten : ${response}")
        }
        return response.map { it.toDto() }
    }

    fun updatelocation(location: Location) : Int{
        var code : Int = 0
        viewModelScope.launch {
            _user.value.token?.let {code =  updateLocation(location.latitude,location.longitude, it) }
        }
        return code
    }

    fun getChats(id : String) : List<ChatMessageDto>{
        return repository.getChats(id)
    }

    fun updateChats(chatid: String, messages : List<ChatMessageDto>, receiver : String? = null){
        viewModelScope.launch {
            repository.updateChats(chatid,messages,receiver)
        }
    }
    fun sendMessage(message : String,recieverid : String,onResponse:(String)->Unit){
        viewModelScope.launch {
            _user.value.token?.let{ t ->
                println("vm calling send message")
                val resp =
                    sendMessageCall(
                        s = SendMessageDto(
                            message,
                            recieverid
                        ),
                        autht = t
                    )
            try{ resp?.chatid.let {
                if (it != null) {
                    onResponse(it)
                }
            } }catch (e : Exception){e.printStackTrace()}


            }
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
