package com.example.speciesinspector_v2

import android.app.Application
import com.google.firebase.FirebaseApp

class SpeciesInspectorV2App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
