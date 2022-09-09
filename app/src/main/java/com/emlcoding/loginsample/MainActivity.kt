package com.emlcoding.loginsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emlcoding.loginsample.ui.theme.LoginSampleTheme

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

@Composable
fun Login() {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    
    val loginEnabled = user.isNotEmpty() && pass.isNotEmpty()
    val isError = validationMessage.isNotEmpty()

    val login = { validationMessage = validateLogin(user, pass) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
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
                    Icon(imageVector = if(passVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = "Change password visibility")
                }
            }
        )
        AnimatedVisibility(
            visible = validationMessage.isNotEmpty(),
            // Se añade el parametro initialOffsetX para que en vez de que aparezca el texto por la izquierda lo haga por la derecha
            enter = slideInHorizontally(initialOffsetX = { 2 * it })
        ) {
            Text(text = validationMessage, color = MaterialTheme.colors.error)
        }

        AnimatedVisibility(visible = loginEnabled) {
            Button(
                onClick = login
            ) {
                Text(text = "Login")
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