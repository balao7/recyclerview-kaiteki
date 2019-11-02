package com.kroegerama.kaiteki.recyclerview.example.model

open class ListItem(val title: String)

class ImageItem(
    title: String,
    val imageUrl: String
) : ListItem(title)