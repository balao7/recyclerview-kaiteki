package com.kroegerama.kaiteki.recyclerview.example

import android.graphics.drawable.Drawable
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kroegerama.kaiteki.baseui.BaseFragment
import com.kroegerama.kaiteki.bundle
import com.kroegerama.kaiteki.recyclerview.example.model.ImageItem
import com.kroegerama.kaiteki.recyclerview.example.view.PullDismissLayout
import kotlinx.android.synthetic.main.frag_detail.*

class FragDetail : BaseFragment( R.layout.frag_detail), PullDismissLayout.Listener {

    private val argItem by lazy {
        val title = arguments?.getString(ARG_TITLE).orEmpty()
        val url = arguments?.getString(ARG_URL).orEmpty()
        ImageItem(title, url)
    }
    private val argTnImage by lazy { arguments?.getString(ARG_IMAGE_TN).orEmpty() }
    private val argTnTitle by lazy { arguments?.getString(ARG_TITLE_TN).orEmpty() }

    override fun prepare() {
        postponeEnterTransition()

        enterTransition = Fade(Fade.MODE_IN)
        exitTransition = Fade(Fade.MODE_OUT)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_detail)
    }

    override fun setupGUI() {
        tvTitle.text = argItem.title
        loadImage()

        setupPullDismiss()

        ViewCompat.setTransitionName(ivImage, argTnImage)
        ViewCompat.setTransitionName(tvTitle, argTnTitle)
    }

    private fun setupPullDismiss() {
        pullDismiss.listener = this
        pullDismiss.removeView = false
        pullDismiss.animateAlpha = true
        pullDismiss.heightPercent = .5f
        pullDismiss.alphaPercent = .5f
    }

    override fun onDismissed() {
        (activity as? MainActivity)?.gotoMain()
    }

    override fun onShouldInterceptTouchEvent() = scrollView.scrollY != 0

    private fun loadImage() {
        Glide.with(this)
            .load(argItem.imageUrl)
            .apply(RequestOptions.noTransformation())
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            }).into(ivImage)
    }

    override fun decorateTransaction(transaction: FragmentTransaction) {
        val tnImage = ViewCompat.getTransitionName(ivImage) ?: return
        val tnTitle = ViewCompat.getTransitionName(tvTitle) ?: return
        transaction.addSharedElement(ivImage, tnImage)
        transaction.addSharedElement(tvTitle, tnTitle)
    }

    companion object {
        private const val TAG = "FragDetail"

        private const val ARG_TITLE = "title"
        private const val ARG_URL = "url"
        private const val ARG_IMAGE_TN = "tn_image"
        private const val ARG_TITLE_TN = "tn_title"

        fun makeInstance(item: ImageItem, tnImage: String, tnTitle: String) = FragDetail().apply {
            arguments = bundle {
                putString(ARG_TITLE, item.title)
                putString(ARG_URL, item.imageUrl)
                putString(ARG_IMAGE_TN, tnImage)
                putString(ARG_TITLE_TN, tnTitle)
            }
        }
    }
}