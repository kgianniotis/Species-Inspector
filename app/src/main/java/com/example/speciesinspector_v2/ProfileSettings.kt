package com.example.speciesinspector_v2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileSettings : ComponentActivity() {
    private val logoutAction by lazy { LogoutAction(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Finds current user's username
        val currentUser = FirebaseAuth.getInstance().currentUser
        var username by mutableStateOf("")
        var email by mutableStateOf("")

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        username = document.getString("username") ?: ""
                        email = document.getString("email") ?: ""
                    }
                }
        }

        setContent {
            SpeciesInspector_V2Theme {
                ProfileSettingsScreen(
                    username = username,
                    email = email,
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
fun ProfileSettingsScreen(
    username: String,
    email: String,
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
        topBarTitle = "Profile Settings",
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
        ProfileSettingsMainContent(
            username = username,
            email = email
        )
    }
}



@Composable
fun ProfileSettingsMainContent(
    modifier: Modifier = Modifier,
    username: String,
    email: String
) {

    var changeUsernameForm by remember { mutableStateOf(false) }
    var changeEmailForm by remember { mutableStateOf(false) }
    var changePasswordForm by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
        ){

            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = modifier
                    .padding(8.dp)
            ){
                Text(
                    text = "$username 's Profile",
                    fontSize = 35.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = modifier
                    .background(Color(0xFFffff4d))
                    .padding(8.dp)
            ){
                Text(
                    text = "Username : $username"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (!changeUsernameForm) {
                Box(
                    modifier = modifier
                        .background(Color(0xFFFFA500))
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { changeUsernameForm = true }
                ) {
                    Text(
                        text = "Change Username"
                    )
                }
            } else {
                UsernameChangeForm(
                    onClose = { changeUsernameForm = false },
                    onAbortUsernameChange = { changeUsernameForm = false }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = modifier
                    .background(Color(0xFFffff4d))
                    .padding(8.dp)
            ){
                Text(
                    text = "Email : $email"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (!changeEmailForm) {
                Box(
                    modifier = modifier
                        .background(Color(0xFFFFA500))
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { changeEmailForm = true }
                ) {
                    Text(
                        text = "Change Email"
                    )
                }
            } else {
                EmailChangeForm(
                    onClose = { changeEmailForm = false },
                    onAbortEmailChange = { changeEmailForm = false }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(35.dp))

            if (!changePasswordForm) {
                Box(
                    modifier = modifier
                        .background(Color(0xFFFFA500))
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { changePasswordForm = true }
                ) {
                    Text(
                        text = "Change Password"
                    )
                }
            } else {
                PasswordChangeForm(
                    onClose = { changePasswordForm = false },
                    onAbortPasswordChange = { changePasswordForm = false }
                )
            }
        }
    }
}


fun saveChangedUsername(
    uid: String?,
    username: String,
    context: Context
) {
    uid?.let {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(it)
        userRef.update("username", username)
    }

    ?.addOnSuccessListener {
        Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show()
    }
    ?.addOnFailureListener {
        Toast.makeText(context, "Failed to change username", Toast.LENGTH_SHORT).show()
    }
}

fun saveChangedEmail(
    uid: String?,
    email: String,
    context: Context
) {
    uid?.let {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(it)
        userRef.update("email", email)
    }
    ?.addOnSuccessListener {
        Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show()
    }
    ?.addOnFailureListener {
        Toast.makeText(context, "Failed to change username", Toast.LENGTH_SHORT).show()
    }
}

fun saveChangedPassword(
    currentUser: FirebaseUser?,
    currentPassword: String,
    newPassword: String,
    context: Context
) {
    currentUser?.let { user ->
        val email = user.email ?: return
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(context, "Password changed successfully :)", Toast.LENGTH_SHORT).show()

                                //Logs out the user after successfully creating a password
                                FirebaseAuth.getInstance().signOut()
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)

                            } else {
                                Toast.makeText(context, "Password could not be changed :(", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Could not authenticate user >:(.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

@Composable
fun PasswordChangeForm(

    onClose: () -> Unit,
    onAbortPasswordChange: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Enter current Password:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Enter new Password:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm new Password:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
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
                        .padding(6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = {
                            if(
                                currentUser != null && newPassword != currentPassword
                            ){
                                if (newPassword.isNotEmpty()) {
                                    if(newPassword == confirmPassword){
                                        saveChangedPassword(
                                            currentUser,
                                            currentPassword,
                                            newPassword,
                                            context
                                        )

                                        onClose()
                                    } else{
                                        Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                                        onClose()
                                    }
                                } else {
                                    Toast.makeText(context, "New Password must not be empty.", Toast.LENGTH_SHORT).show()
                                    onClose()
                                }
                            } else {
                                Toast.makeText(context, "Your new password must not be the same as you old one.", Toast.LENGTH_SHORT).show()
                                onClose()
                            }

                        })
                ){
                    Text(
                        text = "Confirm Password Change",
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
                        .clickable(onClick = onAbortPasswordChange)
                ) {
                    Text(
                        text = "Abort Change",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }


    }
}

@Composable
fun UsernameChangeForm(

    onClose: () -> Unit,
    onAbortUsernameChange: () -> Unit
) {
    var newUsername by remember { mutableStateOf("") }

    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = newUsername,
            onValueChange = { newUsername = it },
            label = { Text("Enter new Username:") },
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
                            if (currentUser != null && newUsername.isNotEmpty()) {
                                saveChangedUsername(
                                    currentUser.uid,
                                    newUsername,
                                    context
                                )

                                onClose()
                            } else {
                                Toast.makeText(context, "New Username must not be empty.", Toast.LENGTH_SHORT).show()
                            }
                        })
                ){
                    Text(
                        text = "Confirm Username Change",
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
                        .clickable(onClick = onAbortUsernameChange)
                ) {
                    Text(
                        text = "Abort Change",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }


    }
}

@Composable
fun EmailChangeForm(

    onClose: () -> Unit,
    onAbortEmailChange: () -> Unit
) {
    var newEmail by remember { mutableStateOf("") }

    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            label = { Text("Enter new Email:") },
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
                            if (currentUser != null && newEmail.isNotEmpty()) {
                                saveChangedEmail(
                                    currentUser.uid,
                                    newEmail,
                                    context
                                )

                                onClose()
                            } else {
                                Toast.makeText(context, "New Email must not be empty.", Toast.LENGTH_SHORT).show()
                            }
                        })
                ){
                    Text(
                        text = "Confirm Email Change",
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
                        .clickable(onClick = onAbortEmailChange)
                ) {
                    Text(
                        text = "Abort Change",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }


    }
}
