package io.github.viniciusalvesmello.demodownloadmanager.core

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment

const val REQUEST_PERMISSION_CODE = 1

fun checkPermissionIsGranted(context: Context, permission: String): Boolean =
    ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

fun requestPermissionOnFragment(fragment: Fragment, listPermission: List<String>) {
    fragment.requestPermissions(
        listPermission.toTypedArray(),
        REQUEST_PERMISSION_CODE
    )
}