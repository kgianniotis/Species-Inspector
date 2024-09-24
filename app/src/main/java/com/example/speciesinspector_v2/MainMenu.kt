package com.example.speciesinspector_v2

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.content.Context

class MainMenu : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            setContent {
                SpeciesInspector_V2Theme {
                    MainScreen(
                        requestCameraPermission = { requestCameraPermission() },
                        onNavigateToMyActivity = { navigateToMyActivity() },
                        permissionDenied = true,
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
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            saveImageToExternalStorage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                MainScreen(
                    requestCameraPermission = { requestCameraPermission() },
                    onNavigateToMyActivity = { navigateToMyActivity() },
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

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        val fos: OutputStream?

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SpeciesInspector")
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/SpeciesInspector")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            showToast("Image saved successfully!")
        } ?: run {
            showToast("Error saving image")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMyActivity() {
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
fun MainScreen(
    requestCameraPermission: () -> Unit,
    onNavigateToMyActivity: () -> Unit,
    permissionDenied: Boolean = false,
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

    var filterOpen by remember { mutableStateOf(false) }
    val filterWidth by animateDpAsState(
        targetValue = if (filterOpen) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300), label = "Filter Menu"
    )
    var selectedRegion by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedUsername by remember { mutableStateOf("") }

    var showValidation by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var passedImageDate by remember { mutableStateOf("") }
    var passedImageTime by remember { mutableStateOf("") }
    var passedImageRegion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column {
            TopBar(
                onClick = requestCameraPermission,
                onMenuClick = { drawerOpen = true },
                onNavigateToMyActivity = onNavigateToMyActivity,
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
                if (showValidation) {
                    imageUri?.let {
                        ValidationForm(
                            imageUri = it,
                            date = passedImageDate,
                            time = passedImageTime,
                            region = passedImageRegion,
                            onClose = { showValidation = false },
                            onAbortValidation = { showValidation = false }
                        )
                    }
                } else {
                    MainContent(
                        selectedRegion = selectedRegion,
                        selectedCategory = selectedCategory,
                        selectedUsername = selectedUsername
                    )
                }

                if (permissionDenied) {
                    PermissionDeniedMessage()
                }
            }
        }
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onClick: () -> Unit,
    onNavigateToMyActivity: () -> Unit,
    onFiltering: () -> Unit
) {
    val image = painterResource(R.drawable.photo_icon)
    val imageMenu = painterResource(R.drawable.burger_menu)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Yellow)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Image(
            painter = imageMenu,
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

        Image(
            painter = image,
            contentDescription = "Take Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clickable(onClick = onClick)
        )

        Text(
            text = "Activity",
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(onClick = onNavigateToMyActivity)
        )
    }
}



@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    selectedRegion: String,
    selectedCategory: String,
    selectedUsername: String
) {
    var userRole by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

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

    var communityImages by remember { mutableStateOf(listOf<CommunityImages>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val images = fetchCommunityImages()
            communityImages = images
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            val filteredImages = if (selectedRegion.isNotEmpty() || selectedCategory.isNotEmpty() || selectedUsername.isNotEmpty()) {
                communityImages.filter { image ->
                    val matchesRegion = selectedRegion.isEmpty() || image.region == selectedRegion
                    val matchesCategory = selectedCategory.isEmpty() || image.categories == selectedCategory
                    val matchesUsername = selectedUsername.isEmpty() || image.username == selectedUsername
                    matchesRegion && matchesCategory && matchesUsername
                }
            } else {
                communityImages
            }

            filteredImages.forEach { image ->
                var showValidationForm by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    var isExpanded by remember { mutableStateOf(false) }
                    val observationText = image.observations
                    val maxLength = 10
                    val displayText = if (observationText.length > maxLength && !isExpanded) {
                        "${observationText.take(maxLength)}..."
                    } else {
                        observationText
                    }

                    Image(
                        painter = rememberAsyncImagePainter(model = image.imageUrl),
                        contentDescription = "Main Menu Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Date: ${image.date}",
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Time: ${image.time}",
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Region: ${image.region}",
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Category: ${image.categories}",
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Observations: $displayText",
                        modifier = Modifier.clickable { isExpanded = !isExpanded },
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Username: ${image.username}",
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = "Scientific Name: ${image.scientificName}",
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Common Name: ${image.commonName}",
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (showValidationForm) {
                    ValidationForm(
                        imageUri = Uri.parse(image.imageUrl),
                        date = image.date,
                        time = image.time,
                        region= image.region,
                        onClose = { showValidationForm = false },
                        onAbortValidation = { showValidationForm = false }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (!showValidationForm){
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFA500))
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (userRole == "Scientist") {
                                            showValidationForm = true
                                        } else {
                                            Toast.makeText(context, "Only Scientists can validate photos.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            ) {
                                Text(
                                    text = "Validate Photo"
                                )
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


@Composable
fun ValidationForm(
    imageUri: Uri,
    date: String,
    time: String,
    region: String,
    onClose: () -> Unit,
    onAbortValidation: () -> Unit
) {
    var scientificName by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var additionalDetails by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    val additionalDetailsMaxLength = 70
    val additionalDetailsContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = scientificName,
            onValueChange = { scientificName = it },
            label = { Text("Enter Scientific Name:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = commonName,
            onValueChange = { commonName = it },
            label = { Text("Enter Common Name:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = additionalDetails,
            onValueChange = {
                if (it.length <= additionalDetailsMaxLength) additionalDetails = it
                else Toast.makeText(additionalDetailsContext, "Cannot be more than 70 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Enter Additional Details:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )


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
                        .padding(6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = {
                            if (currentUser != null) {
                                val imageUriString = imageUri.toString()
                                saveValidationDetailsToDatabase(
                                    scientificName,
                                    commonName,
                                    additionalDetails,
                                    date,
                                    time,
                                    region,
                                    imageUriString,
                                    context
                                )
                                onClose()
                            } else {
                                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                            }
                        })
                ){
                    Text(
                        text = "Confirm Validation",
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
                        .clickable(onClick = onAbortValidation)
                ) {
                    Text(
                        text = "Abort Validation",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }


    }
}


fun saveValidationDetailsToDatabase(
    scientificName: String,
    commonName: String,
    additionalDetails: String,
    date: String,
    time: String,
    region: String,
    imageUri: String,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (currentUser != null) {
        db.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                val username = document.getString("username") ?: "Unknown User"

                val validationData = mapOf(
                    "scientificName" to scientificName,
                    "commonName" to commonName,
                    "additionalDetails" to additionalDetails,
                    "date" to date,
                    "time" to time,
                    "region" to region,
                    "imageUri" to imageUri,
                    "validatedBy" to username,
                    "timestamp" to System.currentTimeMillis()
                )

                db.collection("validated_images")
                    .add(validationData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Image validated successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to save validation details", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to retrieve user details", Toast.LENGTH_SHORT).show()
            }
    }
}



suspend fun fetchCommunityImages(): List<CommunityImages> {
    val firestore = FirebaseFirestore.getInstance()
    val imagesList = mutableListOf<CommunityImages>()
    try {
        val result = firestore.collection("community_images").get().await()
        for (document in result.documents) {
            val imageUrl = document.getString("imageUrl") ?: ""
            val date = document.getString("date") ?: ""
            val observations = document.getString("observations") ?: ""
            val region = document.getString("region") ?: ""
            val categories = document.getString("categories") ?: ""
            val time = document.getString("time") ?: ""
            val username = document.getString("username") ?: ""
            val scientificName = document.getString("scientificName") ?: ""
            val commonName = document.getString("commonName") ?: ""



            if (imageUrl.isNotEmpty()) {
                imagesList.add(
                    CommunityImages(
                        imageUrl = imageUrl,
                        date = date,
                        observations = observations,
                        region = region,
                        time = time,
                        username = username,
                        scientificName = scientificName,
                        commonName = commonName,
                        categories = categories
                    )
                )
            } else {
                Log.e("CommunityImages", "Missing imageUrl for document ${document.id}")
            }
        }
    } catch (e: Exception) {
        Log.e("CommunityImages", "Error fetching documents", e)
    }
    return imagesList
}


@Composable
fun PermissionDeniedMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Camera permission is required to use this feature.",
            color = Color.White,
            modifier = Modifier
                .background(Color.Red)
                .padding(16.dp)
        )
    }
}


