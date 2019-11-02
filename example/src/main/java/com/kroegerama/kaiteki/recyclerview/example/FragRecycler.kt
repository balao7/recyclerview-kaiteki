package com.kroegerama.kaiteki.recyclerview.example

import android.graphics.Point
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.kroegerama.kaiteki.baseui.BaseFragment
import com.kroegerama.kaiteki.recyclerview.KaitekiSwipeCallback
import com.kroegerama.kaiteki.recyclerview.example.model.ImageItem
import com.kroegerama.kaiteki.recyclerview.itemdecoration.StickyHeaderDecoration
import kotlinx.android.synthetic.main.frag_recycler.*

class FragRecycler : BaseFragment(R.layout.frag_recycler) {

//    private var sharedImage: View? = null
//    private var sharedTitle: View? = null

    override fun prepare() {
//        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
//
//        enterTransition = Fade(Fade.MODE_IN)
//        exitTransition = Fade(Fade.MODE_OUT)
//
//        sharedElementEnterTransition =
//            TransitionInflater.from(context).inflateTransition(R.transition.image_detail)
    }

    override fun saveState(outState: Bundle) {
        outState.putFloat("progress", coordinator.progress)
    }

    override fun loadState(state: Bundle) {
        coordinator.progress = state.getFloat("progress", 0f)
    }

    override fun setupGUI() {
//        ViewCompat.requestApplyInsets(coordinator)
//        coordinator.getConstraintSet(R.id.collapsed).let {
//            val h = it.getHeight(R.id.ivTitle) + (context?.getStatusBarHeight() ?: 0)
//            it.constrainHeight(R.id.ivTitle, h)
//        }
//        coordinator.getConstraintSet(R.id.expanded).let {
//            val h = it.getHeight(R.id.ivTitle) + (context?.getStatusBarHeight() ?: 0)
//            it.constrainHeight(R.id.ivTitle, h)
//        }

        ViewCompat.setTransitionName(ivTitle, "topBar")
        val adapter = ExampleAdapter(
            requireContext(),
            mapOf(
                "January" to listOf(
                    ImageItem("One", "https://picsum.photos/800/1200?random=1"),
                    ImageItem("Two", "https://picsum.photos/800/1200?random=2"),
                    ImageItem("Three", "https://picsum.photos/800/1200?random=3"),
                    ImageItem("Four", "https://picsum.photos/800/1200?random=4"),
                    ImageItem("Five", "https://picsum.photos/800/1200?random=5")
                ),
                "February" to listOf(
                    ImageItem("Hello World", "https://picsum.photos/800/1200?random=6"),
                    ImageItem("Hallo Welt", "https://picsum.photos/800/1200?random=7")
                ),
                "March" to listOf(
                    ImageItem("Six", "https://picsum.photos/800/1200?random=8"),
                    ImageItem("Seven", "https://picsum.photos/800/1200?random=9"),
                    ImageItem("Eight", "https://picsum.photos/800/1200?random=10")
                ),
                "April" to listOf(
                    ImageItem("Nine", "https://picsum.photos/800/1200?random=11"),
                    ImageItem("Ten", "https://picsum.photos/800/1200?random=12"),
                    ImageItem("Eleven", "https://picsum.photos/800/1200?random=13"),
                    ImageItem("Twelve", "https://picsum.photos/800/1200?random=14")
                ),
                "May" to listOf(
                    ImageItem("Alpha", "https://picsum.photos/800/1200?random=15"),
                    ImageItem("Beta", "https://picsum.photos/800/1200?random=16"),
                    ImageItem("Gamma", "https://picsum.photos/800/1200?random=17")
                ),
                "June" to listOf(
                    ImageItem("First", "https://picsum.photos/800/1200?random=18"),
                    ImageItem("Second", "https://picsum.photos/800/1200?random=19"),
                    ImageItem("Third", "https://picsum.photos/800/1200?random=20"),
                    ImageItem("4th", "https://picsum.photos/800/1200?random=21"),
                    ImageItem("5th", "https://picsum.photos/800/1200?random=22")
                ),
                "July" to (1..10).map {
                    ImageItem("$it", "https://picsum.photos/800/1200?random=${it + 22}")
                }.toList()
            )
        ) { item, image, title ->
            (activity as? MainActivity)?.startDetail(item, image, title, ivTitle)
        }
        val size = Point().apply(requireActivity().windowManager.defaultDisplay::getSize)
        val sizeProvider = FixedPreloadSizeProvider<ImageItem>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        val preloader = RecyclerViewPreloader(Glide.with(this), adapter, sizeProvider, 10)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        recycler.addItemDecoration(StickyHeaderDecoration(adapter))
        recycler.addOnScrollListener(preloader)

        val callback = KaitekiSwipeCallback(
            KaitekiSwipeCallback.TextSwipeItem(
                requireContext(), R.color.colorAccent, R.drawable.ic_android, R.string.chip_text,
                R.dimen.abc_text_size_caption_material, R.color.abc_btn_colored_text_material
            ) {

            }, KaitekiSwipeCallback.TextSwipeItem(
                requireContext(), R.color.colorAccent, R.drawable.ic_android, R.string.abc_capital_on,
                R.dimen.abc_text_size_caption_material, R.color.abc_btn_colored_text_material
            ) {

            }
        )
        ItemTouchHelper(callback).attachToRecyclerView(recycler)

//        startPostponedEnterTransition()
    }

//    override fun decorateTransaction(transaction: FragmentTransaction) {
//        val image = sharedImage ?: return
//        val title = sharedTitle ?: return
//        val tnImage = ViewCompat.getTransitionName(image) ?: return
//        val tnTitle = ViewCompat.getTransitionName(title) ?: return
//        transaction.addSharedElement(image, tnImage)
//        transaction.addSharedElement(title, tnTitle)
//    }

//    fun Context.getStatusBarHeight(): Int {
//        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceId > 0) {
//            return resources.getDimensionPixelSize(resourceId)
//        }
//        return 20f.dpToPx()
//    }
}