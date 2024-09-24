package com.example.speciesinspector_v2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class InvasivesCrete : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contextCheck = LocalContext.current
            var showNewEntryScreenCrete by remember { mutableStateOf(false) }
            var invasivesList by remember { mutableStateOf<List<InvasiveSpecies>>(emptyList()) }
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
            var userRole by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                fetchInvasives { invasives ->
                    invasivesList = invasives
                }
            }

            //For finding the role of the current user
            LaunchedEffect(Unit) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    FirebaseFirestore.getInstance().collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            userRole = document.getString("role")
                        }
                }
            }


            val selectImageLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) {
                    uri: Uri? ->
                if (uri != null) {
                    selectedImageUri = uri
                    showNewEntryScreenCrete = true
                }
            }

            SpeciesInspector_V2Theme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF228B22))
                ){
                    if (showNewEntryScreenCrete) {
                        NewEntryScreenCrete(
                            selectedImageUri = selectedImageUri,
                            onNewEntrySubmitCrete = { uri, scientificName, origin, categorization, dateAppeared, entryStatus, population, information ->
                                handleImageSelectedCrete(
                                    uri = uri,
                                    scientificName = scientificName,
                                    origin = origin,
                                    categorization = categorization,
                                    dateAppeared = dateAppeared,
                                    entryStatus = entryStatus,
                                    population = population,
                                    information = information
                                )
                                showNewEntryScreenCrete = false
                            },
                            onAbortUpload = { invasivesCrete() }
                        )
                    }else{
                        InvasivesCreteScreen(
                            invasivesList = invasivesList,
                            userRole = userRole,
                            onLogout = { logoutAction.performLogout() },
                            onNavigateToMainMenu = { navigateToMainMenu() },
                            onNavigateToProfileSettings = { navigateToProfileSettings() },
                            onNavigateToDonate = { navigateToDonate() },
                            onNavigateToGuides = { navigateToGuides() },
                            onNavigateToMyGroups = { navigateToMyGroups() },
                            onNavigateToUsefulKnowledge = { navigateToUsefulKnowledge() },
                            onNavigateToSpeciesList = { navigateToSpeciesList() },
                            onAddNewEntry =  {
                                if (userRole == "Admin") {
                                    selectImageLauncher.launch("image/*")
                                } else {
                                    Toast.makeText(contextCheck, "Only Admins can create new Entries.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }


    private fun fetchInvasives(onResult: (List<InvasiveSpecies>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("invasive_species")
            .document("region_lists")
            .collection("Crete")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { document ->
                    document.toObject(InvasiveSpecies::class.java).copy(documentId = document.id)
                }
                onResult(list)
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

    private fun invasivesCrete() {
        val intent = Intent(this, InvasivesCrete::class.java)
        startActivity(intent)
    }


    private fun uploadImageCrete(uri: Uri, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    onSuccess(downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }


    private fun handleImageSelectedCrete(
        uri: Uri,
        scientificName: String,
        origin: String,
        categorization: String,
        dateAppeared: String,
        entryStatus: String,
        population: String,
        information: String
    ) {
        uploadImageCrete(uri,
            onSuccess = { url ->
                saveNewEntryToFirestoreCrete(
                    imageUrl = url,
                    scientificName = scientificName,
                    origin = origin,
                    categorization = categorization,
                    dateAppeared = dateAppeared,
                    entryStatus = entryStatus,
                    population = population,
                    information = information
                )

                Toast.makeText(this, "Entry successfully uploaded!", Toast.LENGTH_SHORT).show()

            },
            onFailure = {
                Toast.makeText(this, "New Entry upload failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

}

@Composable
fun InvasivesCreteScreen(
    invasivesList: List<InvasiveSpecies>,
    userRole: String?,
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit,
    onAddNewEntry: () -> Unit

) {

    TemplateScreen(
        topBarTitle = "Crete",
        onMenuClick = {},
        onLogout = onLogout,
        onActionButton = onAddNewEntry,
        actionText = "Add New Entry",
        onNavigateToMainMenu = onNavigateToMainMenu,
        onNavigateToProfileSettings = onNavigateToProfileSettings,
        onNavigateToDonate = onNavigateToDonate,
        onNavigateToGuides = onNavigateToGuides,
        onNavigateToMyGroups = onNavigateToMyGroups,
        onNavigateToUsefulKnowledge = onNavigateToUsefulKnowledge,
        onNavigateToSpeciesList = onNavigateToSpeciesList
    ) {
        InvasivesCreteMainContent(
            invasivesList,
            userRole = userRole
        )
    }
}

@Composable
fun InvasivesCreteMainContent(
    invasivesList: List<InvasiveSpecies>,
    userRole: String?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        invasivesList.forEach { invasive ->
            InvasiveItemCrete(
                invasive = invasive,
                userRole = userRole
            )
        }
    }
}

@Composable
fun InvasiveItemCrete(
    invasive: InvasiveSpecies,
    userRole: String?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(invasive.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Scientific Name: ${invasive.scientificName}")
        Text(text = "Origin: ${invasive.origin}")
        Text(text = "Categorization: ${invasive.categorization}")
        Text(text = "Date Appeared: ${invasive.dateAppeared}")
        Text(text = "Population: ${invasive.population}")
        Text(text = "Entry Status: ${invasive.entryStatus}")
        Text(text = "Information: ${invasive.information}")
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {

                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if(userRole == "Admin"){
                                showDialog = true
                            } else {
                                Toast.makeText(context, "Only Admins can delete photos.", Toast.LENGTH_SHORT).show()
                            }

                        }
                ) {
                    Text(text = "Delete Photo")
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Delete Photo") },
                        text = { Text("Are you sure you want to delete this photo?") },
                        confirmButton = {
                            TextButton(onClick = {
                                deleteInvasiveSpeciesEntryCrete(
                                    invasive.imageUrl,
                                    invasive.documentId,
                                    context
                                )
                                showDialog = false
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }

            }
        }
    }
}

fun deleteInvasiveSpeciesEntryCrete(
    imageUrl: String,
    documentId: String,
    context: Context
) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
    storageReference.delete()
        .addOnSuccessListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("invasive_species")
                .document("region_lists")
                .collection("Crete")
                .document(documentId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Entry deleted successfully.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error deleting entry!", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Unknown system error!", Toast.LENGTH_SHORT).show()
        }
}

fun saveNewEntryToFirestoreCrete(
    imageUrl: String,
    scientificName: String,
    origin: String,
    dateAppeared: String,
    categorization: String,
    entryStatus: String,
    population: String,
    information: String
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (currentUser != null) {
        val imageInfo = hashMapOf(
            "imageUrl" to imageUrl,
            "scientificName" to scientificName,
            "origin" to origin,
            "dateAppeared" to dateAppeared,
            "categorization" to categorization,
            "entryStatus" to entryStatus,
            "population" to population,
            "information" to information
        )

        db.collection("invasive_species")
            .document("region_lists")
            .collection("Crete")
            .add(imageInfo)

    }
}


@Composable
fun NewEntryScreenCrete(
    selectedImageUri: Uri?,
    onAbortUpload: () -> Unit,
    onNewEntrySubmitCrete: (Uri, String, String, String, String, String, String, String) -> Unit

){
    var scientificName by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("") }
    var categorization by remember { mutableStateOf("") }
    var dateAppeared by remember { mutableStateOf("") }
    var population by remember { mutableStateOf("") }
    var entryStatus by remember { mutableStateOf("") }
    var information by remember { mutableStateOf("") }

    val dateRegex = Regex("""\d{2}/\d{2}/\d{4}""")
    val dateContext = LocalContext.current
    var dateError by remember { mutableStateOf(false) }

    val informationMaxLength = 150
    val informationContext = LocalContext.current

    val fullContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Enter New Entry Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = scientificName,
            onValueChange = {
                scientificName = it
            },
            label = { Text("Scientific Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = origin,
            onValueChange = {
                origin = it
            },
            label = { Text("Origin") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categorization,
            onValueChange = {
                categorization = it
            },
            label = { Text("Categorization") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dateAppeared,
            onValueChange = {
                dateAppeared = it
                dateError = !dateAppeared.matches(dateRegex)
            },
            label = { Text("Date Appeared in New Habitat (dd/mm/yyyy)") },
            modifier = Modifier.fillMaxWidth(),
            isError = dateError
        )
        if (dateError) {
            Toast.makeText(dateContext, "Wrong Date Format", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = population,
            onValueChange = {
                population = it
            },
            label = { Text("Population") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = entryStatus,
            onValueChange = {
                entryStatus = it
            },
            label = { Text("Entry Status") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = information,
            onValueChange = {
                if (it.length <= informationMaxLength) information = it
                else Toast.makeText(informationContext, "Cannot be more than 150 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Information") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFA500))
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (selectedImageUri != null) {
                                if (
                                    scientificName.isNotEmpty() &&
                                    dateAppeared.isNotEmpty() &&
                                    entryStatus.isNotEmpty() &&
                                    population.isNotEmpty() &&
                                    categorization.isNotEmpty() &&
                                    information.isNotEmpty() &&
                                    origin.isNotEmpty()
                                ) {
                                    onNewEntrySubmitCrete(
                                        selectedImageUri,
                                        scientificName,
                                        origin,
                                        categorization,
                                        dateAppeared,
                                        entryStatus,
                                        population,
                                        information
                                    )
                                } else {
                                    Toast.makeText(fullContext, "All Text fields must be filled.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(fullContext, "Please select an image.", Toast.LENGTH_SHORT).show()
                            }
                        }
                ) {
                    Text(
                        text = "Confirm Entry",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onAbortUpload)
                ) {
                    Text(
                        text = "Abort Upload",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
    }

}

