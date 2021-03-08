package com.malek.giffy.utilities

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.displaySnackBarError(
    @StringRes messageStringRes: Int,
    @StringRes actionTitle: Int? = null,
    action: View.OnClickListener? = null,
    root: View
) {
    Snackbar.make(
        root,
        getString(messageStringRes),
        Snackbar.LENGTH_LONG
    ).apply {
        if (actionTitle != null && action != null) {
            this.setAction(actionTitle, action)
        }
        show()
    }
}


