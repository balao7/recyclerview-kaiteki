package com.kroegerama.kaiteki.recyclerview.example

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kroegerama.kaiteki.baseui.BaseFragmentActivity
import com.kroegerama.kaiteki.recyclerview.example.model.ImageItem
import com.kroegerama.kaiteki.startActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseFragmentActivity<Navigation>(
    layout = R.layout.activity_main,
    fragmentContainer = R.id.container,
    startIndex = Navigation.Main
) {

    override fun loadIndexState(key: String, bundle: Bundle) = bundle.getString(key).let {
        try {
            Navigation.valueOf(it!!)
        } catch (e: IllegalArgumentException) {
            Navigation.Main
        }
    }

    override fun saveIndexState(index: Navigation, key: String, bundle: Bundle) {
        bundle.putString(key, index.name)
    }

    override fun setupGUI() {
        ViewCompat.setTransitionName(bottomNav, "bottomNav")
    }

    override fun run(runState: RunState) {
        if (!navigator.hasSelection) navigate(Navigation.Main)

        startActivity<AcSimple>()
    }

    override fun createFragment(index: Navigation, payload: Any?) = when (index) {
        Navigation.Main -> FragRecycler()
    }

    override fun FragmentTransaction.decorate(fromIndex: Navigation?, toIndex: Navigation, fragment: Fragment) {
        setReorderingAllowed(true)
    }

    fun gotoMain() {
        navigate(Navigation.Main)
    }

    fun startDetail(item: ImageItem, image: View, title: View, topNav: View) {
        val options = activityOptions(this) {
            add(image)
            add(title)

            add(topNav)
            add(bottomNav)

            window?.decorView?.let { decor ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    decor.findViewById<View>(android.R.id.navigationBarBackground)?.let { add(it) }
                    decor.findViewById<View>(android.R.id.statusBarBackground)?.let { add(it) }
                }
            }
        }
        AcDetail.start(
            this, options, item,
            ViewCompat.getTransitionName(image).orEmpty(),
            ViewCompat.getTransitionName(title).orEmpty()
        )
    }
}

enum class Navigation {
    Main
}