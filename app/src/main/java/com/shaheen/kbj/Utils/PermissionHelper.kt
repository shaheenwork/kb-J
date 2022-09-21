package com.shaheen.kbj.Utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


class PermissionHelper(permissions: Array<String>?, context: Context?) {
    private val mContext: Context?
    private val permissions: Array<String>?
    fun hasPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    init {
        mContext = context
        this.permissions = permissions
        val PERMISSION_ALL = 1
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                (mContext as Activity?)!!,
                permissions!!, PERMISSION_ALL
            )
        }
    }
}