package com.example.careconnect.Pages.Auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ViewModels.SuperViewModel

@Composable
fun Auth(mainNav : NavController,vm : AuthViewModel,s : SuperViewModel){
    val authNav = rememberNavController()
    NavHost(
        navController = authNav,
        startDestination = "commonAuth"
    ){
        composable("login") { Login(authNav,mainNav,vm,s) }
        composable("signup") { SignUp(authNav,) }
        composable("verification/{mail}",arguments = listOf(navArgument("mail") { type = NavType.StringType })){it ->
            val mail = it.arguments?.getString("mail") ?: ""
            Verification(authNav,mail,vm)
        }
        composable("commonAuth"){
            Column (
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp)
            ){
                Box (
                    modifier = Modifier
                        .padding(0.dp,70.dp,0.dp,90.dp)
                ){
                    Text(
                        "CareConnect",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
                Column{
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight(0.085f)
                            .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                            .clickable(true,null,null){
                                authNav.navigate("signup")
                            }
                    ) {
                        Text(
                            "Create Account",
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight(0.085f)
                            .background(Color(0xFF4A4C52), RoundedCornerShape(10.dp))
                            .clickable(true,null,null){
                                authNav.navigate("login")
                            }
                    ) {
                        Text(
                            "Already have an account?",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight(0.06f)
                    ) {
                        Text("Language ", color = MaterialTheme.colorScheme.inverseSurface, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}