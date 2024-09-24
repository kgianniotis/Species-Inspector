package com.example.speciesinspector_v2

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TemplateScreen(
    topBarTitle: String,
    onMenuClick: () -> Unit,
    onActionButton: () -> Unit,
    actionText: String,
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit,
    content: @Composable () -> Unit
) {
    var drawerOpen by remember { mutableStateOf(false) }
    val drawerWidth = animateDpAsState(
        targetValue = if (drawerOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "Burger Menu"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column {

            TopBar(
                title = topBarTitle,
                onMenuClick = { drawerOpen = true },
                onActionButton = onActionButton,
                actionText = actionText
            )

            if (drawerOpen) {
                BurgerMenu().DrawerContent(
                    drawerWidth = drawerWidth.value,
                    onClose = { drawerOpen = false },
                    onLogout = onLogout,
                    onNavigateToMainMenu = onNavigateToMainMenu,
                    onNavigateToProfileSettings = onNavigateToProfileSettings,
                    onNavigateToDonate = onNavigateToDonate,
                    onNavigateToGuides = onNavigateToGuides,
                    onNavigateToMyGroups = onNavigateToMyGroups,
                    onNavigateToUsefulKnowledge = onNavigateToUsefulKnowledge,
                    onNavigateToSpeciesList = onNavigateToSpeciesList
                )
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}


@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit,
    onActionButton: () -> Unit,
    actionText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Yellow)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val image = painterResource(R.drawable.burger_menu)

        Image(
            painter = image,
            contentDescription = "Menu",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clickable { onMenuClick() }
                .padding(16.dp)
        )

        Text(
            text = title,
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .background(Color(0xFFFFA500))
                .padding(8.dp)
                .clickable(onClick = onActionButton)
        ) {
            Text(
                text = actionText,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}
