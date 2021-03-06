package com.malek.giffy.ui.home

import com.malek.giffy.ui.UserIntent

sealed class HomeUserIntent : UserIntent() {
    object GetNewImage : HomeUserIntent()
}