package com.aritradas.uncrack.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aritradas.uncrack.R
import com.aritradas.uncrack.sharedViewModel.UserViewModel
import com.aritradas.uncrack.ui.theme.bold24

@Composable
fun ProfileContainer(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    var initials by remember { mutableStateOf("") }
    val userData by userViewModel.state.collectAsState()

    initials = getInitials(userData.name)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if (initials.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.no_user_profile_picture),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = initials,
                style = bold24.copy(color = MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

fun getInitials(name: String): String {
    val nameParts = name.trim().split(' ')
    var initials = nameParts[0].first().uppercaseChar().toString()
    if (nameParts.size > 1) {
        initials += nameParts[1].first()
    } else if (nameParts[0].length > 1) {
        initials += nameParts[0][1]
    }
    return initials
}