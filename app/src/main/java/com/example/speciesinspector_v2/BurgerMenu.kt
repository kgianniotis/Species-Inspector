package com.example.speciesinspector_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class BurgerMenu {
    @Composable
    fun DrawerContent(
        drawerWidth: Dp,
        onClose: () -> Unit,
        onLogout: () -> Unit,
        onNavigateToMainMenu: () -> Unit,
        onNavigateToProfileSettings: () -> Unit,
        onNavigateToDonate: () -> Unit,
        onNavigateToGuides: () -> Unit,
        onNavigateToMyGroups: () -> Unit,
        onNavigateToUsefulKnowledge: () -> Unit,
        onNavigateToSpeciesList: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .width(drawerWidth)
                .fillMaxHeight()
                .background(Color(0xFF7AB97A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Main Menu",
                    modifier = Modifier
                        .clickable { onNavigateToMainMenu(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Profile Settings",
                    modifier = Modifier
                        .clickable { onNavigateToProfileSettings(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "My Groups",
                    modifier = Modifier
                        .clickable { onNavigateToMyGroups(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Invasive Species",
                    modifier = Modifier
                        .clickable { onNavigateToSpeciesList(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Application Guides",
                    modifier = Modifier
                        .clickable { onNavigateToGuides(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Useful Knowledge",
                    modifier = Modifier
                        .clickable { onNavigateToUsefulKnowledge(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Donate",
                    modifier = Modifier
                        .clickable { onNavigateToDonate(); onClose() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Logout",
                    modifier = Modifier
                        .clickable { onLogout() })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Close Menu",
                    modifier = Modifier
                        .clickable { onClose() })
            }
        }
    }
}
