package com.example.careconnect.Pages.General

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Models.DoctorDto
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ui.Composables.Doctorcard

@Composable
fun Hospital(a : AuthViewModel,hospitalid : String,name : String,distance : String,navController: NavController){
    var hospitalDoctors by remember { mutableStateOf(emptyList<DoctorDto>()) }

    LaunchedEffect(null) {
        hospitalDoctors = a.getHospitalDoctors(hospitalid)
        println("Doctors : $hospitalDoctors")
    }

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ){

        item {
            HospitalTopBar(name,distance,navController)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row {
                    Text(
                        text = "Available Experts",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(${hospitalDoctors.size})"
                    )
                }


            }
        }
        items(hospitalDoctors){ it ->
            Doctorcard(it, { navController.navigate("chat-screen/new/${false}/${it.id}") })
        }
    }
}

@Composable
fun HospitalTopBar(name : String,distance: String,navController: NavController) {
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp, 5.dp, 5.dp, 10.dp)

        ){
            IconButton(
                onClick = {navController.popBackStack()}
            ) {
                Icon(
                    Icons.Filled.ArrowBack,null
                )
            }
            Column(){
                Text(
                    text = name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E8AE7)
                )
                Text(
                    text = "Distance : $distance km",
                    fontSize = 17.sp,
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(45.dp)

            ){
                Image(
                    modifier = Modifier
                        .size(35.dp),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
                )
            }
        }
    }
}
