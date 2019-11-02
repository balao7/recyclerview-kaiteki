package com.kroegerama.kaiteki.recyclerview.example

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kroegerama.kaiteki.baseui.BaseActivity
import com.kroegerama.kaiteki.recyclerview.example.model.ImageItem
import com.kroegerama.kaiteki.recyclerview.example.view.PullDismissLayout
import kotlinx.android.synthetic.main.frag_detail.*


class AcDetail : BaseActivity(R.layout.frag_detail), PullDismissLayout.Listener {

    private val argTnImage by lazy { intent?.getStringExtra(ARG_IMAGE_TN).orEmpty() }
    private val argTnTitle by lazy { intent?.getStringExtra(ARG_TITLE_TN).orEmpty() }
    private val argUrl by lazy { intent?.getStringExtra(ARG_URL).orEmpty() }
    private val argTitle by lazy { intent?.getStringExtra(ARG_TITLE).orEmpty() }

    override fun prepare() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val fade = Fade()
//            fade.excludeTarget(android.R.id.statusBarBackground, true)
//            fade.excludeTarget(android.R.id.navigationBarBackground, true)
//            window?.enterTransition = fade
//            window?.exitTransition = fade
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun setupGUI() {
        supportPostponeEnterTransition()

        ViewCompat.setTransitionName(ivImage, argTnImage)
        ViewCompat.setTransitionName(tvTitle, argTnTitle)
        ViewCompat.setTransitionName(dummyBottom, "topBar")
        ViewCompat.setTransitionName(dummyTop, "bottomNav")

        tvTitle.text = argTitle
        loadImage()

        setupPull()
    }

    private fun setupPull() {
        pullDismiss.listener = this
        pullDismiss.removeView = false
//        pullDismiss.animateAlpha = true
        pullDismiss.heightPercent = .5f
//        pullDismiss.alphaPercent = .5f
    }

    private fun loadImage() {
        Glide.with(this)
            .load(argUrl)
            .apply(RequestOptions.noTransformation())
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }
            }).into(ivImage)
    }

    override fun onDismissed() {
        supportFinishAfterTransition()
    }

    override fun onShouldInterceptTouchEvent() = scrollView.scrollY != 0

    companion object {
        private const val ARG_IMAGE_TN = "tn_image"
        private const val ARG_TITLE_TN = "tn_title"
        private const val ARG_URL = "url"
        private const val ARG_TITLE = "title"

        fun start(context: Context, options: ActivityOptionsCompat, item: ImageItem, tnImage: String, tnTitle: String) {
            val intent = Intent(context, AcDetail::class.java)
            intent.putExtra(ARG_IMAGE_TN, tnImage)
            intent.putExtra(ARG_TITLE_TN, tnTitle)

            intent.putExtra(ARG_URL, item.imageUrl)
            intent.putExtra(ARG_TITLE, item.title)

            startActivity(context, intent, options.toBundle())
//            (context as BaseActivity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}