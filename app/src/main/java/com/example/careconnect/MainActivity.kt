package com.example.careconnect

import ProfilePage
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.careconnect.Pages.Auth.Auth
import com.example.careconnect.Pages.General.ChatListScreen
import com.example.careconnect.Pages.General.Hospital
import com.example.careconnect.Pages.General.NotificationsScreen
import com.example.careconnect.Pages.General.PatientDashboardScreen
import com.example.careconnect.Pages.Hidden.ChatScreen
import com.example.careconnect.Pages.Hidden.EditProfilePage
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ViewModels.SuperViewModel
import com.example.careconnect.ui.Composables.BottomBar
import com.example.careconnect.ui.Composables.ChatTBar
import com.example.careconnect.ui.Composables.DashTBar
import com.example.careconnect.ui.Composables.FloatingActionButton
import com.example.compose.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val  superModel = SuperViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotification()
        enableEdgeToEdge()
        setContent {
            AppTheme {

                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                val systemUiController = rememberSystemUiController()
                val useDark = !isSystemInDarkTheme()
                val chatBgColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color(0xFFECF2FF)
                val systemColors =  if(currentRoute == "chat-screen/{isBot}") chatBgColor else if(currentRoute == "chat-screen" || currentRoute == "chat")  MaterialTheme.colorScheme.inverseOnSurface  else MaterialTheme.colorScheme.surface
                val color = MaterialTheme.colorScheme.background

                val context = applicationContext
                val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
                val location = remember { mutableStateOf<Location?>(null) }




                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult  = {}
                )
                LaunchedEffect(key1 = context) {
                    val permissionStatus = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        Toast.makeText(context, "Location Permission InUse", Toast.LENGTH_SHORT).show()
                        fusedLocationClient.lastLocation.addOnSuccessListener { it ->
                            location.value = it
                        }
                    }
                }




                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = systemColors,
                        darkIcons = useDark
                    )
                    systemUiController.setNavigationBarColor(
                        color = if(currentRoute != "chat-screen" && currentRoute != "chat") systemColors else color,
                        darkIcons = false
                    )
                }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        ,
                    topBar = {
                        if (currentRoute == "dash") {
                            DashTBar(superModel)
                        } else if (currentRoute == "chat"){
                            ChatTBar()
                        }
                    },
                    bottomBar = {
                        if ((currentRoute != "auth") && (currentRoute != "chat-screen/{id}/{isBot}")) {
                            BottomBar(navController)
                        }
                    },
                    floatingActionButton = {
                        if(currentRoute == "chat"){
                            FloatingActionButton(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (!authViewModel.isAuthorized.collectAsState().value) "auth" else "dash",
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        composable("auth") {
                            Auth(navController, authViewModel, superModel)
                        }
                        composable("dash") {
                            LaunchedEffect(key1 = Unit) {
                                location.value?.let { it1 -> authViewModel.updateLocation(it1) }
                            }
                            PatientDashboardScreen(authViewModel,navController)
                        }
                        composable("chat") {
                            ChatListScreen(navController,authViewModel)
                        }
                        composable("notifications") {
                            NotificationsScreen()
                        }
                        composable("profile") {
                            ProfilePage(
                                name = authViewModel.getUser()?.username,
                                email = authViewModel.getUser()?.username,
                                profileImageRes = R.drawable.prof,
                                onEditClick = {authViewModel.deleteAllUsers();authViewModel.logout()}
                            )
                        }
                        composable("editprofile/{mail/{username}}",arguments = listOf(navArgument("mail") { type = NavType.StringType },
                            navArgument("username"){type = NavType.StringType})) { it ->
                            val mail = it.arguments?.getString("mail") ?: ""
                            val username = it.arguments?.getString("username") ?: ""
                            EditProfilePage(
                                initialName  =  username,
                                initialEmail = mail,
                                profileImageRes = R.drawable.prof,
                                onSaveClick = {name,new ->  }
                            )
                        }
                        composable("chat-screen/{id}/{isBot}", arguments = listOf(navArgument("isBot"){type = NavType.BoolType},navArgument("id"){type = NavType.StringType})){ it ->
                            val bot = it.arguments?.getBoolean("isBot")
                            val id = it.arguments?.getString("id")
                            println("NAvigating with $id")
                            if (id != null) {
                                ChatScreen(isBot = bot!!,vm = authViewModel, chatid = id, navController = navController,context = applicationContext, activity = this@MainActivity)
                            }
                        }
                        composable("hospital/{id}/{name}/{distance}", arguments = listOf(navArgument("id"){type= NavType.StringType},
                            navArgument("name"){type= NavType.StringType},
                            navArgument("distance"){type= NavType.StringType})){ it ->
                            val id = it.arguments?.getString("id")
                            val name = it.arguments?.getString("name")
                            val distance = it.arguments?.getString("distance")
                             id?.let {
                                 if (name != null) {
                                     if (distance != null) {
                                         Hospital(authViewModel,it,name,distance,navController)
                                     }
                                 }
                             }
                        }
                        composable("chat-screen/{id}/{isBot}/{recieverId}/{recieverName}", arguments = listOf(
                            navArgument("isBot"){type = NavType.BoolType},
                            navArgument("id"){type = NavType.StringType},
                            navArgument("recieverId"){type = NavType.StringType},
                            navArgument("recieverName"){type = NavType.StringType}

                        )){ it ->
                            val bot = it.arguments?.getBoolean("isBot")
                            val id = it.arguments?.getString("id")
                            val recieverid = it.arguments?.getString("recieverId")
                            val recieverName = it.arguments?.getString("recieverName")
                            println("NAvigating with $id")
                            if (id != null) {
                                if (recieverid != null) {
                                    ChatScreen(
                                        isBot = bot!!,
                                        vm = authViewModel,
                                        chatid = id,
                                        navController = navController,
                                        context = applicationContext,
                                        activity = this@MainActivity,
                                        recieverId = recieverid,
                                        recieverName = recieverName)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
    private fun requestNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }

        }
    }

}


