package com.example.careconnect.ui.Composables

import Notify
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun BottomBar(navController: NavController){
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        BottomBarItem.dash,
        BottomBarItem.chat,
        BottomBarItem.notifications,
        BottomBarItem.profile
    )
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp,0.dp,40.dp,5.dp)
    ) {
        for (item in items) {
            BottomNavBarItem (
                icon = item.icon,
                title = item.title,
                selected = selectedItem == items.indexOf(item),
                onClick = {
                    selectedItem = items.indexOf(item)
                    navController.navigate(item.route)
                },
                selectedIndex = selectedItem
            )
        }
    }
}

@Composable
fun BottomNavBarItem(icon : ImageVector,title: String,selected : Boolean,onClick : ()->Unit,selectedIndex : Int){
    IconButton(
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inverseSurface
        )
    }
}

sealed class BottomBarItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object dash : BottomBarItem(
        title = "dash",
        icon = Icons.Filled.Home,
        route = "dash"
    )
    object chat : BottomBarItem(
        title = "chat",
        icon = Notify,
        route = "chat"
    )
    object notifications : BottomBarItem(
        title = "notifications",
        icon = Icons.Outlined.Notifications,
        route = "notifications"
    )
    object profile : BottomBarItem(
        title = "profile",
        icon = Icons.Outlined.Person,
        route = "profile"
    )


}