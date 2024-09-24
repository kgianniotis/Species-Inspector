package com.example.speciesinspector_v2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter


data class PhotoItem(
    val photoUrl: String,
    val username: String,
    val uploadDate: String
)

@Composable
fun PhotoItemView(photoItem: PhotoItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoItem.photoUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Uploaded by: ${photoItem.username}", fontSize = 14.sp, color = Color.Black)
        Text(text = "Date: ${photoItem.uploadDate}", fontSize = 12.sp, color = Color.Gray)
    }
}

