package com.example.careconnect.Pages.General

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.careconnect.Database.ChatsDto
import com.example.careconnect.R
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ui.Composables.ChatItem
import com.example.careconnect.ui.Composables.ChatListItem

@Composable
fun ChatListScreen(navController: NavController,authViewModel: AuthViewModel) {
    var chats by remember { mutableStateOf<List<ChatsDto>>(emptyList()) }
    LaunchedEffect(navController) {
        chats = authViewModel.getChatList()
    }

    ChatListPage(chats = chats, onChatClick = { chatId ->
        navController.navigate("chat-screen/${chatId}/${true}")
    })
}
@Composable
fun ChatListPage(
    chats: List<ChatsDto>,
    onChatClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(chats) { chat ->
            ChatListItem(chat = chat, onChatClick = onChatClick)
        }
    }
}
