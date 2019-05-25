package com.kroegerama.kaiteki.recyclerview.example

import androidx.recyclerview.widget.LinearLayoutManager
import com.kroegerama.kaiteki.baseui.BaseActivity
import com.kroegerama.kaiteki.recyclerview.itemdecoration.StickyHeaderDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_main

    override fun setupGUI() {
        super.setupGUI()

        val adapter = ExampleAdapter(
            mapOf(
                "January" to listOf("One", "Two", "Three", "Four", "Five"),
                "February" to listOf("Hello World", "Hallo Welt"),
                "March" to listOf("Six", "Seven", "Eight"),
                "April" to listOf("Nine", "Ten", "Eleven", "Twelve"),
                "May" to listOf("Alpha", "Beta", "Gamma"),
                "June" to listOf("First", "Second", "Third", "4th", "5th"),
                "July" to (1..10).map(Int::toString).toList()
            )
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recycler.addItemDecoration(StickyHeaderDecoration(adapter))
    }
}
