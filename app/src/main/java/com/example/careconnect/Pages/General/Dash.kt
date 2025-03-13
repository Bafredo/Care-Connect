package com.example.careconnect.Pages.General


import Filter
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Models.HospitalDto
import com.example.careconnect.R
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ui.Composables.AppointmentCard
import com.example.careconnect.ui.Composables.HospitalCard
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboardScreen(a : AuthViewModel,navController: NavController) {
    var search by remember { mutableStateOf("") }
    var hospitals by remember { mutableStateOf(emptyList<HospitalDto>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(null) {
        scope.launch{
            hospitals = a.getHospitals()
            println(hospitals)
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(

                ) {
                    Image(
                        modifier = Modifier
                            .size(65.dp)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.profile),
                        contentDescription = null
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Hello, ${a.getUser()?.username}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold

                )
            }
        }

        item{
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp)
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Search here..") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(17.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Color(0x88E8E7E7),
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color(0x88E8E7E7),
                    ),
                    trailingIcon = { Icon(Icons.Outlined.Search, null) }
                )

            }
        }
        item{
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row {
                    Text(
                        text = "Your Appointments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(5)"
                    )
                }

                Icon(
                    imageVector = Icons.Filled.Add,
                    null
                )
            }
            Box {
                LazyRow {
                    items(4) {
                        AppointmentCard()

                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row {
                    Text(
                        text = "Hospitals Near You ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(55)"
                    )
                }

                Icon(
                    imageVector = Filter,
                    null
                )
            }

        }
                items(hospitals) { h ->
                    if (h != null) {
                        HospitalCard(h){navController.navigate("hospital/${h.id}/${h.name}/${h.distance}")}
                    }
                }
            }


}



