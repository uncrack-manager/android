package com.aritradas.uncrack.presentation.auth.signup

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aritradas.uncrack.R
import com.aritradas.uncrack.components.NoInternetScreen
import com.aritradas.uncrack.components.UCButton
import com.aritradas.uncrack.components.UCTextField
import com.aritradas.uncrack.navigation.Screen
import com.aritradas.uncrack.presentation.auth.AuthViewModel
import com.aritradas.uncrack.ui.theme.DMSansFontFamily
import com.aritradas.uncrack.ui.theme.medium16
import com.aritradas.uncrack.util.ConnectivityObserver
import com.aritradas.uncrack.util.Validator.Companion.isValidEmail
import com.aritradas.uncrack.util.Validator.Companion.isValidName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    connectivityObserver: ConnectivityObserver,
    modifier: Modifier = Modifier,
    onSignUp: (FirebaseUser) -> Unit
) {

    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val auth: FirebaseAuth = Firebase.auth
    var newUser by remember { mutableStateOf(auth.currentUser) }
    val isRegisterButtonEnable by remember {
        derivedStateOf {
            userName.isValidName() && userEmail.isValidEmail()
        }
    }
    val errorLiveData by authViewModel.errorLiveData.observeAsState()
    val registerStatus by authViewModel.registerStatus.observeAsState(false)
    var isLoading by remember { mutableStateOf(false) }
    var networkStatus by remember { mutableStateOf(ConnectivityObserver.Status.Unavailable) }

    LaunchedEffect(true) {
        connectivityObserver.observe().collectLatest { status ->
            networkStatus = status
        }
    }

    LaunchedEffect(registerStatus) {
        if (registerStatus) {
            isLoading = false
            Toast.makeText(
                context,
                context.getString(R.string.account_created_successfully), Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(errorLiveData) {
        errorLiveData?.let { error ->
            isLoading = false
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    BackHandler(onBack = {
        (context as? android.app.Activity)?.finish()
    })

    when(networkStatus) {
        ConnectivityObserver.Status.Available -> {
            Scaffold(
                modifier = modifier.fillMaxSize()
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sign_up),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = DMSansFontFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    UCTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        headerText = stringResource(R.string.name_header),
                        hintText = stringResource(R.string.name_hint),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        value = userName,
                        onValueChange = { userName = it }
                    )

                    Spacer(modifier = Modifier.height(30.dp))


                    UCTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        headerText = stringResource(R.string.email_header),
                        hintText = stringResource(R.string.email_hint),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        value = userEmail,
                        onValueChange = { userEmail = it }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    UCTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        headerText = stringResource(R.string.password_header),
                        hintText = stringResource(R.string.password_hint),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        value = userPassword,
                        onValueChange = { userPassword = it },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisibility)
                                painterResource(id = R.drawable.visibility_off)
                            else painterResource(id = R.drawable.visibility_on)

                            val imageDescription =
                                if (passwordVisibility) stringResource(R.string.show_password) else stringResource(
                                    R.string.hide_password
                                )

                            IconButton(onClick =
                            { passwordVisibility = passwordVisibility.not() }
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = image,
                                    contentDescription = imageDescription
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    UCButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.register),
                        isLoading = isLoading,
                        loadingText = "Creating your account",
                        onClick = {
                            isLoading = true
                            authViewModel.signUp(
                                userName,
                                userEmail,
                                userPassword,
                                onSignedUp = { signUpUser ->
                                    newUser = signUpUser
                                    onSignUp(signUpUser)
                                }
                            )
                        },
                        enabled = isRegisterButtonEnable
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.already_have_an_account),
                            style = medium16.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.LoginScreen.name)
                            },
                            text = stringResource(id = R.string.login),
                            style = medium16.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
        else -> {
            NoInternetScreen()
        }
    }

}