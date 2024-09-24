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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme

class SpeciesList : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                SpeciesListScreen(
                    onLogout = { logoutAction.performLogout() },
                    onNavigateToMainMenu = { navigateToMainMenu() },
                    onNavigateToProfileSettings = { navigateToProfileSettings() },
                    onNavigateToDonate = { navigateToDonate() },
                    onNavigateToGuides = { navigateToGuides() },
                    onNavigateToMyGroups = { navigateToMyGroups() },
                    onNavigateToUsefulKnowledge = { navigateToUsefulKnowledge() },
                    onNavigateToSpeciesList = { navigateToSpeciesList() },
                    onInvasivesAttica = { invasivesAttica() },
                    onInvasivesCentralGreece = { invasivesCentralGreece() },
                    onInvasivesCentralMacedonia = { invasivesCentralMacedonia() },
                    onInvasivesCrete = { invasivesCrete() },
                    onInvasivesEasternMacedoniaThrace = { invasivesEasternMacedoniaThrace() },
                    onInvasivesEpirus = { invasivesEpirus() },
                    onInvasivesIonianIslands = { invasivesIonianIslands() },
                    onInvasivesNorthAegean = { invasivesNorthAegean() },
                    onInvasivesPeloponnese = { invasivesPeloponnese() },
                    onInvasivesSouthAegean = { invasivesSouthAegean() },
                    onInvasivesThessaly = { invasivesThessaly() },
                    onInvasivesWesternGreece = { invasivesWesternGreece() },
                    onInvasivesWesternMacedonia = { invasivesWesternMacedonia() }

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

    private fun invasivesAttica() {
        val intent = Intent(this, InvasivesAttica::class.java)
        startActivity(intent)
    }

    private fun invasivesCentralGreece() {
        val intent = Intent(this, InvasivesCentralGreece::class.java)
        startActivity(intent)
    }

    private fun invasivesCentralMacedonia() {
        val intent = Intent(this, InvasivesCentralMacedonia::class.java)
        startActivity(intent)
    }

    private fun invasivesCrete() {
        val intent = Intent(this, InvasivesCrete::class.java)
        startActivity(intent)
    }

    private fun invasivesEasternMacedoniaThrace() {
        val intent = Intent(this, InvasivesEasternMacedoniaThrace::class.java)
        startActivity(intent)
    }
    private fun invasivesEpirus() {
        val intent = Intent(this, InvasivesEpirus::class.java)
        startActivity(intent)
    }
    private fun invasivesIonianIslands() {
        val intent = Intent(this, InvasivesIonianIslands::class.java)
        startActivity(intent)
    }
    private fun invasivesNorthAegean() {
        val intent = Intent(this, InvasivesNorthAegean::class.java)
        startActivity(intent)
    }
    private fun invasivesPeloponnese() {
        val intent = Intent(this, InvasivesPeloponnese::class.java)
        startActivity(intent)
    }
    private fun invasivesSouthAegean() {
        val intent = Intent(this, InvasivesSouthAegean::class.java)
        startActivity(intent)
    }
    private fun invasivesThessaly() {
        val intent = Intent(this, InvasivesThessaly::class.java)
        startActivity(intent)
    }
    private fun invasivesWesternGreece() {
        val intent = Intent(this, InvasivesWesternGreece::class.java)
        startActivity(intent)
    }
    private fun invasivesWesternMacedonia() {
        val intent = Intent(this, InvasivesWesternMacedonia::class.java)
        startActivity(intent)
    }


}


@Composable
fun SpeciesListScreen(
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit,
    onInvasivesAttica: () -> Unit,
    onInvasivesCentralGreece: () -> Unit,
    onInvasivesCentralMacedonia: () -> Unit,
    onInvasivesCrete: () -> Unit,
    onInvasivesEasternMacedoniaThrace: () -> Unit,
    onInvasivesEpirus: () -> Unit,
    onInvasivesIonianIslands: () -> Unit,
    onInvasivesNorthAegean: () -> Unit,
    onInvasivesPeloponnese: () -> Unit,
    onInvasivesSouthAegean: () -> Unit,
    onInvasivesThessaly: () -> Unit,
    onInvasivesWesternGreece: () -> Unit,
    onInvasivesWesternMacedonia: () -> Unit

) {

    topBarTemplateSimpleScreen(
        topBarTitle = "Invasive Species",
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
        SpeciesListMainContent(
            onInvasivesAttica = onInvasivesAttica,
            onInvasivesCentralGreece = onInvasivesCentralGreece,
            onInvasivesCentralMacedonia =  onInvasivesCentralMacedonia,
            onInvasivesCrete =  onInvasivesCrete,
            onInvasivesEasternMacedoniaThrace =  onInvasivesEasternMacedoniaThrace,
            onInvasivesEpirus =  onInvasivesEpirus,
            onInvasivesIonianIslands =  onInvasivesIonianIslands,
            onInvasivesNorthAegean =  onInvasivesNorthAegean,
            onInvasivesPeloponnese =  onInvasivesPeloponnese,
            onInvasivesSouthAegean =  onInvasivesSouthAegean,
            onInvasivesThessaly =  onInvasivesThessaly,
            onInvasivesWesternGreece =  onInvasivesWesternGreece,
            onInvasivesWesternMacedonia =  onInvasivesWesternMacedonia
        )
    }
}


@Composable
fun SpeciesListMainContent(
    modifier: Modifier = Modifier,
    onInvasivesAttica: () -> Unit,
    onInvasivesCentralGreece: () -> Unit,
    onInvasivesCentralMacedonia: () -> Unit,
    onInvasivesCrete: () -> Unit,
    onInvasivesEasternMacedoniaThrace: () -> Unit,
    onInvasivesEpirus: () -> Unit,
    onInvasivesIonianIslands: () -> Unit,
    onInvasivesNorthAegean: () -> Unit,
    onInvasivesPeloponnese: () -> Unit,
    onInvasivesSouthAegean: () -> Unit,
    onInvasivesThessaly: () -> Unit,
    onInvasivesWesternGreece: () -> Unit,
    onInvasivesWesternMacedonia: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Invasive Species per Region",
                fontSize = 50.sp,
                lineHeight = 50.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = modifier
                .fillMaxSize()
        ){
            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesAttica)
                ){
                    Text(
                        text = "Attica",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesCentralGreece)
                ){
                    Text(
                        text = "Central Greece",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesCentralMacedonia)
                ){
                    Text(
                        text = "Central Macedonia",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesCrete)
                ){
                    Text(
                        text = "Crete",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesEasternMacedoniaThrace)
                ){
                    Text(
                        text = "East Macedonia",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesEpirus)
                ){
                    Text(
                        text = "Epirus",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesIonianIslands)
                ){
                    Text(
                        text = "Ionian Islands",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesNorthAegean)
                ){
                    Text(
                        text = "North Aegean",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesPeloponnese)
                ){
                    Text(
                        text = "Peloponnese",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesSouthAegean)
                ){
                    Text(
                        text = "South Aegean",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesThessaly)
                ){
                    Text(
                        text = "Thessaly",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesWesternGreece)
                ){
                    Text(
                        text = "Western Greece",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFff5c33))
                        .border(1.dp, Color.Black)
                        .padding(20.dp)
                        .clickable(onClick = onInvasivesWesternMacedonia)
                ){
                    Text(
                        text = "Western Macedonia",
                        fontSize = 15.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }


        }
    }
}



