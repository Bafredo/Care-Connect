package com.example.careconnect.Pages.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.careconnect.Network.Calls.validateToken
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Verification(nav: NavController, mail: String) {
    var otpDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    var responseCode by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun verify() {
        val otp = otpDigits.joinToString("")
        if (otp.length < 6) {
            errorMessage = "Enter all 6 digits"
            return
        }

        isLoading = true
        errorMessage = null

        scope.launch {
            try {
                responseCode = validateToken(
                    otp = otp, mail = mail,
                    success = {nav.popBackStack();nav.popBackStack();nav.navigate("login")}
                )
                if (responseCode == 200) {
                    nav.navigate("login") {
                        popUpTo(nav.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    errorMessage = "Invalid OTP. Try again."
                }
            } catch (e: Exception) {
                errorMessage = "Network error. Try again."
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text(text = "Verification", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Enter the 6-digit OTP sent to $mail",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(20.dp))

        // OTP Input Row
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            otpDigits.forEachIndexed { index, digit ->
                OutlinedTextField(
                    value = digit,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            otpDigits[index] = newValue
                            if (newValue.isNotEmpty() && index < 5) {
                                focusManager.moveFocus(FocusDirection.Next)
                            } else if (index == 5) {
                                keyboardController?.hide()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(4.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    visualTransformation = VisualTransformation.None
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(20.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable(enabled = !isLoading) { verify() }
                .height(50.dp)
                .fillMaxWidth(0.6f)
                .background(Color(0xFF0F36AB), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            } else {
                Text("Verify", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
