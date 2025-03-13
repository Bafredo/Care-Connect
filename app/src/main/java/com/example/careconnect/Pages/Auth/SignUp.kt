package com.example.careconnect.Pages.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Calls.register
import com.example.careconnect.Network.Models.RegisterUserDto
import kotlinx.coroutines.launch

@Composable
fun SignUp(authNav: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var responseCode by remember { mutableStateOf(0) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Loading state

    val scope = rememberCoroutineScope()

    fun registerUser() {
        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true // Start loading
            scope.launch {
                try {
                    responseCode = register(RegisterUserDto(username.trim(), email.trim(), password.trim()))
                    println("Registration resp code $responseCode" )
                    if (responseCode == 201) {
                        authNav.navigate("verification/$email")
                    }
                } catch (e: Exception) {
                    responseCode = -1 // Indicate error
                } finally {
                    isLoading = false // Stop loading after response
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(text = "Care Connect", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(30.dp))

        Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") }
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") }
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                    )
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        // Show error message if registration fails
        if (responseCode == -1) {
            Text("Registration failed. Try again.", color = Color.Red)
        }

        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Text("Forgot password?", modifier = Modifier.clickable { /* Handle forgot password */ })
        }
        Spacer(Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable { authNav.popBackStack() }
                    .background(Color.DarkGray, RoundedCornerShape(10.dp))
                    .padding(16.dp)
                    .width(80.dp)
            ) {
                Text("Back", color = Color.White)
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(enabled = !isLoading) { registerUser() }
                    .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                    .padding(16.dp)
                    .width(80.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Next", color = Color.White)
                }
            }
        }
    }
}
