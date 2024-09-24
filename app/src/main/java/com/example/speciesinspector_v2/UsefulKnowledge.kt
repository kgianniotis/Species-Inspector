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

class UsefulKnowledge : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                UsefulKnowledgeScreen(
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
fun UsefulKnowledgeScreen(
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
        topBarTitle = "Useful knowledge",
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
        UsefulKnowledgeMainContent()
    }
}



@Composable
fun UsefulKnowledgeMainContent(
    modifier: Modifier = Modifier
) {

    var invasivesExplanation by remember { mutableStateOf(false) }
    var invasivesThreatExplanation by remember { mutableStateOf(false) }
    var safetyExplanation by remember { mutableStateOf(false) }

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
                    .clickable { invasivesExplanation = !invasivesExplanation }
            ){
                Text(
                    text = "What Are Invasive Species",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( invasivesExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){

                    Text(
                        text = "Invasive Species are deemed those species that originate " +
                                "from a different environment from the one in which they are observed. They may have arrived in this new" +
                                "environment by a plethora of ways, human activities or destruction of their native habitat.",
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )


                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { invasivesThreatExplanation = !invasivesThreatExplanation }
            ){
                Text(
                    text = "What Dangers Do Invasive Species possess?",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( invasivesThreatExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){

                    Text(
                        text = "Invasive species are extremely dangerous to the new environments they appear in," +
                                "as they often lack natural predators and can evolve to be highly competent in their new habitats." +
                                "Due to this reason, they can often drive native species to extinction and throw out of balance the biodiversity" +
                                "of the whole environment. Moreover, new species may be carriers of diseases that while may not be deadly to themselves," +
                                "they can infect other species, even humans.",
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFffff66))
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
                    .clickable { safetyExplanation = !safetyExplanation }
            ){
                Text(
                    text = "Staying Safe In The Wild",
                    fontSize = 25.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if( safetyExplanation ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ){

                    Text(
                        text = "When taking photos in the wild, it is important to not forsake your safety and your health." +
                                "Always keep safe distances from wild animals and refrain from touching wild animals and plants," +
                                "as they can be poisonous. Furthermore, when going in the wild, make sure to have with you enough water" +
                                "and dry food. In addition, make sure to take precautions for heat and insects, like applying apple sunscreen" +
                                "and covering your limbs and neck.",
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


