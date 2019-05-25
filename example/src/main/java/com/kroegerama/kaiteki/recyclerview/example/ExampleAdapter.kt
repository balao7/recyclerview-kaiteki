package com.kroegerama.kaiteki.recyclerview.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kroegerama.kaiteki.recyclerview.itemdecoration.StickyHeaderDecoration

class ExampleAdapter(
    itemMap: Map<String, List<String>>
) : RecyclerView.Adapter<VHExample>(), StickyHeaderDecoration.HeaderProvider<VHHeader> {

    private class DataItem(
        val title: String,
        val isHeader: Boolean
    )

    private val items: List<DataItem>

    init {
        items = ArrayList<DataItem>().apply {
            itemMap.entries.forEach { entry ->
                val (header, list) = entry
                add(DataItem(header, true))
                list.forEach { child -> add(DataItem(child, false)) }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_HEADER -> VHHeader.createInstance(parent)
        TYPE_ITEM -> VHItem.createInstance(parent)
        else -> throw IllegalStateException("illegal item type: $viewType")
    }

    override fun onBindViewHolder(holder: VHExample, position: Int) {
        when (holder) {
            is VHHeader -> holder.update(getItem(position).title)
            is VHItem -> holder.update(getItem(position).title)
        }
    }

    override fun getItemViewType(position: Int) = if (getItem(position).isHeader) TYPE_HEADER else TYPE_ITEM

    private fun getItem(position: Int) = items[position]

    override fun getHeaderPositionForItem(position: Int): Int {
        val iterator = items.listIterator(position + 1)
        while (iterator.hasPrevious()) {
            val item = iterator.previous()
            if (item.isHeader) {
                return iterator.nextIndex()
            }
        }
        return -1
    }

    override fun onCreateHeaderViewHolder(position: Int, parent: ViewGroup) =
        VHHeader.createInstance(parent)

    override fun onBindHeaderViewHolder(header: VHHeader, position: Int) {
        header.update(getItem(position).title)
    }

    override fun isHeader(position: Int) = getItemViewType(position) == TYPE_HEADER

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}

sealed class VHExample(view: View) : RecyclerView.ViewHolder(view)

class VHHeader(view: View) : VHExample(view) {
    private val title: TextView = view.findViewById(R.id.tvTitle)

    fun update(value: String) {
        title.text = value
    }

    companion object {
        private const val LAYOUT = R.layout.item_header

        fun createInstance(parent: ViewGroup) = VHHeader(
            LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
        )
    }
}

class VHItem(view: View) : VHExample(view) {
    private val title: TextView = view.findViewById(R.id.tvTitle)

    fun update(value: String) {
        title.text = value
    }

    companion object {
        private const val LAYOUT = R.layout.item_content

        fun createInstance(parent: ViewGroup) = VHItem(
            LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
        )
    }
}