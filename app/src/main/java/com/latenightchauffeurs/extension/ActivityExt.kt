package com.latenightchauffeurs.extension

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.latenightchauffeurs.R

/**
 * Create by Sirumalayil on 01-04-2023.
 */

fun AppCompatActivity.navigate(
    fragment: Fragment,
    addToBackStack: Boolean = true,
    animation: Boolean = true,
    backStackName: String = "",
) {

    val rootView: ViewGroup = findViewById(android.R.id.content)
    val container = rootView.findViewById<FrameLayout>(R.id.container)
        ?: throw Throwable("Activity FrameLayout id needs to be \"container\"")
    if (animation) {
        add(fragment, container.id, addToBackStack, backStackName)
    } else {
        addWithoutAnim(
            fragment,
            container.id,
            addToBackStack,
            backStackName
        )
    }
}

fun AppCompatActivity.add(
    fragment: Fragment,
    @IdRes container: Int,
    addToBackStack: Boolean = false,
    backStackName: String = "",
    @AnimRes inAnimationRes: Int = R.anim.right_in,
    @AnimRes outAnimationRes: Int = R.anim.left_out,
    @AnimRes popInAnimationRes: Int = R.anim.left_in,
    @AnimRes popOutAnimationRes: Int = R.anim.right_out
) {
    supportFragmentManager.transact {
        if (inAnimationRes != 0 && outAnimationRes != 0) {
            setCustomAnimations(
                inAnimationRes,
                outAnimationRes,
                popInAnimationRes,
                popOutAnimationRes
            )

        }
        add(container, fragment, backStackName)
        if (addToBackStack) addToBackStack(backStackName)
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

fun AppCompatActivity.addWithoutAnim(
    fragment: Fragment,
    @IdRes container: Int,
    addToBackStack: Boolean = false,
    backStackName: String = ""
) {
    supportFragmentManager.transact {
        add(container, fragment, backStackName)
        if (addToBackStack) addToBackStack(backStackName)
    }
}
