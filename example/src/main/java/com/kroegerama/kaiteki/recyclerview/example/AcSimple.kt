package com.kroegerama.kaiteki.recyclerview.example

import androidx.recyclerview.widget.LinearLayoutManager
import com.kroegerama.kaiteki.baseui.BaseActivity
import com.kroegerama.kaiteki.recyclerview.BaseAdapter
import com.kroegerama.kaiteki.recyclerview.BaseViewHolder
import com.kroegerama.kaiteki.recyclerview.SimpleAdapter
import com.kroegerama.kaiteki.toast
import kotlinx.android.synthetic.main.ac_simple.*
import kotlinx.android.synthetic.main.item_simple.view.*

class AcSimple : BaseActivity(R.layout.ac_simple) {

    private val adapter by lazy { MySimpleAdapter() }

    override fun setupGUI() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.setItems((1..100).map { "Item $it" })
    }

    class MyAdapter : BaseAdapter<String>() {
        override fun getLayoutRes(viewType: Int) = R.layout.item_simple

        override fun updater(viewHolder: BaseViewHolder<String>, viewType: Int, item: String?) = with(viewHolder) {
            itemView.title.text = item
        }

        override fun compareItems(checkContent: Boolean, a: String, b: String) = a == b

        override fun injectListeners(viewHolder: BaseViewHolder<String>, viewType: Int, currentItem: () -> String?) = with(viewHolder) {
            itemView.setOnClickListener { itemView.context.toast(currentItem() ?: "null") }
        }

    }

    inner class MySimpleAdapter : SimpleAdapter<String>(R.layout.item_simple) {

        override fun simpleUpdate(viewHolder: BaseViewHolder<String>, item: String?) = with(viewHolder) {
            itemView.title.text = item
        }

        override fun itemClick(viewHolder: BaseViewHolder<String>, item: String?) {
            toast(item ?: "null")
        }

        override fun compareItems(checkContent: Boolean, a: String, b: String): Boolean = a == b

    }
}