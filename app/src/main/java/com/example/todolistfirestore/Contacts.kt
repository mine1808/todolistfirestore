package com.example.todolistfirestore

import java.io.Serializable

data class Contacts(
    var id: String = "",
    var name: String = "",
    var number: String = ""
):Serializable
