package com.example.speciesinspector_v2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.io.File

class LogoutAction(private val context: Context) {

    fun performLogout() {
        clearUserData()
        clearCache()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    private fun clearUserData() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun clearCache() {
        val cacheDir = context.cacheDir
        deleteDir(cacheDir)
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir.delete()
    }
}
