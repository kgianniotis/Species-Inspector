package com.example.speciesinspector_v2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import java.io.File

class Messages : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                MessagesScreen(
                    onLogout = { logoutAction.performLogout() },
                    onNavigateToMainMenu = { navigateToMainMenu() },
                    onNavigateToProfileSettings = { navigateToProfileSettings() },
                    onNavigateToDonate = { navigateToDonate() },
                    onNavigateToGuides = { navigateToGuides() },
                    onNavigateToMyGroups = { navigateToMyGroups() },
                    onNavigateToUsefulKnowledge = { navigateToUsefulKnowledge() },
                    onNavigateToSpeciesList = { navigateToSpeciesList() }
                )
            }
        }
    }



    private fun navigateToMainMenu() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }

    private fun navigateToProfileSettings() {
        val intent = Intent(this, ProfileSettings::class.java)
        startActivity(intent)
    }

    private fun navigateToDonate() {
        val intent = Intent(this, Donate::class.java)
        startActivity(intent)
    }

    private fun navigateToGuides() {
        val intent = Intent(this, Guides::class.java)
        startActivity(intent)
    }


    private fun navigateToMyGroups() {
        val intent = Intent(this, MyGroups::class.java)
        startActivity(intent)
    }

    private fun navigateToSpeciesList() {
        val intent = Intent(this, SpeciesList::class.java)
        startActivity(intent)
    }


    private fun navigateToUsefulKnowledge() {
        val intent = Intent(this, UsefulKnowledge::class.java)
        startActivity(intent)
    }
}


@Composable
fun MessagesScreen(
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit

) {
    var drawerOpen by remember { mutableStateOf(false) }
    val drawerWidth = animateDpAsState(targetValue = if (drawerOpen) 200.dp else 0.dp, animationSpec = tween(durationMillis = 300),
        label = "Burger Menu"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MessagesMainContent(
                    onMenuClick = { drawerOpen = true }
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

            }
        }
    }
}



@Composable
fun MessagesMainContent(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        Text(
            text = "Messages",
            fontSize = 50.sp,
            lineHeight = 50.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}


