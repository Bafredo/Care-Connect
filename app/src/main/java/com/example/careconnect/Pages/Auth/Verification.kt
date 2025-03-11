package com.example.careconnect.Pages.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.TestModifierUpdaterLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Calls.validateToken
import com.example.careconnect.ViewModels.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun Verification(nav : NavController,mail : String,vm : AuthViewModel){
    var ver by remember { mutableStateOf("") }
    var resp by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()
    fun verify(){
        scope.launch {
            try{
                resp = validateToken(otp = ver, mail = mail) {

                }
                if (resp == 200) {
                    nav.popBackStack()
                    nav.popBackStack()

                    nav.navigate("login")
                }
            }catch (e : Exception){
                println(e.localizedMessage)
            }
        }
    }
    Column (
       modifier = Modifier
           .padding(20.dp,10.dp)
    ){
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Verification",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(30.dp))
        Text(
            text = "Please check your email for a One-Time Password (OTP) that we have sent to you at $mail",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraLight
        )
        Spacer(Modifier.height(10.dp))

        Spacer(Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ){
            OutlinedTextField(
                value = ver,
                onValueChange = { it -> ver = it }
            )
        }
        Spacer(Modifier.height(20.dp))
        Text("$resp")
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(true, null, null) {
                        verify()
                    }
                    .height(50.dp)
                    .fillMaxWidth(0.6f)
                    .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text("Verify")
            }
        }
    }
}