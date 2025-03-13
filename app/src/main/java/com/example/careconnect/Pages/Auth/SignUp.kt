package com.example.careconnect.Pages.Auth

import HidePassword
import ShowPassword
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Calls.register
import com.example.careconnect.Network.Models.RegisterUser
import com.example.careconnect.Network.Models.RegisterUserDto
import com.example.careconnect.ViewModels.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Modifier

@Composable
fun SignUp(authNav : NavController){

    var field1 by remember { mutableStateOf("") }
    var field2 by remember { mutableStateOf("") }
    var field3 by remember { mutableStateOf("") }
    var resp by remember { mutableStateOf(0) }
    var obs by remember { mutableStateOf(true) }

    fun handleField1() = {it : String ->
        field1 = it
    }
    fun handleField2() = {it : String ->
        field2 = it
    }
    fun handleField3() = {it : String ->
        field3 = it
    }

    val scope = rememberCoroutineScope()

    fun registerView(f1 : String,f2 : String,f3 : String){
        scope.launch {
            try {
            resp = register(RegisterUserDto(f1,f2,f3))
            if(resp == 201){ authNav.navigate("verification/$field2") }
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(30.dp, 10.dp)
    ){
        Spacer(androidx.compose.ui.Modifier.height(20.dp))

        Text(
            text = "Care Connect",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(androidx.compose.ui.Modifier.height(30.dp))
        Text(
            "Sign Up ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(androidx.compose.ui.Modifier.height(20.dp))
        Text(
            text ="Account",

            )
        Spacer(androidx.compose.ui.Modifier.height(7.dp))
        OutlinedTextField(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            value = field1,
            onValueChange = handleField1(),
            placeholder = { Text("Username ") },
        )
        Spacer(androidx.compose.ui.Modifier.height(20.dp))

        OutlinedTextField(
            placeholder = { Text("Email") },
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            value = field2,
            onValueChange = handleField2()
        )
        Spacer(androidx.compose.ui.Modifier.height(20.dp))
        OutlinedTextField(
            placeholder = { Text("password") },
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            value = field3,
            onValueChange = handleField3(),
            visualTransformation = if(obs) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {obs = !obs}
                ) {
                    Image(
                        modifier = androidx.compose.ui.Modifier
                            .size(20.dp),
                        imageVector = if(obs) ShowPassword else HidePassword,
                        contentDescription =   null,
                        contentScale = ContentScale.Inside
                    )
                }
            }

        )
        Spacer(androidx.compose.ui.Modifier.height(5.dp))
        Text("$resp")
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box {
                    Text(
                        "Forgot password?"
                    )
            }

        }
        Spacer(androidx.compose.ui.Modifier.height(7.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = androidx.compose.ui.Modifier
                    .clickable(true, null, null) {
                        authNav.popBackStack()
                    }
                    .background(Color.DarkGray, RoundedCornerShape(10.dp))
                    .padding(20.dp)
                    .width(70.dp)

            ) { Text("Back") }
            Box (
                contentAlignment = Alignment.Center,
                modifier = androidx.compose.ui.Modifier
                    .clickable(true, null, null) {
                        if (field1.isNotEmpty() && field3.isNotEmpty() && field2.isNotEmpty()) {

                                registerView(field1.trim(), field2.trim(), field3.trim())

                        }
                    }
                    .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                    .padding(20.dp)
                    .width(70.dp)
            ){ Text("Next") }
        }
    }

}