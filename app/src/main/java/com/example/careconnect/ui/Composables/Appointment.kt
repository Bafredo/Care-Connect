package com.example.careconnect.ui.Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppointmentCard() {
    val formatter = SimpleDateFormat("dd MM yyyy")
    Column(
        modifier = Modifier
            .widthIn(400.dp, 420.dp)
            .heightIn(270.dp, 300.dp)
            .padding(11.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x884E8AE7)) // Solid background color
            .padding(9.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, if (isSystemInDarkTheme()) Color(0x88E8E7E7) else Color(0x88463B3B), CircleShape)
            ) {
                Text(
                    modifier = Modifier.padding(20.dp, 10.dp),
                    text = "Specialist"
                )
            }
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .border(1.dp, if (isSystemInDarkTheme()) Color(0x88E8E7E7) else Color(0x88463B3B), CircleShape)
            ) {
                Text(
                    modifier = Modifier.padding(20.dp, 10.dp),
                    text = "Hospital"
                )
            }
        }
        Text(
            text = "Dr Muchael Maina",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Due : ${formatter.format(Date(Date().time + 10000000000))}",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        DateCard()
    }
}

@Composable
fun DateCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x16C0CCE1))
            .padding(2.dp, 9.dp)
    ) {
        Row {
            Text("Availability")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            for (i in 0..6) {
                DayCard(i)
            }
        }
    }
}

@Composable
fun DayCard(day: Int) {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(1.dp)
            .width(45.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0x75C1CCDA))
            .padding(5.dp)
    ) {
        Text(text = days[day])
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(35.dp)
                .padding(5.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Text(
                text = day.toString(),
                fontSize = 18.sp
            )
        }
    }
}