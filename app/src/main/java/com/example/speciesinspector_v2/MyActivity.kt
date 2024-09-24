package com.example.speciesinspector_v2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class MyActivity : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Finds current user's username
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
                MyActivityScreen(
                    username = username,
                    onLogout = { logoutAction.performLogout() },
                    onNavigateToMainMenu = { navigateToMainMenu() },
                    onNavigateToProfileSettings = { navigateToProfileSettings() },
                    onNavigateToDonate = { navigateToDonate() },
                    onNavigateToGuides = { navigateToGuides() },
                    onNavigateToMyGroups = { navigateToMyGroups() },
                    onNavigateToUsefulKnowledge = { navigateToUsefulKnowledge() },
                    onNavigateToSpeciesList = { navigateToSpeciesList() },
                    onAbortUpload = { backToMyActivity() }
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

    private fun backToMyActivity() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val role = document.getString("role")
                    if (role == "Citizen") {
                        val intent = Intent(this, MyActivity::class.java)
                        startActivity(intent)
                    } else if (role == "Admin" || role == "Scientist") {
                        val intent = Intent(this, ProfessionalMyActivity::class.java)
                        startActivity(intent)
                    }
                }
        }
    }
}


@Composable
fun DetailEntryForm(
    onDetailsSubmit: (String, String, String, String, String) -> Unit,
    onAbortUpload: () -> Unit
) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf("") }

    //max length of observations
    val observationsMaxLength = 150
    val observationsContext = LocalContext.current

    val dateRegex = Regex("""\d{2}/\d{2}/\d{4}""")
    val dateContext = LocalContext.current
    val timeRegex = Regex("""^([01]?[0-9]|2[0-3]):[0-5][0-9]$""")
    val timeContext = LocalContext.current
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }

    var regionError by remember { mutableStateOf(false) }
    val regionsContext = LocalContext.current

    var categoriesError by remember { mutableStateOf(false) }
    val categoriesContext = LocalContext.current

    val fullContext = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter Photo Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = date,
            onValueChange = {
                date = it
                dateError = !date.matches(dateRegex)
            },
            label = { Text("Date (dd/mm/yyyy)") },
            modifier = Modifier.fillMaxWidth(),
            isError = dateError
        )
        if (dateError) {
            Toast.makeText(dateContext, "Wrong Date Format", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = time,
            onValueChange = {
                time = it
                timeError = !time.matches(timeRegex)
            },
            label = { Text("Time (24-hour format)") },
            modifier = Modifier.fillMaxWidth(),
            isError = timeError
        )
        if (timeError) {
            Toast.makeText(timeContext, "Wrong Time Format", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = region,
            onValueChange = {
                region = it
                regionError = !regions.any { r -> r.name.equals(region, ignoreCase = true) }
            },
            label = { Text("Region") },
            modifier = Modifier.fillMaxWidth(),
            isError = regionError
        )
        if (regionError) {
            Toast.makeText(regionsContext, "Region Selection Error", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = categories,
            onValueChange = {
                categories = it
                categoriesError = !categoriesOfSpecies.any { r -> r.name.equals(categories, ignoreCase = true) }
            },
            label = { Text("Species' Category") },
            modifier = Modifier.fillMaxWidth(),
            isError = categoriesError
        )
        if (categoriesError) {
            Toast.makeText(categoriesContext, "Category Selection Error", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = observations,
            onValueChange = {
                if (it.length <= observationsMaxLength) observations = it
                else Toast.makeText(observationsContext, "Cannot be more than 150 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Observations") },
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
                        .clickable( onClick = {
                            if (observations.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && region.isNotEmpty() && categories.isNotEmpty()) onDetailsSubmit(date, time, region, categories, observations)
                            else Toast.makeText(fullContext, "All Text fields must be filled.", Toast.LENGTH_SHORT).show()
                        })
                ) {
                    Text(
                        text = "Confirm Upload",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
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

fun uploadImageToFirebaseStorage(
    filePath: Uri,
    onUploadSuccess: (String) -> Unit
) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    val fileReference: StorageReference = storageReference.child("images/${UUID.randomUUID()}")
    fileReference.putFile(filePath)
        .addOnSuccessListener {
            fileReference.downloadUrl.addOnSuccessListener { uri ->
                onUploadSuccess(uri.toString())
            }
        }

}

fun saveImageInfoToFirestore(
    downloadUrl: String,
    username: String,
    date: String,
    time: String,
    region: String,
    observations: String,
    categories: String
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        val imageInfo = hashMapOf(
            "url" to downloadUrl,
            "userId" to currentUser.uid,
            "username" to username,
            "timestamp" to com.google.firebase.Timestamp.now(),
            "date" to date,
            "time" to time,
            "region" to region,
            "observations" to observations,
            "categories" to categories
        )
        db.collection("images")
            .add(imageInfo)

    }
}

fun deleteImageFromFirebaseStorageAndFirestore(imageUrl: String, documentId: String) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
    storageReference.delete()
        .addOnSuccessListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("images").document(documentId).delete()
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
        .addOnFailureListener {

        }
}

@Composable
fun MyActivityScreen(
    username: String,
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit,
    onAbortUpload: () -> Unit
) {
    var drawerOpen by remember { mutableStateOf(false) }
    val drawerWidth by animateDpAsState(
        targetValue = if (drawerOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300), label = "Burger Menu"
    )

    var filterOpen by remember { mutableStateOf(false) }
    val filterWidth by animateDpAsState(
        targetValue = if (filterOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300), label = "Filter Menu"
    )
    var selectedRegion by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedUsername by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column {
            MyActivityTopBar(
                onFiltering = { filterOpen = true },
                onImageSelect = { selectImageLauncher.launch("image/*") },
                onMenuClick = { drawerOpen = true }
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
                    drawerWidth = drawerWidth,
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
                if (imageUrl == null) {
                    MyActivityMainContent(
                        username = username,
                        selectedImageUri = selectedImageUri,
                        onImageUpload = { uri ->
                            uploadImageToFirebaseStorage(
                                filePath = uri,
                                onUploadSuccess = { url ->
                                    imageUrl = url
                                    selectedImageUri = null
                                }
                            )
                        },
                        onDetailsSubmit = { date, time, region, categories, observations ->
                            saveImageInfoToFirestore(
                                downloadUrl = imageUrl!!,
                                username = username,
                                date = date,
                                time = time,
                                region = region,
                                observations = observations,
                                categories = categories
                            )
                            imageUrl = null
                        },
                        onAbortUpload = {onAbortUpload()},
                        selectedRegion = selectedRegion,
                        selectedCategory = selectedCategory,
                        selectedUsername = selectedUsername
                    )
                } else {
                    DetailEntryForm(
                        onDetailsSubmit = { date, time, region, categories, observations ->
                            saveImageInfoToFirestore(
                                downloadUrl = imageUrl!!,
                                username = username,
                                date = date,
                                time = time,
                                region = region,
                                observations = observations,
                                categories = categories
                            )
                            imageUrl = null
                        },
                        onAbortUpload = {onAbortUpload()}
                    )
                }
            }
        }
    }
}

@Composable
fun MyActivityTopBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onFiltering: () -> Unit,
    onImageSelect: () -> Unit
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

        Box(
            modifier = Modifier
                .background(Color(0xFFFFA500))
                .padding(8.dp)
                .clickable(onClick = onImageSelect)
        ) {
            Text(
                text = "Upload Picture",
                fontSize = 15.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun MyActivityMainContent(
    modifier: Modifier = Modifier,
    username: String,
    selectedImageUri: Uri?,
    onImageUpload: (Uri) -> Unit,
    onDetailsSubmit: (String, String, String, String, String) -> Unit,
    onAbortUpload: () -> Unit,
    selectedRegion: String,
    selectedCategory: String,
    selectedUsername: String
) {
    val context = LocalContext.current
    var imageUrls by remember { mutableStateOf(listOf<String>()) }
    var imageDocs by remember { mutableStateOf(listOf<String>()) }
    var imageDetails by remember { mutableStateOf(listOf<Map<String, String>>()) }

    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            db.collection("images")
                .whereEqualTo("userId", currentUser.uid)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val urls = snapshot.documents.map { it.getString("url") ?: "" }
                        val docs = snapshot.documents.map { it.id }
                        val details = snapshot.documents.map {
                            mapOf(
                                "date" to (it.getString("date") ?: ""),
                                "time" to (it.getString("time") ?: ""),
                                "region" to (it.getString("region") ?: ""),
                                "categories" to (it.getString("categories") ?: ""),
                                "observations" to (it.getString("observations") ?: "")
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$username's Activity",
                fontSize = 30.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Button(
                onClick = { onImageUpload(it) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA500),
                    contentColor = Color.Black
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add Details and Confirm")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAbortUpload,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.Black
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Abort Upload")
            }
        }

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
                            contentDescription = "Uploaded Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        val docId = imageDocs[index]
                        var isExpanded by remember { mutableStateOf(false) }
                        val observationText = details["observations"] ?: ""
                        val maxLength = 10
                        val displayText = if (observationText.length > maxLength && !isExpanded) {
                            "${observationText.take(maxLength)}..."
                        } else {
                            observationText
                        }

                        Text(text = "Date: ${details["date"]}")
                        Text(text = "Time: ${details["time"]}")
                        Text(text = "Region: $regionSearched")
                        Text(text = "Category: $categorySearched")
                        Text(
                            text = "Observations: $displayText",
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

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFFA500))
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            upLoadToCommunity(context, imageUrl, details, username)
                                        }
                                ) {
                                    Text(text = "Upload to Community")
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
                                        deleteImageFromFirebaseStorageAndFirestore(imageUrl, docId)
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
                        contentDescription = "Uploaded Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    val docId = imageDocs[index]
                    val details = imageDetails[index]
                    var isExpanded by remember { mutableStateOf(false) }
                    val observationText = details["observations"] ?: ""
                    val maxLength = 10
                    val displayText = if (observationText.length > maxLength && !isExpanded) {
                        "${observationText.take(maxLength)}..."
                    } else {
                        observationText
                    }

                    Text(text = "Date: ${details["date"]}")
                    Text(text = "Time: ${details["time"]}")
                    Text(text = "Region: ${details["region"]}")
                    Text(text = "Category: ${details["categories"]}")
                    Text(
                        text = "Observations: $displayText",
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

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFA500))
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        upLoadToCommunity(context, imageUrl, details, username)
                                    }
                            ) {
                                Text(text = "Upload to Community")
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
                                    deleteImageFromFirebaseStorageAndFirestore(imageUrl, docId)
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

fun upLoadToCommunity(
    context: Context,
    imageUrl: String,
    details: Map<String, String>,
    username: String
) {
    val db = FirebaseFirestore.getInstance()

    val communityData = hashMapOf(
        "imageUrl" to imageUrl,
        "date" to details["date"],
        "observations" to details["observations"],
        "region" to details["region"],
        "categories" to details["categories"],
        "time" to details["time"],
        "username" to username,
        "scientific Name" to "",
        "common Name" to ""
    )

    db.collection("community_images")
        .add(communityData)
        .addOnSuccessListener {
            Toast.makeText(context, "Uploaded to Community", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to upload: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}






