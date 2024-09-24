package com.example.speciesinspector_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speciesinspector_v2.ui.theme.SpeciesInspector_V2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            SpeciesInspector_V2Theme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF228B22))
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            ScreenTitle(
                                title = getString(R.string.create_your_account_title)
                            )
                            CreateAccount(
                                onCreateAccount = { email, username, password, role ->
                                    if (password.length < 6) {
                                        Toast.makeText(
                                            this@CreateAccountActivity, "Password must be at least 6 characters.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        createAccount(email, username, password, role)
                                    }
                                },
                                alreadyHaveAccountClick = { navigateToMainActivity() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createAccount(email: String, username: String, password: String, role: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "username" to username,
                            "email" to email,
                            "role" to role
                        )
                        firestore.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    baseContext, "Account created successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    baseContext, "Error saving user: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}


@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 50.sp,
            lineHeight = 50.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
fun CreateAccount(
    onCreateAccount: (email: String, username: String, password: String, role: String) -> Unit,
    alreadyHaveAccountClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    val roles = listOf("Citizen", "Admin", "Scientist")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.8f)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.8f)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.8f)
        )

        // Role selection boxes
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text("Select Role:")
            roles.forEach { role ->
                val isSelected = selectedRole == role
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .background(
                            if (isSelected) Color.Blue else Color.LightGray,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedRole = role
                        }
                        .padding(16.dp)
                ) {
                    Text(text = role, color = if (isSelected) Color.White else Color.Black)
                }
            }
        }

        Button(
            onClick = { onCreateAccount(email, username, password, selectedRole) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = "Confirm Account Creation"
            )
        }

        Button(
            onClick = alreadyHaveAccountClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = "Already have an account? Sign in"
            )
        }
    }
}