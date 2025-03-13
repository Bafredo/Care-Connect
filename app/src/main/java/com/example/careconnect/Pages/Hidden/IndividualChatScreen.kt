package com.example.careconnect.Pages.Hidden

import Sound
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.careconnect.Database.ChatMessageDto
import com.example.careconnect.MainActivity
import com.example.careconnect.Network.Calls.chatBot
import com.example.careconnect.Network.Models.toChatMessage
import com.example.careconnect.Network.Models.toDto
import com.example.careconnect.R
import com.example.careconnect.ViewModels.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    vm: AuthViewModel,
    isBot: Boolean = false,
    contactName: String = "Name",
    chatid: String?,
    navController: NavController,
    context: Context,
    activity: MainActivity,
    recieverid : String? = null
) {
    var messages by remember { mutableStateOf(listOf<ChatMessageDto>())}
    var messageText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var chat_id by remember { mutableStateOf(chatid)}


    // Load chats only once (or when chatid changes) using LaunchedEffect
    if(chat_id != null){
        LaunchedEffect(chat_id) {
            chat_id?.let { vm.listenToChat(it) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.background
                else
                    Color(0xFFECF2FF)
            )
    ) {
        ChatHeader(contactName,navController,context,activity){
            navController.popBackStack()

        }
        MessagesList(messages, modifier = Modifier.weight(1f))
        ChatInput(
            messageText = messageText,
            onMessageChange = { messageText = it },
            onSend = {
                if (messageText.isNotBlank()) {
                    // Add the user's message
                    messages = messages + ChatMessageDto(messageText, isSent = true)
                    val sentMessage = messageText
                    messageText = ""

                    // If chat mode is bot, make the network call
                    if (isBot) {
                        coroutineScope.launch {
                            vm.getUser()?.token?.let { token ->
                                try {
                                    println(chatid)
                                    val response = chatBot(
                                        u = sentMessage,
                                        chatid = if(chat_id == "new") null else chat_id,
                                        autht = token
                                    )
                                    response?.toDto()?.toChatMessage()?.let { botMessage ->
                                        messages = messages + botMessage
                                        response.chatid?.let {
                                            vm.updateChats(it,messages)
                                            println("created chatId : ${it}")
                                            chat_id = it
                                        }
                                    }
                                } catch (e: Exception) {
                                    // TODO: Handle network exceptions (e.g., show an error message)
                                }
                            }
                        }
                    } else {
                        if(recieverid != null){
                            coroutineScope.launch {
                                vm.sendMessage(sentMessage,recieverid){it ->
                                    vm.updateChats(it,messages,recieverid)
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
@Composable
fun ChatHeader(contactName: String, navController: NavController, context: Context, activity: MainActivity,onBackClick : ()->Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile Picture"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = contactName)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val phoneNumber = "0795290373"
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                    val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(callIntent)
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 0)
                    Toast.makeText(context, "Call permission not granted", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Call"
                )
            }
            IconButton(onClick = { /* Handle more options */ }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More Options"
                )
            }
        }
    }
}


@Composable
fun MessagesList(messages: List<ChatMessageDto>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            ChatBubble(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Additional actions */ }) {
            Icon(
                imageVector = Icons.Rounded.AddCircle, // Using built-in AddCircle icon as an example
                contentDescription = "Add",
                modifier = Modifier.size(25.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = Sound,
            contentDescription = "Voice Message",
            tint = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.inverseSurface
            else
                Color.Black
        )
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.background
                else
                    Color.White,
                focusedBorderColor = Color(0xFF748A88),
                unfocusedBorderColor = Color(0x8871719D)
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = onSend,
            enabled = messageText.isNotBlank()
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                tint = if (messageText.isNotBlank())
                    Color(0xFF0B9B8E)
                else if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.inverseSurface
                else
                    Color.Black
            )
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessageDto) {
    val bubbleColor = if (message.isSent) {
        if (isSystemInDarkTheme()) MaterialTheme.colorScheme.inverseOnSurface else Color.Gray
    } else {
        Color.Gray
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(color = bubbleColor, shape = RoundedCornerShape(22.dp))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}
@Serializable
data class FirestoreMessage(
    var participants : List<Participant> = emptyList(),
    var messages : List<MessageItem> = emptyList(),
    var createdAt : Long = 0L
)

fun MessageItem.toChatMessageDto(userid : String) = ChatMessageDto(
    text = this.message,
    isSent = this.senderid == userid
)
@Serializable
data class MessageItem(
    var message : String = "",
    var senderid : String = "",
    val timestamp : Long = 0L
)
@Serializable
data class Participant(
    var id : String = "",
    var name : String = ""
)