package com.example.vnpay.data.repository

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.vnpay.data.model.Image
import com.example.vnpay.data.model.ImageFolder
import com.example.vnpay.domain.repository.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ImageRepository {
    override fun getAllImageFolder(): List<ImageFolder> {
        val imageFolder: MutableList<ImageFolder> = mutableListOf()
        val folderPaths = java.util.ArrayList<String>()
        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        val cursor: Cursor? =
            context.contentResolver.query(imageUri, projection, null, null, null)
        try {
            cursor?.let {
                it.moveToFirst()
                do {
                    val folder = ImageFolder()
                    val folderName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val imagePath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val imageName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                    val image = Image(
                        imageName = imageName,
                        imagePath = imagePath
                    )
                    val folderPath = "${
                        imagePath.substring(0, imagePath.lastIndexOf("$folderName/"))
                    }$folderName/"
                    if (!folderPaths.contains(folderPath)) {
                        folderPaths.add(folderPath)
                        folder.path = folderPath
                        folder.folderName = folderName
                        folder.firstImage = imagePath
                        folder.addImages(image)
                        imageFolder.add(folder)
                    } else {
                        imageFolder.first { it.path == folderPath }.apply {
                            firstImage = imagePath
                            addImages(image)
                        }
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        for (i in imageFolder.indices) {
            Log.d(
                "picture folders",
                (imageFolder[i].folderName + " and path = " + imageFolder[i].path) + " " + imageFolder[i].getNumberOfImages()
            )
        }
        return imageFolder
    }
}