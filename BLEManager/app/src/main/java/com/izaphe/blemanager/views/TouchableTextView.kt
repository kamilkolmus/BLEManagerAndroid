package com.izaphe.blemanager.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by Kamil on 2018-03-29.
 */
class TouchableTextView : TextView {

    constructor(context: Context) : super(context)

    protected constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    override fun performClick(): Boolean {

        return false
    }
}