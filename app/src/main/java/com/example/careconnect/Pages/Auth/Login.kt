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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun Login(authNav: NavController,main : NavController,vm : AuthViewModel,s : SuperViewModel){

    var field1 by remember { mutableStateOf("") }
    var field2 by remember { mutableStateOf("") }

    var obs by remember { mutableStateOf(true) }

    var resp by remember { mutableStateOf(RecievedUser()) }

    fun handleField1() = {it : String ->
        field1 = it
    }
    fun handleField2() = {it : String ->
        field2 = it
    }
    val scope = rememberCoroutineScope()

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 10.dp)
    ){
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Care Connect",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(30.dp))
        Text(
            "Sign in ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text ="Account",

        )
        Spacer(Modifier.height(7.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = field1,
            onValueChange = handleField1(),
            placeholder = { Text("Username or E-mail") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Person,
                    null
                )
            },
            )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            placeholder = { Text("password") },
            modifier = Modifier
                .fillMaxWidth(),
            value = field2,
            onValueChange = handleField2(),
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
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = if(obs) ShowPassword else HidePassword,
                        contentDescription =   null,
                        contentScale = ContentScale.Inside
                    )
                }
            }

        )
        Spacer(Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box {
                Text(
                    "Forgot password?"
                )
            }

        }
        Spacer(Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(true, null, null) {
                        authNav.popBackStack()
                    }
                    .background(Color.DarkGray, RoundedCornerShape(10.dp))
                    .padding(20.dp)
                    .width(70.dp)

            ) { Text("Back") }
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(true, null, null) {
                        if (field1.isNotEmpty() && field2.isNotEmpty()) {

                                scope.launch {
                                    try {
                                    val rec = login(User(field1.trim(), field2.trim()))
                                        val code = updateRemote(s.remote,rec.token!!)

                                        if (code == 200 || code == 409) {

                                        vm.setUser(UserData(username = field1, email =  rec.user?.email, password =  field2, token =  rec.token))
                                        vm.login()

                                        }
                                    } catch (e: Exception) {
                                        println(e.localizedMessage)
                                    }

                                }

                        }
                    }
                    .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                    .padding(20.dp)
                    .width(70.dp)
            ){ Text("Next") }
        }
    }

}