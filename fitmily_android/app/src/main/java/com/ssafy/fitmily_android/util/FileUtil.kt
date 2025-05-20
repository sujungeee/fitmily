package com.ssafy.fitmily_android.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File

class FileUtil {
    fun getFileExtension(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)

        if (mimeType != null) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }

        if (uri.scheme == "file" || uri.scheme == "content") {
            val fileName = uri.lastPathSegment ?: return null
            val dotIndex = fileName.lastIndexOf('.')
            if (dotIndex != -1 && dotIndex < fileName.length - 1) {
                return fileName.substring(dotIndex + 1)
            }
        }
        return null
    }

    fun getFileName(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            "content" -> {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            return it.getString(nameIndex)
                        }
                    }
                }
                null
            }
            "file" -> {
                File(uri.path!!).name
            }
            else -> null
        }
    }

    fun getFileFromUri(context: Context, uri: Uri?, fileName: String, fileExtension: String): File {
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
        val file = File(context.cacheDir, "$fileName.$fileExtension")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }
}