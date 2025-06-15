package com.aritradas.uncrack.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aritradas.uncrack.MainActivity
import com.aritradas.uncrack.R
import com.aritradas.uncrack.components.AutoLockTimeoutDialog
import com.aritradas.uncrack.components.SettingsItemGroup
import com.aritradas.uncrack.components.ThemeDialog
import com.aritradas.uncrack.components.UCSettingsCard
import com.aritradas.uncrack.components.UCSwitchCard
import com.aritradas.uncrack.navigation.Screen
import com.aritradas.uncrack.ui.theme.OnPrimaryContainerLight
import com.aritradas.uncrack.ui.theme.OnSurfaceVariantLight
import com.aritradas.uncrack.ui.theme.medium14
import com.aritradas.uncrack.ui.theme.medium18
import com.aritradas.uncrack.ui.theme.normal16
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritradas.uncrack.sharedViewModel.ThemeMode
import com.aritradas.uncrack.sharedViewModel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val isScreenshotEnabled by settingsViewModel.isScreenshotEnabled.observeAsState(false)
    val onLogOutComplete by settingsViewModel.onLogOutComplete.observeAsState(false)
    val onDeleteAccountComplete by settingsViewModel.onDeleteAccountComplete.observeAsState(false)
    val biometricAuthState by settingsViewModel.biometricAuthState.collectAsState()
    val autoLockEnabled by settingsViewModel.autoLockEnabled.collectAsState()
    val autoLockTimeout by settingsViewModel.autoLockTimeout.collectAsState()
    var openThemeDialog by remember { mutableStateOf(false) }
    var openLogoutDialog by remember { mutableStateOf(false) }
    var openDeleteAccountDialog by remember { mutableStateOf(false) }
    var openAutoLockTimeoutDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    if (onLogOutComplete || onDeleteAccountComplete) {
        navController.navigate(Screen.LoginScreen.name)
    }

    when {
        openThemeDialog -> {
            ThemeDialog(
                onDismissRequest = { openThemeDialog = false },
                onDarkMode = { themeViewModel.setThemeMode(ThemeMode.DARK) },
                onLightMode = { themeViewModel.setThemeMode(ThemeMode.LIGHT) },
                onDynamicMode = { themeViewModel.setThemeMode(ThemeMode.SYSTEM) }
            )
        }

        openAutoLockTimeoutDialog -> {
            AutoLockTimeoutDialog(
                timeoutOptions = settingsViewModel.autoLockTimeoutLabels,
                selectedIndex = settingsViewModel.getSelectedTimeoutIndex(),
                onTimeoutSelected = { index ->
                    settingsViewModel.setAutoLockTimeout(settingsViewModel.autoLockTimeoutOptions[index])
                },
                onDismissRequest = { openAutoLockTimeoutDialog = false }
            )
        }

        openLogoutDialog -> {
            AlertDialog(
                onDismissRequest = { openLogoutDialog = false },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = null,
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.logout),
                        style = normal16.copy(color = OnPrimaryContainerLight)
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.are_you_sure_you_want_to_logout),
                        style = normal16.copy(color = OnSurfaceVariantLight)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            settingsViewModel.logout()
                            openLogoutDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            "Logout",
                            style = medium14
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openLogoutDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = medium14
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            )
        }

        openDeleteAccountDialog -> {
            AlertDialog(
                onDismissRequest = { openDeleteAccountDialog = false },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = null,
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.delete_account),
                        style = normal16.copy(color = OnPrimaryContainerLight)
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.are_you_sure_you_want_to_delete),
                        style = normal16.copy(color = OnSurfaceVariantLight)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            settingsViewModel.deleteAccount()
                            openDeleteAccountDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            "Delete",
                            style = medium14
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDeleteAccountDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = medium14
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            )
        }
    }

    Scaffold(
        Modifier.fillMaxWidth(),
        topBar = {
            MediumTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 18.dp, top = 18.dp),
                text = stringResource(R.string.theme),
                style = medium18.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsItemGroup {
                UCSettingsCard(
                    itemName = stringResource(R.string.change_theme),
                    itemSubText = stringResource(R.string.cycle_through_light_dark_system),
                    onClick = { openThemeDialog = true }
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 18.dp, top = 18.dp),
                text = stringResource(id = R.string.security),
                style = medium18.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsItemGroup {
                UCSettingsCard(
                    itemName = stringResource(R.string.change_master_password),
                    itemSubText = stringResource(R.string.change_the_master_password_you_use_to_access_uncrack),
                    onClick = {
                        navController.navigate(Screen.UpdateMasterKeyScreen.name)
                    }
                )

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                )

                UCSwitchCard(
                    itemName = stringResource(R.string.biometric_unlock),
                    itemSubText = stringResource(R.string.use_biometric_to_unlock_the_app),
                    isChecked = biometricAuthState,
                    onChecked = {
                        settingsViewModel.showBiometricPrompt(context as MainActivity)
                    }
                )

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                )

                UCSwitchCard(
                    itemName = stringResource(R.string.auto_lock),
                    itemSubText = stringResource(R.string.automatically_lock_app_when_in_background),
                    isChecked = autoLockEnabled,
                    onChecked = {
                        settingsViewModel.setAutoLockEnabled(it)
                    }
                )

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                )

                UCSettingsCard(
                    itemName = stringResource(R.string.auto_lock_timeout),
                    itemSubText = stringResource(R.string.auto_lock_timeout_description_new).format(
                        settingsViewModel.getTimeoutLabelForValue(autoLockTimeout)
                    ),
                    onClick = {
                        openAutoLockTimeoutDialog = true
                    }
                )

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                )


                UCSwitchCard(
                    itemName = stringResource(R.string.allow_screenshots),
                    itemSubText = stringResource(R.string.allow_screenshots_not_recommended),
                    isChecked = isScreenshotEnabled,
                    onChecked = {
                        settingsViewModel.setScreenshotEnabled(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 18.dp, top = 18.dp),
                text = stringResource(R.string.danger_zone),
                style = medium18.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsItemGroup {
                UCSettingsCard(
                    itemName = stringResource(R.string.log_out),
                    textColor = Color.Red,
                    onClick = {
                        openLogoutDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            SettingsItemGroup {
                UCSettingsCard(
                    itemName = stringResource(R.string.delete_account),
                    textColor = Color.Red,
                    onClick = {
                        openDeleteAccountDialog = true
                    }
                )
            }
        }
    }
}