package com.fststep.nxt2me.core.extension

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Constants.IMAGE_URL
import com.fststep.nxt2me.core.data.Preferences
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


fun partFromString(stringData: String): RequestBody {
    return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
}
fun multiPartBodyFromFile(name:String,file: File): MultipartBody.Part {
    val rqstbody= file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name,"${System.currentTimeMillis()}.jpg",rqstbody)
}
fun multiPartBodyFromUri(context: Context,uri:Uri, title:String): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val requestBody = inputStream?.readBytes()?.toRequestBody("image/jpeg".toMediaTypeOrNull())!!
    return MultipartBody.Part.createFormData(title,"${System.currentTimeMillis()}.jpg",requestBody)
}
//fun multiPartBodyFromUri(context: Context,uri:Uri,title:String): MultipartBody.Part {
//    val contentResolver = context.contentResolver
//    val inputStream = contentResolver.openInputStream(uri)
//    val extension= getFileExtension(context, uri)
//    Toast.makeText(context,extension,Toast.LENGTH_SHORT).show()
//    val requestBody = inputStream?.readBytes()?.toRequestBody("image/$extension".toMediaTypeOrNull())!!
//    return MultipartBody.Part.createFormData(title, "${System.currentTimeMillis()}.$extension",requestBody)
//}
//private fun getFileExtension(context: Context,uri: Uri): String {
//    val contentResolver = context.contentResolver
//    val mimeTypeMap = MimeTypeMap.getSingleton()
//    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ?: "jpeg"
//}

fun getGlideUrlWithAuth(imageUrl:String): GlideUrl {
    val glideUrl = GlideUrl(
        imageUrl,
        LazyHeaders.Builder()
            .addHeader("Authorization", "Bearer "+Preferences.fetchToken())
            .build()
    )
    return glideUrl
}
fun getImageDownloadUrl(filename:String): GlideUrl {
        return getGlideUrlWithAuth(Constants.API_BASE_URL+IMAGE_URL+ convertFilePathToQueryString(filename))
}
fun convertFilePathToQueryString(filePath: String): String {
    // URL-encode the file path
    val encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString())

    // Construct the query string
    val queryString = "?path=$encodedPath"

    return queryString
}