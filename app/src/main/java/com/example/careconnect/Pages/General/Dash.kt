package com.example.careconnect.Pages.General

import Filter
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun PatientDashboardScreen(authViewModel: AuthViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var hospitals by remember { mutableStateOf<List<HospitalDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Fetch hospitals on launch
    LaunchedEffect(Unit) {
        scope.launch {
            hospitals = authViewModel.getHospitals()
            isLoading = false
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // User Profile Section
        item {
            UserProfileSection(authViewModel)
        }

        // Search Bar
        item {
            SearchBar(searchQuery) { searchQuery = it }
        }

        // Appointments Section
        item {
            SectionHeader(title = "Your Appointments", count = 5)
            LazyRow {
                items(4) {
                    AppointmentCard()
                }
            }
        }

        // Nearby Hospitals Section
        item {
            SectionHeader(title = "Hospitals Near You", count = hospitals.size)
        }

        // Loading Indicator
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // Hospitals List
            items(hospitals) { hospital ->
                HospitalCard(hospital) {
                    navController.navigate("hospital/${hospital.id}/${hospital.name}/${hospital.distance}")
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Row {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(5.dp))
            Text(text = "($count)", fontSize = 16.sp, fontWeight = FontWeight.Light)
        }
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}
@Composable
fun UserProfileSection(authViewModel: AuthViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(65.dp).clip(CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Hello, ${authViewModel.getUser()?.username ?: "Guest"}",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        label = { Text("Search here...") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 10.dp),
        shape = RoundedCornerShape(17.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Color(0x88E8E7E7),
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color(0x88E8E7E7),
        ),
        trailingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search Icon") }
    )
}
