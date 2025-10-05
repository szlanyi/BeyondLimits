package com.example.beyondlimits.feature_auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.theme.ButtonRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // ✅ Erfolgreich eingeloggt → weiter navigieren
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onAuthSuccess()
    }

    // ❌ Fehlermeldungen anzeigen
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 🔹 Hintergrundbild
            Image(
                painter = painterResource(id = R.drawable.bg_img2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 🔹 Gradient Overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.75f),
                                Color.Black.copy(alpha = 0.85f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // 🔹 Inhalt
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp, vertical = 64.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.beyondlogo),
                    contentDescription = "Beyond Limits Logo",
                    modifier = Modifier
                        .height(80.dp)
                        .padding(bottom = 24.dp)
                )

                // Titel / Animation Login <-> Register
                AnimatedContent(
                    targetState = isLoginMode,
                    label = "AuthModeTransition",
                    transitionSpec = {
                        fadeIn(tween(600)) togetherWith fadeOut(tween(400))
                    }
                ) { loginMode ->

                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (loginMode) "Login" else "Sign Up",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // ✅ Name-Feld nur bei Registrierung
                        if (!loginMode) {
                            OutlinedTextField(
                                value = displayName,
                                onValueChange = { displayName = it },
                                label = { Text("Display Name") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors()
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = false
                            },
                            label = { Text("Email") },
                            isError = emailError,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors()
                        )

                        if (emailError) {
                            Text(
                                text = "Enter a valid email",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = false
                            },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors()
                        )

                        if (passwordError) {
                            Text(
                                text = "Password can't be empty",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Login / Signup Button
                        Button(
                            onClick = {
                                emailError = email.isBlank()
                                passwordError = password.isBlank()

                                if (!emailError && !passwordError) {
                                    if (loginMode) {
                                        viewModel.login(email, password)
                                    } else {
                                        viewModel.register(email, password, displayName)
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Please fill in all fields")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            enabled = !state.isLoading,
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonRed)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text(
                                    text = if (loginMode) "Login" else "Sign Up",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Umschalten Login <-> SignUp
                        TextButton(onClick = { isLoginMode = !isLoginMode }) {
                            Text(
                                if (loginMode)
                                    "Don't have an account? Sign Up"
                                else
                                    "Already have an account? Log in",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// 🔧 Helper – vereinheitlicht TextField-Styling
@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color(0xFFDDDDDD),
    focusedContainerColor = Color.White.copy(alpha = 0.05f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.03f),
    focusedIndicatorColor = ButtonRed,
    unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f),
    cursorColor = ButtonRed,
    focusedLabelColor = ButtonRed,
    unfocusedLabelColor = Color(0xFFAAAAAA)
)
