package com.example.beyondlimits.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.theme.ButtonRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
) {
    var isLoginMode by remember { mutableStateOf(true) }
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onAuthSuccess()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = isLoginMode,
                label = "AuthModeTransition",
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) + fadeIn(animationSpec = tween(500)) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(500),
                                    targetOffsetX = { fullWidth -> -fullWidth }
                                ) + fadeOut(animationSpec = tween(500))
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeIn(animationSpec = tween(500)) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(500),
                                    targetOffsetX = { fullWidth -> fullWidth }
                                ) + fadeOut(animationSpec = tween(500))
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { loginMode ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (loginMode) "Login" else "Sign Up",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        label = { Text("Email", fontWeight = FontWeight.Bold) },
                        isError = emailError,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            focusedIndicatorColor = ButtonRed,
                            unfocusedIndicatorColor = ButtonRed,
                            focusedLabelColor = ButtonRed,
                            unfocusedLabelColor = ButtonRed,
                            cursorColor = ButtonRed,
                            errorIndicatorColor = MaterialTheme.colorScheme.error
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                        },
                        label = { Text("Password", fontWeight = FontWeight.Bold) },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            focusedIndicatorColor = ButtonRed,
                            unfocusedIndicatorColor = ButtonRed,
                            focusedLabelColor = ButtonRed,
                            unfocusedLabelColor = ButtonRed,
                            cursorColor = ButtonRed,
                            errorIndicatorColor = MaterialTheme.colorScheme.error
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            emailError = email.isBlank()
                            passwordError = password.isBlank()

                            if (!emailError && !passwordError) {
                                if (loginMode) viewModel.login(email, password)
                                else viewModel.register(email, password)
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Please fill in all the fields")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonRed)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                        Text(if (loginMode) "Login" else "Sign Up")
                    }

                    TextButton(onClick = { isLoginMode = !isLoginMode }) {
                        Text(
                            if (loginMode)
                                "Don't have an account? Sign Up"
                            else
                                "Already have an account? Log in",
                            color = ButtonRed
                        )
                    }
                }
            }
        }
    }
}

