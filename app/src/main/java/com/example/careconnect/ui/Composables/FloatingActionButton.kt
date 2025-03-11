package com.example.careconnect.ui.Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FloatingActionButton(navController: NavController){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable{
                navController.navigate("chat-screen/''/${true}")
            }
            .height(50.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF4E8AE7))
    ){
        Text(
            "Chat With AI",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}