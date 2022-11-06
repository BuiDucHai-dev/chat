package com.example.stalk.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class Comm {

    companion object{

        const val REQUEST_PERMISSION_CODE: Int = 1
    }


    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun requestPermission(activity: Activity){
        val permission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(activity, permission, REQUEST_PERMISSION_CODE)
    }

    fun checkPermission(activity: Activity) : Boolean{
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            return false
        }
        return true
    }

    fun hideKeyBoard(activity: Activity){
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun checkContactPermission(activity: Activity) : Boolean{
        return ContextCompat.checkSelfPermission(
            activity, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestContactPermission(activity: Activity) {
        val permission = arrayOf(
            Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(activity, permission, REQUEST_PERMISSION_CODE)
    }
}