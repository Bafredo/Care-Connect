package com.example.careconnect

import DrawerContent
import ProfilePage
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.careconnect.Pages.Auth.Auth
import com.example.careconnect.Pages.General.ChatListScreen
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
import kotlinx.coroutines.launch

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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val color = MaterialTheme.colorScheme.background

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
                    modifier = Modifier.fillMaxSize()
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
                            PatientDashboardScreen(authViewModel)
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
                                onEditClick = {authViewModel.delete();authViewModel.logout()}
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
                            if (id != null) {
                                ChatScreen(isBot = bot!!,vm = authViewModel, chatid = id, navController = navController,context = applicationContext, activity = this@MainActivity)
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


