package com.aritradas.uncrack.presentation.tools.generator

import androidx.compose.material3.SegmentedButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aritradas.uncrack.R
import com.aritradas.uncrack.components.UCTopAppBar
import com.aritradas.uncrack.presentation.tools.usernameGenerator.UsernameGenerator
import com.aritradas.uncrack.presentation.tools.passwordGenerator.PasswordGenerator
import com.aritradas.uncrack.presentation.tools.passwordGenerator.PasswordGeneratorViewModel
import com.aritradas.uncrack.presentation.tools.usernameGenerator.UsernameGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(
    navController: NavHostController,
    passwordGeneratorViewModel: PasswordGeneratorViewModel,
    usernameGeneratorViewModel: UsernameGeneratorViewModel,
    modifier: Modifier = Modifier,
    initialTab: String = "password"
) {
    var selectedTab by remember { mutableStateOf(
        if (initialTab.equals("username", true)) "Username" else "Password")
    }

    Scaffold(
        topBar = {
            UCTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.generator),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                onBackPress = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                SegmentedButton(
                    selected = selectedTab == "Password",
                    onClick = { selectedTab = "Password" },
                    shape = RoundedCornerShape(
                        topStart = 16.dp, bottomStart = 16.dp, topEnd = 0.dp, bottomEnd = 0.dp
                    ),
                    label = { Text("Password") }
                )
                SegmentedButton(
                    selected = selectedTab == "Username",
                    onClick = { selectedTab = "Username" },
                    shape = RoundedCornerShape(
                        topStart = 0.dp, bottomStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp
                    ),
                    label = { Text("Username") }
                )
            }

            // Change the content based on the selected tab
            when (selectedTab) {
                "Password" -> PasswordGenerator(
                    passwordGeneratorViewModel = passwordGeneratorViewModel,
                    modifier = Modifier.weight(1f)
                )

                "Username" -> UsernameGenerator(
                    usernameGeneratorViewModel = usernameGeneratorViewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}