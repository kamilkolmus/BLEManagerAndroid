package com.izaphe.blemanager.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet

import android.widget.ImageButton

class TouchableImageButton : ImageButton {

    constructor(context: Context) : super(context)

    protected constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean {
        return true
    }
}