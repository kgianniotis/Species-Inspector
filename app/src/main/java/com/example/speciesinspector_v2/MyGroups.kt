package com.example.speciesinspector_v2

import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class MyGroups : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var username: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContent {
            SpeciesInspector_V2Theme {
                var showPostMessageScreen by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    fetchUserDetails()
                }

                if (showPostMessageScreen) {
                    PostMessageScreen(
                        firestore = firestore,
                        onNavigateBack = { showPostMessageScreen = false },
                        username = username ?: "Unknown",
                        role = role ?: "Citizen",
                        onGoBack = {navigateToMyGroups()}
                    )
                } else {
                    MyGroupsScreen(
                        onLogout = { logoutAction.performLogout() },
                        onNavigateToMainMenu = { navigateToMainMenu() },
                        onNavigateToProfileSettings = { navigateToProfileSettings() },
                        onNavigateToDonate = { navigateToDonate() },
                        onNavigateToGuides = { navigateToGuides() },
                        onNavigateToMyGroups = { navigateToMyGroups() },
                        onNavigateToUsefulKnowledge = { navigateToUsefulKnowledge() },
                        onNavigateToSpeciesList = { navigateToSpeciesList() },
                        firestore = firestore,
                        onActionButton = { showPostMessageScreen = true }
                    )
                }
            }
        }
    }

    private fun fetchUserDetails() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        username = document.getString("username")
                        role = document.getString("role")
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
fun MyGroupsScreen(
    onLogout: () -> Unit,
    onNavigateToMainMenu: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToMyGroups: () -> Unit,
    onNavigateToUsefulKnowledge: () -> Unit,
    onNavigateToSpeciesList: () -> Unit,
    firestore: FirebaseFirestore,
    onActionButton: () -> Unit
) {
    TemplateScreen(
        topBarTitle = "My Groups",
        onMenuClick = {},
        onLogout = onLogout,
        onActionButton = onActionButton,
        actionText = "Start Thread",
        onNavigateToMainMenu = onNavigateToMainMenu,
        onNavigateToProfileSettings = onNavigateToProfileSettings,
        onNavigateToDonate = onNavigateToDonate,
        onNavigateToGuides = onNavigateToGuides,
        onNavigateToMyGroups = onNavigateToMyGroups,
        onNavigateToUsefulKnowledge = onNavigateToUsefulKnowledge,
        onNavigateToSpeciesList = onNavigateToSpeciesList
    ) {
        MyGroupsMainContent(
            firestore = firestore
        )
    }
}

@Composable
fun MyGroupsMainContent(
    firestore: FirebaseFirestore,
    modifier: Modifier = Modifier
) {
    var messages by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var selectedMessageId by remember { mutableStateOf<String?>(null) }
    var showCommentsScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firestore.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    return@addSnapshotListener
                }
                messages = snapshot.documents.mapNotNull { it.data?.plus("id" to it.id) }
            }
    }

    if (showCommentsScreen && selectedMessageId != null) {
        val selectedMessage = messages.find { it["id"] == selectedMessageId }
        val selectedTitle = selectedMessage?.get("title") as? String ?: "Untitled"
        val selectedContent = selectedMessage?.get("message") as? String ?: ""
        val selectedUsername = selectedMessage?.get("username") as? String ?: "Unknown"
        val selectedRole = selectedMessage?.get("role") as? String ?: "Unknown"
        val selectedTimestamp = selectedMessage?.get("timestamp") as? Long ?: 0L

        CommentsScreen(
            firestore = firestore,
            messageId = selectedMessageId!!,
            onNavigateBack = { showCommentsScreen = false },
            messageTitle = selectedTitle,
            messageContent = selectedContent,
            messageUsername = selectedUsername,
            messageRole = selectedRole,
            messageTimestamp = selectedTimestamp
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            messages.forEach { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffff66))
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .clickable {
                            selectedMessageId = msg["id"] as? String
                            showCommentsScreen = true
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var isExpanded by remember { mutableStateOf(false) }
                        val messageText = (msg["message"] as? String) ?: ""
                        val maxLength = 35
                        val displayText = if (messageText.length > maxLength && !isExpanded) {
                            "${messageText.take(maxLength)}..."
                        } else {
                            messageText
                        }

                        Text(
                            text = (msg["title"] as? String) ?: "Untitled",
                            fontSize = 25.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(bottom = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .fillMaxWidth(),
                            thickness = 1.dp
                        )
                        Text(
                            text = displayText,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .fillMaxWidth(),
                            thickness = 1.dp
                        )
                        Text(
                            text = "By: ${(msg["username"] as? String) ?: "Unknown"} | Role: ${(msg["role"] as? String) ?: "Unknown"} | Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(msg["timestamp"] as? Long ?: 0L))}",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Left
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}








@Composable
fun PostMessageScreen(
    firestore: FirebaseFirestore,
    onNavigateBack: () -> Unit,
    username: String,
    role: String,
    onGoBack: () -> Unit
) {
    var messageTitle by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showErrorToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val messageMaxLength = 200
    val messageContext = LocalContext.current

    LaunchedEffect(showErrorToast) {
        if (showErrorToast) {
            Toast.makeText(context, "You need to provide both a Title and a Message.", Toast.LENGTH_SHORT).show()
            showErrorToast = false
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF228B22))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = messageTitle,
                onValueChange = { messageTitle = it },
                label = { Text("Enter a Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = message,
                onValueChange = {
                    if (it.length <= messageMaxLength) message = it
                    else Toast.makeText(messageContext, "Message cannot be more than 200 Characters", Toast.LENGTH_SHORT).show()
                },
                label = { Text("Enter your message") },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(8.dp)
                        .clickable(onClick = onGoBack)
                ) {
                    Text("Go Back")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFA500))
                        .padding(8.dp)
                        .clickable(onClick = {
                            if (messageTitle.isNotEmpty() && message.isNotEmpty()) {
                                val newMessage = hashMapOf(
                                    "title" to messageTitle,
                                    "message" to message,
                                    "username" to username,
                                    "timestamp" to System.currentTimeMillis(),
                                    "role" to role,
                                    "comments" to emptyList<Map<String, Any>>()
                                )
                                firestore.collection("messages").add(newMessage)
                                messageTitle = ""
                                message = ""
                                onNavigateBack()
                            } else{
                                showErrorToast = true
                            }
                        })
                ) {
                    Text("Post Thread")
                }
            }
        }
    }
}



@Composable
fun CommentsScreen(
    firestore: FirebaseFirestore,
    messageId: String,
    onNavigateBack: () -> Unit,
    messageTitle: String,
    messageContent: String,
    messageUsername: String,
    messageRole: String,
    messageTimestamp: Long
) {
    var comments by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var newComment by remember { mutableStateOf("") }
    var showErrorToast by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentUsername by remember { mutableStateOf("Unknown") }
    var currentUserRole by remember { mutableStateOf("Unknown") }

    //Access my firestore database
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentUsername = document.getString("username") ?: "Unknown"
                        currentUserRole = document.getString("role") ?: "Unknown"
                    }
                }
        }

        //Access my firestore database
        firestore.collection("messages").document(messageId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    return@addSnapshotListener
                }
                val messageData = snapshot.data
                comments = messageData?.get("comments") as? List<Map<String, Any>> ?: listOf()
            }
    }


    LaunchedEffect(showErrorToast) {
        if (showErrorToast) {
            Toast.makeText(context, "Comments cannot be empty.", Toast.LENGTH_SHORT).show()
            showErrorToast = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Display the selected message details
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFffff66))
                .border(1.dp, Color.Black)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = messageTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    thickness = 1.dp
                )

                Text(
                    text = messageContent,
                    fontSize = 16.sp
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    thickness = 1.dp
                )

                Text(
                    text = "By: $messageUsername | Role: $messageRole | Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(messageTimestamp))}",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            comments.forEach { comment ->
                val commentText = comment["text"] as? String ?: ""
                val commentUsername = comment["username"] as? String ?: "Unknown"
                val commentRole = comment["role"] as? String ?: "Unknown"

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = when (commentRole) {
                                "Scientist" -> Color.Blue
                                "Citizen" -> Color.Red
                                else -> Color.LightGray
                            }
                        )
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = commentText,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "By: $commentUsername | Role: $commentRole",
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = newComment,
            onValueChange = { newComment = it },
            label = { Text("Add a comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFA500))
                    .padding(8.dp)
                    .clickable {
                        if (newComment.isNotEmpty()) {
                            val newCommentData = mapOf(
                                "text" to newComment,
                                "username" to currentUsername,
                                "role" to currentUserRole
                            )
                            firestore.collection("messages").document(messageId)
                                .update("comments", FieldValue.arrayUnion(newCommentData))
                            newComment = ""
                        } else {
                            showErrorToast = true
                        }
                    }
            ) {
                Text("Post Comment")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .padding(8.dp)
                    .clickable(onClick = onNavigateBack)
            ) {
                Text("Back")
            }
        }
    }
}








