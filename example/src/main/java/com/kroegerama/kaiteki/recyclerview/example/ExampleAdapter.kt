package com.kroegerama.kaiteki.recyclerview.example

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kroegerama.kaiteki.recyclerview.example.model.ImageItem
import com.kroegerama.kaiteki.recyclerview.example.model.ListItem
import com.kroegerama.kaiteki.recyclerview.itemdecoration.StickyHeaderDecoration

class ExampleAdapter(
    private val context: Context,
    itemMap: Map<String, List<ImageItem>>,
    private val itemClick: (ImageItem, ImageView, TextView) -> Unit
) : RecyclerView.Adapter<VHExample>(), StickyHeaderDecoration.HeaderProvider<VHHeader>,
    ListPreloader.PreloadModelProvider<ImageItem> {

    override fun getPreloadItems(position: Int): List<ImageItem> {
        val item = getItem(position)
        return if (item.isHeader) {
            emptyList()
        } else {
            listOf(item.item as ImageItem)
        }
    }

    override fun getPreloadRequestBuilder(item: ImageItem) = GlideApp.with(context)
        .load(item.imageUrl)
        .dontTransform()

    private class DataItem(
        val item: ListItem,
        val isHeader: Boolean
    )

    private val items: List<DataItem>

    init {
        items = ArrayList<DataItem>().apply {
            itemMap.entries.forEach { entry ->
                val (header, list) = entry
                add(DataItem(ListItem(header), true))
                list.forEach { child -> add(DataItem(child, false)) }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_HEADER -> VHHeader.createInstance(parent)
        TYPE_ITEM -> VHItem.createInstance(parent, itemClick)
        else -> throw IllegalStateException("illegal item type: $viewType")
    }

    override fun onBindViewHolder(holder: VHExample, position: Int) {
        when (holder) {
            is VHHeader -> holder.update(getItem(position).item.title)
            is VHItem -> holder.update(getItem(position).item as ImageItem)
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
        header.update(getItem(position).item.title)
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

class VHItem(view: View, listener: (ImageItem, ImageView, TextView) -> Unit) : VHExample(view) {
    private val title: TextView = view.findViewById(R.id.tvTitle)
    private val image: ImageView = view.findViewById(R.id.ivImage)

    private var curItem: ImageItem? = null

    init {
        view.setOnClickListener { listener.invoke(curItem ?: return@setOnClickListener, image, title) }
    }

    fun update(value: ImageItem) {
        curItem = value
        title.text = value.title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.clipToOutline = true
        }

        GlideApp.with(itemView).load(value.imageUrl).transition(options).dontTransform().into(image)

        ViewCompat.setTransitionName(image, "image_$adapterPosition")
        ViewCompat.setTransitionName(title, "title_$adapterPosition")
    }

    companion object {
        private const val LAYOUT = R.layout.item_content

        private val options by lazy { DrawableTransitionOptions.withCrossFade(200) }

        fun createInstance(parent: ViewGroup, listener: (ImageItem, ImageView, TextView) -> Unit) = VHItem(
            LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false),
            listener
        )
    }
}