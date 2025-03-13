package com.example.careconnect.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.careconnect.Network.Models.HospitalDto
import com.example.careconnect.R

@Composable
fun HospitalCard(h : HospitalDto, onClick : ()->Unit){
    Row (
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(107.dp)
            .clickable {
                onClick()
            }
            .padding(6.dp)
            .border(1.dp, if(isSystemInDarkTheme()) Color(0x3C424242) else Color(0x88E8E7E7), RoundedCornerShape(17.dp))
    ){
        Image(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(41.dp)),
            painter = painterResource(R.drawable.hospital),
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
                text = h.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                softWrap = false
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
                    text = "Level : ${h.level}",
                    fontSize = 13.sp
                )
            }
            Text(
                text = "Distance : ${h.distance} km",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}