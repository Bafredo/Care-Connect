package com.example.careconnect.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NotificationItem(
    val id: String,
    val iconRes: Int, // Drawable resource ID
    val message: String,
    val timestamp: String
)

@Composable
fun NotificationsPage(
    notifications: List<NotificationItem>,
    onNotificationClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(notifications) { notification ->
            NotificationListItem(notification = notification, onNotificationClick = onNotificationClick)
        }
    }
}

@Composable
fun NotificationListItem(notification: NotificationItem, onNotificationClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNotificationClick(notification.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = notification.iconRes),
            contentDescription = "Notification Icon",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = notification.timestamp,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
