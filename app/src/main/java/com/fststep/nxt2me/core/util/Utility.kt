/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.models.JwtTokenPayload
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.view.CommonDialogs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Shubham Singh on 05/06/21.
 */

object Utility {
    fun getBitmapOptions(): BitmapFactory.Options {
        // Get the dimensions of the View
        val targetW: Int = 250
        val targetH: Int = 151

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = 1.coerceAtLeast((photoW / targetW).coerceAtMost(photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        return bmOptions
    }

    fun galleryAddPic(currentPhotoPath: String, context: Context) {
        if (currentPhotoPath.isEmpty()) return
        val file = File(currentPhotoPath)
        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()),
            null
        ) { path: String?, uri: Uri? ->
            Log.d("ExternalStorage", "Scanned $path:")
            Log.d("ExternalStorage", "-> uri=$uri")
        }
    }

    fun savefile(sourceuri: Uri, currentPhotoPath: String, contentResolver: ContentResolver): Boolean {
        if (currentPhotoPath.isEmpty()) return false
        var result = false
        val destinationFilename = currentPhotoPath
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(contentResolver.openInputStream(sourceuri))
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
            result = false
        } finally {
            return try {
                bis?.close()
                bos?.close()
                result
            } catch (e: IOException) {
                e.printStackTrace()
                result
            }
        }
    }
    fun isFieldsEmpty(array: ArrayList<EditText>): Boolean {
        for(a in array){
            if(a.text!!.trim().isEmpty()){
                return true
            }
        }
        return false
    }
    fun performErrorState(context: AppCompatActivity, state: State.ErrorState, msg: String) {
        Log.i(context.localClassName, state.exception.errorMessage)
        Log.d("TAG","state.error"+state.exception.errorMessage + state.exception.errorCode)
        val dialogs= CommonDialogs()
        dialogs.showDialogWithOneButton(
            context,msg,state.exception.errorMessage,"OK",
            { dialog, _ ->
                dialog.dismiss()
            }
        )
    }
    fun provideTokenPayload(token:String): JwtTokenPayload {
        val p= token.split(".")[1]
        val payload= String(Base64.decode(p,Base64.DEFAULT))
        val gson=Gson()
        return gson.fromJson(payload,JwtTokenPayload::class.java)
    }

    fun disableFocus(arr:ArrayList<View>){
        for(view in arr){
            view.isFocusable=false
        }
    }
    fun enableFocus(arr:ArrayList<View>){
        for(view in arr){
            view.isFocusableInTouchMode=true
        }
    }
    fun getImageFromAssets(context: Context,fileName:String): Bitmap? {
        var bitmapImg: Bitmap?
        try {
            val inputStream=context.assets.open(fileName)
            bitmapImg=BitmapFactory.decodeStream(inputStream)
        }catch (e:IOException){
            e.printStackTrace()
            return null;
        }
        return bitmapImg
    }
    fun getJsonStringFromAssets(context: Context,fileName: String?): String? {
        var jsonString: String = try {
            context.assets.open(fileName!!).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }
    fun hideViews(list: ArrayList<View>) {
        for(view in list){
            view.visibility= View.GONE
        }
    }
    fun showViews(list: ArrayList<View>) {
        for(view in list){
            view.visibility= View.VISIBLE
        }
    }
    fun closeKeyboard(context: Context) {
        val imm: InputMethodManager =
            context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

    }
    fun highlightErrorField(parentScrollView: NestedScrollView,view: TextInputLayout) {
        view.editText?.requestFocus()
        parentScrollView.post {
            parentScrollView.smoothScrollTo(0, view.top)
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                cleanError(listOf(view))
            },
            1500
        )
    }
     fun cleanError(list: List<TextInputLayout>) {
        for(view in list)
            view.error=null
    }
    fun discountAmount(percentage: Double, price: Double): Double {
        return (percentage / 100) * price
    }
}