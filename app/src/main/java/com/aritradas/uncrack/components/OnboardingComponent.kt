package com.aritradas.uncrack.components

import androidx.compose.runtime.Composable
import com.aritradas.uncrack.presentation.intro.model.OnBoardingItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aritradas.uncrack.ui.theme.bold22
import com.aritradas.uncrack.ui.theme.medium16

@Composable
fun OnboardingComponent(item: OnBoardingItem, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = item.image),
            contentDescription = "onboard image",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        Spacer(modifier = Modifier.padding(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = item.title,
            style = bold22.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = item.text,
            textAlign = TextAlign.Center,
            style = medium16.copy(color = MaterialTheme.colorScheme.surfaceTint)
        )
    }
}