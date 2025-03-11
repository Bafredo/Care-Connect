package com.example.careconnect.ui.Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun DoctorCard(){
    Row (
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(107.dp)
            .padding(6.dp)
            .border(1.dp, if(isSystemInDarkTheme()) Color(0x3C424242) else Color(0x88E8E7E7), RoundedCornerShape(17.dp))
    ){
        AsyncImage(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(41.dp)),
            model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5nBKAC0DZcUg8HA1AvsbCCUXut_Ts_0JvOw&s",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(17.dp)
        ){
            Text(
                text = "Dr.Name Another",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                Icon(
                    modifier = Modifier
                        .size(18.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary

                )
                Text(
                    text = "4.9 (3897 reviews)",
                    fontSize = 13.sp
                )
            }
            Text(
                text = "Cardio Specialist - Aga Khan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}