package com.malek.giffy.ui.search

sealed class UserAction {
    data class NewQuery(val query: String) : UserAction()
    object NextPage : UserAction()
}