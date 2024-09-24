package com.example.speciesinspector_v2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme

class Guides : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                GuidesScreen(
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
fun GuidesScreen(
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit

) {
    topBarTemplateSimpleScreen(
        topBarTitle = "Application Guides",
        onMenuClick = {},
        onLogout = onLogout,
        onNavigateToMainMenu = onNavigateToMainMenu,
        onNavigateToProfileSettings = onNavigateToProfileSettings,
        onNavigateToDonate = onNavigateToDonate,
        onNavigateToGuides = onNavigateToGuides,
        onNavigateToMyGroups = onNavigateToMyGroups,
        onNavigateToUsefulKnowledge = onNavigateToUsefulKnowledge,
        onNavigateToSpeciesList = onNavigateToSpeciesList
    ) {
        GuidesMainContent()
    }
}



@Composable
fun GuidesMainContent(
    modifier: Modifier = Modifier
) {
    var regionsExplanation by remember { mutableStateOf(false) }
    var categoriesExplanation by remember { mutableStateOf(false) }
    var photosExplanation by remember { mutableStateOf(false) }
    var speciesListExplanation by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { regionsExplanation = !regionsExplanation }
            ){
                Text(
                    text = "Selecting Regions",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( regionsExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){
                    Column {
                        Text(
                            text = "When selecting regions for photos, the available " +
                                    "ones correspond to the current administrative divisions of Greece:",
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Column(modifier = Modifier.padding(bottom = 4.dp)) {
                            BulletPointText("Attica")
                            BulletPointText("Central Greece")
                            BulletPointText("Central Macedonia")
                            BulletPointText("Crete")
                            BulletPointText("Eastern Macedonia and Thrace")
                            BulletPointText("Epirus")
                            BulletPointText("Ionian Islands")
                            BulletPointText("North Aegean")
                            BulletPointText("Peloponnese")
                            BulletPointText("South Aegean")
                            BulletPointText("Thessaly")
                            BulletPointText("Western Greece")
                            BulletPointText("Western Macedonia")
                        }
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { categoriesExplanation = !categoriesExplanation }
            ){
                Text(
                    text = "Selecting Species Categories",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( categoriesExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){
                    Column {
                        Text(
                            text = "When selecting categories for when uploading photos, the available ones are:",
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Column(modifier = Modifier.padding(bottom = 4.dp)) {
                            BulletPointText("Mammal")
                            BulletPointText("Fish")
                            BulletPointText("Amphibian")
                            BulletPointText("Reptile")
                            BulletPointText("Insect")
                            BulletPointText("Arthropod")
                            BulletPointText("Bird")
                            BulletPointText("Mollusk")
                            BulletPointText("Plant")
                            BulletPointText("Fungus")
                        }
                    }

                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { photosExplanation = !photosExplanation }
            ){
                Text(
                    text = "Taking Good Photos",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( photosExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){
                    Column {
                        Text(
                            text = "When taking photos of a species, make sure to follow these steps to maximize the quality of your photos:",
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                        ) {
                            BulletPointText("Have the target centered.")
                            BulletPointText("Make sure the surroundings are well lit.")
                            BulletPointText("Try to zoom if the target is very far from you.")
                        }
                    }

                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { speciesListExplanation = !speciesListExplanation }
            ){
                Text(
                    text = "Checking The Invasive Species Lists",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( speciesListExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){
                    Column {
                        Text(
                            text = "Make sure to often check out the lists with invasive species per region. These lists are often updated" +
                                    "with new species or remove older ones that are no longer deemed a threat.",
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "IMPORTANT!!!",
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Text(
                            text = "Only Admins can enter and new invasive species to the lists.",
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                    }

                }
            }

        }

    }
}

@Composable
fun BulletPointText(text: String) {
    Row {
        Text(
            text = "• ",
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = text,
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Start
        )
    }
}



