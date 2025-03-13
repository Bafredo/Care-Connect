package com.example.careconnect.Pages.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Database.UserData
import com.example.careconnect.Network.Calls.login
import com.example.careconnect.Network.Calls.updateRemote
import com.example.careconnect.Network.Models.RecievedUser
import com.example.careconnect.Network.Models.User
import com.example.careconnect.ViewModels.AuthViewModel
import com.example.careconnect.ViewModels.SuperViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(authNav: NavController, mainNav: NavController, authVM: AuthViewModel, superVM: SuperViewModel) {
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text("Care Connect", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(30.dp))

        Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = usernameOrEmail,
            onValueChange = { usernameOrEmail = it },
            label = { Text("Username or Email") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            singleLine = true
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        loginError?.let {
            Text(it, color = Color.Red, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text("Forgot password?", color = Color.Blue, modifier = Modifier.clickable { /* Navigate to forgot password */ })
        }

        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Button(
                onClick = { authNav.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.width(100.dp)
            ) {
                Text("Back", color = Color.White)
            }

            Button(
                onClick = {
                    if (usernameOrEmail.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val response = login(User(usernameOrEmail.trim(), password.trim()))
                                val updateCode = updateRemote(superVM.remote, response.token!!)

                                if (updateCode == 200 || updateCode == 409) {
                                    authVM.setUser(
                                        UserData(
                                            username = response.user?.username,
                                            email = response.user?.email,
                                            password = password,
                                            token = response.token
                                        )
                                    )
                                    authVM.login()
                                    mainNav.navigate("dash")
                                } else {
                                    loginError = "Failed to update remote data"
                                }
                            } catch (e: Exception) {
                                loginError = "Login failed: ${e.localizedMessage}"
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        loginError = "Please fill in all fields"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F36AB)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.width(100.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Login",color = Color.White)
                }
            }
        }
    }
}
