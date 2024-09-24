package com.example.speciesinspector_v2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class FilterMenu {
    @Composable
    fun DrawerContent(
        filterWidth: Dp,
        onCloseFilters: () -> Unit,
        selectedRegion: String,
        selectedCategory: String,
        selectedUsername: String,
        onFiltersApplied: (String, String, String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        var regionSelection by remember { mutableStateOf(selectedRegion) }
        var regionSelectionError by remember { mutableStateOf(false) }

        var categorySelection by remember { mutableStateOf(selectedCategory) }
        var categorySelectionError by remember { mutableStateOf(false) }

        var usernameSelection by remember { mutableStateOf(selectedUsername) }
        var usernameSelectionError by remember { mutableStateOf(false) }

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .width(filterWidth)
                .fillMaxHeight()
                .background(Color(0xFF7AB97A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = regionSelection,
                    onValueChange = {
                        regionSelection = it
                        regionSelectionError = !regions.any { r -> r.name.equals(regionSelection, ignoreCase = true) }
                    },
                    label = { Text("Region") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = regionSelectionError
                )
                if (regionSelectionError) {
                    Toast.makeText(context, "Region doesn't exist", Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = categorySelection,
                    onValueChange = {
                        categorySelection = it
                        categorySelectionError = !categoriesOfSpecies.any { r -> r.name.equals(categorySelection, ignoreCase = true) }
                    },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = categorySelectionError
                )
                if (categorySelectionError) {
                    Toast.makeText(context, "Category doesn't exist", Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = usernameSelection,
                    onValueChange = { newUsername ->
                        usernameSelection = newUsername
                        usernameSelectionError = false
                        db.collection("users")
                            .whereEqualTo("username", usernameSelection)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val querySnapshot = task.result
                                    usernameSelectionError = querySnapshot == null || querySnapshot.isEmpty
                                    if (usernameSelectionError) {
                                        Toast.makeText(context, "Username doesn't exist", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Error accessing Firestore", Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = usernameSelectionError
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Apply Filters",
                    modifier = Modifier
                        .clickable {
                            onFiltersApplied(regionSelection, categorySelection, usernameSelection)
                            onCloseFilters()
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Clear All Filters",
                    modifier = Modifier
                        .clickable {
                            regionSelection = ""
                            categorySelection = ""
                            usernameSelection = ""
                            regionSelectionError = false
                            categorySelectionError = false
                            usernameSelectionError = false
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Close Filter Menu",
                    modifier = Modifier
                        .clickable { onCloseFilters() }
                )
            }
        }
    }
}
