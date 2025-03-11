package com.example.careconnect.Pages.General

import androidx.compose.runtime.Composable
import com.example.careconnect.R
import com.example.careconnect.ui.Composables.NotificationItem
import com.example.careconnect.ui.Composables.NotificationsPage

@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        NotificationItem("1", R.drawable.prof, "New message from Alice", "5 min ago"),
        NotificationItem("2", R.drawable.prof, "Your order has been shipped", "1 hour ago"),
        NotificationItem("3", R.drawable.prof, "System update available", "Yesterday")
    )

    NotificationsPage(notifications = notifications, onNotificationClick = { notificationId ->
        // Handle notification click (e.g., navigate to details)
    })
}
