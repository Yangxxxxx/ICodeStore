package com.example.administrator.sometest.KotlinTest

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Administrator on 2018/1/18 0018.
 */

class TestView : FrameLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
}
