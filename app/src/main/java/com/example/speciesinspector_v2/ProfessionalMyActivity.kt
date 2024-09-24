package com.example.speciesinspector_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.compose.ui.platform.LocalContext
import android.content.Context

class ProfessionalMyActivity : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch username from Firestore
        val currentUser = FirebaseAuth.getInstance().currentUser
        var username by mutableStateOf("")

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        username = document.getString("username") ?: ""
                    }
                }

        }

        setContent {
            SpeciesInspector_V2Theme {
                ProfessionalMyActivityScreen(
                    username = username,
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

fun deleteImageFromDatabase(
    imageUrl: String,
    documentId: String,
    context: Context
) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
    storageReference.delete()
        .addOnSuccessListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("validated_images").document(documentId).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Image safely deleted.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error deleting image.", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error deleting image.", Toast.LENGTH_SHORT).show()
        }
}

@Composable
fun ProfessionalMyActivityScreen(
    username: String,
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
    val drawerWidth = animateDpAsState(
        targetValue = if (drawerOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "Burger Menu"
    )

    var filterOpen by remember { mutableStateOf(false) }
    val filterWidth by animateDpAsState(
        targetValue = if (filterOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300), label = "Filter Menu"
    )
    var selectedRegion by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedUsername by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column {
            ProfessionalMyActivityTopBar(
                onMenuClick = { drawerOpen = true },
                onFiltering = { filterOpen = true }
            )
            if(filterOpen){
                FilterMenu().DrawerContent(
                    filterWidth = filterWidth,
                    onCloseFilters = { filterOpen = false },
                    selectedRegion = selectedRegion,
                    selectedCategory = selectedCategory,
                    selectedUsername = selectedUsername,
                    onFiltersApplied = { region, category, username ->
                        selectedRegion = region
                        selectedCategory = category
                        selectedUsername = username
                    }
                )
            }
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
                ProfessionalMyActivityMainContent(
                    username = username,
                    selectedRegion = selectedRegion,
                    selectedCategory = selectedCategory,
                    selectedUsername = selectedUsername

                )

            }
        }
    }
}

@Composable
fun ProfessionalMyActivityTopBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onFiltering: () -> Unit
) {
    Row(
        modifier = modifier
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


        Box(
            modifier = Modifier
                .background(Color(0xFFcc3300))
                .padding(8.dp)
                .clickable(onClick = onFiltering)
        ){
            Text(
                text = "Filters",
                fontSize = 15.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        }


    }
}



@Composable
fun ProfessionalMyActivityMainContent(
    modifier: Modifier = Modifier,
    username: String,
    selectedRegion: String,
    selectedCategory: String,
    selectedUsername: String
) {
    val context = LocalContext.current
    var imageUrls by remember { mutableStateOf(listOf<String>()) }
    var imageDocs by remember { mutableStateOf(listOf<String>()) }
    var imageDetails by remember { mutableStateOf(listOf<Map<String, String>>()) }

    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(username) {
        if (username.isNotEmpty()) {
            db.collection("validated_images")
                .whereEqualTo("validatedBy", username)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val urls = snapshot.documents.map { it.getString("imageUri") ?: "" }
                        val docs = snapshot.documents.map { it.id }
                        val details = snapshot.documents.map {
                            mapOf(
                                "commonName" to (it.getString("commonName") ?: ""),
                                "scientificName" to (it.getString("scientificName") ?: ""),
                                "additionalDetails" to (it.getString("additionalDetails") ?: ""),
                                "date" to (it.getString("date") ?: ""),
                                "time" to (it.getString("time") ?: ""),
                                "region" to (it.getString("region") ?: ""),
                            )
                        }

                        imageUrls = urls
                        imageDocs = docs
                        imageDetails = details
                    }
                }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "$username's Activity",
                fontSize = 30.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            if (selectedRegion.isNotEmpty() || selectedCategory.isNotEmpty() || selectedUsername.isNotEmpty()) {
                for ((index, imageUrl) in imageUrls.withIndex()) {
                    val details = imageDetails[index]
                    val regionSearched = details["region"]
                    val categorySearched = details["categories"]
                    val usernameSearched = details["username"]

                    // Check if the current image matches the filters
                    val matchesRegion = selectedRegion.isEmpty() || regionSearched == selectedRegion
                    val matchesCategory = selectedCategory.isEmpty() || categorySearched == selectedCategory
                    val matchesUsername = selectedUsername.isEmpty() || usernameSearched == selectedUsername

                    if (matchesRegion && matchesCategory && matchesUsername) {
                        val painter = rememberAsyncImagePainter(model = imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Validated Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        val docId = imageDocs[index]
                        val details = imageDetails[index]
                        var isExpanded by remember { mutableStateOf(false) }
                        val additionalDetailsText = details["additionalDetails"] ?: ""
                        val maxLength = 20
                        val additionalDetails = if (additionalDetailsText.length > maxLength && !isExpanded) {
                            "${additionalDetailsText.take(maxLength)}..."
                        } else {
                            additionalDetailsText
                        }

                        Text(text = "Scientific Name: ${details["scientificName"]}")
                        Text(text = "Common Name: ${details["commonName"]}")

                        Text(text = "Date: ${details["date"]}")
                        Text(text = "Time: ${details["time"]}")
                        Text(text = "Region: ${details["region"]}")

                        Text(
                            text = "Additional Details: $additionalDetails",
                            modifier = Modifier.clickable { isExpanded = !isExpanded },
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        var showDialog by remember { mutableStateOf(false) }

                        Spacer(modifier = Modifier.height(20.dp))

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
                                            showDialog = true
                                        }
                                ) {
                                    Text(text = "Delete Photo")
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                            }
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Delete Photo") },
                                text = { Text("Are you sure you want to delete this photo?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        deleteImageFromDatabase(
                                            imageUrl,
                                            docId,
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
            }else{
                for ((index, imageUrl) in imageUrls.withIndex()) {
                    val painter = rememberAsyncImagePainter(model = imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Validated Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    val docId = imageDocs[index]
                    val details = imageDetails[index]
                    var isExpanded by remember { mutableStateOf(false) }
                    val additionalDetailsText = details["additionalDetails"] ?: ""
                    val maxLength = 20
                    val additionalDetails = if (additionalDetailsText.length > maxLength && !isExpanded) {
                        "${additionalDetailsText.take(maxLength)}..."
                    } else {
                        additionalDetailsText
                    }

                    Text(text = "Scientific Name: ${details["scientificName"]}")
                    Text(text = "Common Name: ${details["commonName"]}")

                    Text(text = "Date: ${details["date"]}")
                    Text(text = "Time: ${details["time"]}")
                    Text(text = "Region: ${details["region"]}")

                    Text(
                        text = "Additional Details: $additionalDetails",
                        modifier = Modifier.clickable { isExpanded = !isExpanded },
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    var showDialog by remember { mutableStateOf(false) }

                    Spacer(modifier = Modifier.height(20.dp))

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
                                        showDialog = true
                                    }
                            ) {
                                Text(text = "Delete Photo")
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                        }
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Delete Photo") },
                            text = { Text("Are you sure you want to delete this photo?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    deleteImageFromDatabase(
                                        imageUrl,
                                        docId,
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
}


