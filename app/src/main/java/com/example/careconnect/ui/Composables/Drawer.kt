@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(onItemClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text("Drawer Item 1", modifier = Modifier.padding(8.dp))
        Text("Drawer Item 2", modifier = Modifier.padding(8.dp))
        Text("Drawer Item 3", modifier = Modifier.padding(8.dp))
    }
}

