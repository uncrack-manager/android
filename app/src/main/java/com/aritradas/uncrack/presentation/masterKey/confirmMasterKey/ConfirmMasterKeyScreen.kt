package com.aritradas.uncrack.presentation.masterKey.confirmMasterKey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aritradas.uncrack.R
import com.aritradas.uncrack.components.UCButton
import com.aritradas.uncrack.components.UCTextField
import com.aritradas.uncrack.navigation.Screen
import com.aritradas.uncrack.presentation.masterKey.KeyViewModel
import com.aritradas.uncrack.ui.theme.bold30
import kotlinx.coroutines.delay

@Composable
fun ConfirmMasterKeyScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    masterKeyViewModel: KeyViewModel = hiltViewModel(),
) {
    var confirmMasterKey by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val savedMasterKey = masterKeyViewModel.keyModel.password
    val context = LocalContext.current

    BackHandler(onBack = {
        (context as? android.app.Activity)?.finish()
    })

    LaunchedEffect(Unit) {
        masterKeyViewModel.getMasterKey()
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(2000)
            isLoading = false
            navController.navigate(Screen.VaultScreen.name)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = stringResource(R.string.kindly_provide_your_master_password),
                    style = bold30.copy(color = MaterialTheme.colorScheme.onBackground)
                )

                Spacer(modifier = Modifier.height(50.dp))

                UCTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    headerText = stringResource(id = R.string.master_password),
                    hintText = stringResource(id = R.string.password_hint),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    value = confirmMasterKey,
                    onValueChange = { confirmMasterKey = it },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {

                        val image = if (passwordVisibility)
                            painterResource(id = R.drawable.visibility_off)
                        else painterResource(id = R.drawable.visibility_on)

                        val imageDescription =
                            if (passwordVisibility) stringResource(R.string.show_password) else stringResource(
                                R.string.hide_password
                            )

                        IconButton(onClick = { passwordVisibility = passwordVisibility.not() }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = image,
                                contentDescription = imageDescription
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

            UCButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.unlock_uncrack),
                isLoading = isLoading,
                loadingText = "Unlocking...",
                onClick = {
                    if (savedMasterKey == confirmMasterKey) {
                        isLoading = true
                    }
                },
                enabled = savedMasterKey == confirmMasterKey
            )
        }
    }
}