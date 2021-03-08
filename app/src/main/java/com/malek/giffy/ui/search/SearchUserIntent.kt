package com.malek.giffy.ui.search

import com.malek.giffy.ui.UserIntent

sealed class SearchUserIntent:UserIntent() {
    class NewQuery(val query: String) : SearchUserIntent()
    object NextPage : SearchUserIntent()
}