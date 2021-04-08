package com.example.administrator.sometest.MeasureLayoutTest

import android.content.Context
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.administrator.sometest.R

/**
 * Created by ZN_mager on 2018/1/25.
 */
class ProfileNameLayout : ViewGroup {
    private val TAG = "ProfileNameLayout"

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 2) {
            val ivEdit = findViewById<View>(R.id.tv_name_edit)
            val editWidth = ivEdit.measuredWidth
            val editHeight = ivEdit.measuredHeight
            val tvName = findViewById<View>(R.id.tv_name)
            val nameWidth = tvName.measuredWidth
            val nameHeight = tvName.measuredHeight
            val nameLeft = (measuredWidth - nameWidth) / 2
            val nameRight = nameLeft + nameWidth
            val nameTop = (measuredHeight - nameHeight) / 2
            val nameBottom = nameTop + nameHeight

            var nameLeftRTL = nameLeft
            var nameRightRTL = nameRight
            if(isRtl()){
                nameLeftRTL = measuredWidth-nameRight
                nameRightRTL = measuredWidth-nameLeft
            }
            tvName.layout(nameLeftRTL, nameTop, nameRightRTL, nameBottom)

            val editParams = ivEdit.layoutParams as MarginLayoutParams

            val editLeft = nameRight + editParams.leftMargin
            val editRight = editLeft + editWidth
            val editTop = (measuredHeight - editHeight) / 2
            val editBottom = editTop + editHeight

            var editLeftRTL = editLeft
            var editRightRTL = editRight
            if(isRtl()){
                editLeftRTL = measuredWidth-editRight
                editRightRTL = measuredWidth-editLeft
            }
            ivEdit.layout(editLeftRTL, editTop, editRightRTL, editBottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measuredWidth = width
        var measuredHeight = 0
        if (childCount == 2) {
            val ivEdit = findViewById<View>(R.id.tv_name_edit)

            val editHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

            ivEdit.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), editHeightMeasureSpec)
            val editParams = ivEdit.layoutParams as MarginLayoutParams
            val tvName = findViewById<View>(R.id.tv_name)

            val nameHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

            tvName.measure(MeasureSpec.makeMeasureSpec(width - (ivEdit.measuredWidth + editParams.leftMargin + editParams.rightMargin) * 2, MeasureSpec.AT_MOST), nameHeightMeasureSpec)
            measuredHeight = Math.max(tvName.measuredHeight, ivEdit.measuredHeight)
        }
        setMeasuredDimension(measuredWidth, measuredHeight)

    }

    fun isRtl(): Boolean{
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

}