package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView


class MaxHeightRecyclerView : RecyclerView {
    private var maxHeight = 0
    private val defaultHeight = 200

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            init(context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode) {
            init(context, attrs)
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttrs: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
            //200 is a defualt value
            maxHeight = styledAttrs.getDimensionPixelSize(
                R.styleable.MaxHeightRecyclerView_maxHeight,
                defaultHeight
            )
            styledAttrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, unused: Int) {
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}