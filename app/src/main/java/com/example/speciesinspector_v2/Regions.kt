package com.example.speciesinspector_v2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text

data class Region(val name: String)

val regions = listOf(
    Region("Attica"),
    Region("Central Greece"),
    Region("Central Macedonia"),
    Region("Crete"),
    Region("Eastern Macedonia and Thrace"),
    Region("Epirus"),
    Region("Ionian Islands"),
    Region("North Aegean"),
    Region("Peloponnese"),
    Region("South Aegean"),
    Region("Thessaly"),
    Region("Western Greece"),
    Region("Western Macedonia")
)

