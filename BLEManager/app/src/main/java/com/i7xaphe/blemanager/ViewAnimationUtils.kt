package com.i7xaphe.blemanager

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


import android.widget.LinearLayout

object ViewAnimationUtils {

    fun expand(v: View) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targtetHeight = v.getMeasuredHeight()

        v.getLayoutParams().height = 0
        v.setVisibility(View.VISIBLE)
        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.getLayoutParams().height = if (interpolatedTime == 1f)
                    LinearLayout.LayoutParams.WRAP_CONTENT
                else
                    (targtetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.setDuration(((targtetHeight / v.getContext().getResources().getDisplayMetrics().density) as Int).toLong())
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.getMeasuredHeight()

        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.setVisibility(View.GONE)
                } else {
                    v.getLayoutParams().height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.setDuration(((initialHeight / v.getContext().getResources().getDisplayMetrics().density) as Int).toLong())
        v.startAnimation(a)
    }
}