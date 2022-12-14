package com.emlcoding.loginsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emlcoding.loginsample.ui.theme.LoginSampleTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen {
                Login()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Login() {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }
    var passVisible by remember { mutableStateOf(false) }
    
    val loginEnabled = user.isNotEmpty() && pass.isNotEmpty()
    val isError = validationMessage.isNotEmpty()

    val login = {
        validationMessage = validateLogin(user, pass)
    }

    // Para tener una animacion infinita con el color de fondo de los componentes de Login
    val transition = updateTransition(targetState = count, label = "update transition")
    val borderDp by transition.animateDp(label = "transition dp") {
        it.dp
    }
    val bgColor by transition.animateColor(label = "transition color") {
        Color.Red.copy(alpha = min(1f, it / 10f))
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .wrapContentSize()
                .background(bgColor)
                .border(borderDp, Color.Red)
                .padding(16.dp)
        ) {
            TextField(
                value = user,
                onValueChange = { user = it },
                isError = isError,
                label = { Text("User") },
                placeholder = { Text("Use your best email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                value = pass,
                onValueChange = { pass = it },
                isError = isError,
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions { login() },
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconToggleButton(checked = passVisible, onCheckedChange = { passVisible = it }) {
                        Crossfade(targetState = passVisible) { visible ->
                            if (visible) {
                                Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "Change password visibility")
                            } else {
                                Icon(imageVector = Icons.Default.Visibility, contentDescription = "Change password visibility")
                            }
                        }


                    }
                }
            )

            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    slideIntoContainer(Up) + fadeIn() with
                            slideOutOfContainer(Up) + fadeOut()
                }
            ) {
                Text(text = "Num of clicks: $it")
            }

            AnimatedVisibility(
                visible = validationMessage.isNotEmpty(),
                // Se a??ade el parametro initialOffsetX para que en vez de que aparezca el texto por la izquierda lo haga por la derecha
                enter = slideInHorizontally(initialOffsetX = { 2 * it })
            ) {
                Text(text = validationMessage, color = MaterialTheme.colors.error)
            }

            AnimatedVisibility(visible = loginEnabled) {
                Button(
                    onClick = {
                        login()
                        count++
                    }
                ) {
                    Text(text = "Login")
                }
            }
        }
    }

}

fun validateLogin(user: String, pass: String): String = when {
        !user.contains('@') -> "User must be a valid email"
        pass.length < 5 -> "Password must have at least 5 characters"
        else -> ""
}

@Composable
fun Screen(content: @Composable () -> Unit) {
    LoginSampleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginSampleTheme {
        Greeting("Android")
    }
}