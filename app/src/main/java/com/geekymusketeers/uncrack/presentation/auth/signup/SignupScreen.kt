package com.geekymusketeers.uncrack.presentation.auth.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.components.UCButton
import com.geekymusketeers.uncrack.components.UCTextField
import com.geekymusketeers.uncrack.data.remote.request.AuthResult
import com.geekymusketeers.uncrack.presentation.auth.AuthUIEvent
import com.geekymusketeers.uncrack.presentation.auth.AuthViewModel
import com.geekymusketeers.uncrack.ui.theme.DMSansFontFamily
import com.geekymusketeers.uncrack.ui.theme.OnPrimaryContainerLight
import com.geekymusketeers.uncrack.ui.theme.PrimaryLight
import com.geekymusketeers.uncrack.ui.theme.UnCrackTheme
import com.geekymusketeers.uncrack.ui.theme.medium16
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupScreen : ComponentActivity() {

    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.White.toArgb(), Color.White.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.White.toArgb(), Color.White.toArgb()
            )
        )
        super.onCreate(savedInstanceState)

        setContent {
            viewModel = hiltViewModel()
            UnCrackTheme {
                SignupContent(viewModel)
            }
        }
    }
}

@Composable
fun SignupContent(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    val state = viewModel.state
    val context = LocalContext.current
    var passwordVisibility by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {

                }

                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context, "Your are not authorized",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AuthResult.UnknownError -> {
                    Toast.makeText(
                        context, "Error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Sign In",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = DMSansFontFamily,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(60.dp))

            UCTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                headerText = stringResource(R.string.name_header),
                hintText = stringResource(R.string.name_hint),
                value = state.signUpName,
                onValueChange = {
                    viewModel.onEvent(AuthUIEvent.SignUpNameChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(30.dp))


            UCTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                headerText = stringResource(R.string.email_header),
                hintText = stringResource(R.string.email_hint),
                value = state.signUpEmail,
                onValueChange = {
                    viewModel.onEvent(AuthUIEvent.SignUpEmailChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            UCTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                headerText = stringResource(R.string.password_header),
                hintText = stringResource(R.string.password_hint),
                value = state.signUpPassword,
                onValueChange = {
                    viewModel.onEvent(AuthUIEvent.SignUpPasswordChanged(it))
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisibility)
                        painterResource(id = R.drawable.visibility_on)
                    else painterResource(id = R.drawable.visibility_off)

                    IconButton(onClick =
                    { passwordVisibility = passwordVisibility.not() }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = image,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            UCButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.register),
                onClick = {
                    viewModel.onEvent(AuthUIEvent.SignUp)
                },
                enabled = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.already_have_an_account),
                    style = medium16.copy(color = OnPrimaryContainerLight)
                )

                Text(
                    text = stringResource(id = R.string.login),
                    style = medium16.copy(color = PrimaryLight)
                )
            }
        }
    }
}