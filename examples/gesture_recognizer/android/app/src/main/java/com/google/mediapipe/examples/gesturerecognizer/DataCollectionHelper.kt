package com.google.mediapipe.examples.gesturerecognizer

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DataCollectionHelper(private val context: Context) {
    private val baseDir = File(context.getExternalFilesDir(null), "gesture_dataset")
    
    init {
        baseDir.mkdirs()
        // Ensure 'none' category exists as required by MediaPipe
        File(baseDir, "none").mkdirs()
    }

    fun getCategories(): List<String> {
        return baseDir.listFiles { file -> file.isDirectory }
            ?.map { it.name }
            ?.sorted()
            ?: listOf("none")    }

    fun createCategoryIfNotExists(category: String) {
        if (category.isNotBlank()) {
            File(baseDir, category).mkdirs()
        }
    }

    fun saveImage(bitmap: Bitmap, category: String) {
        val categoryDir = File(baseDir, category)
        if (!categoryDir.exists()) {
            categoryDir.mkdirs()
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFile = File(categoryDir, "gesture_${timestamp}.jpg")

        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
    }

    fun getDatasetPath(): String {
        return baseDir.absolutePath
    }
}