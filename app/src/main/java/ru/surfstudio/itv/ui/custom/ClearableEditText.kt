package ru.surfstudio.itv.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener


/**
 * To clear icon can be changed via
 *
 * <pre>
 * android:drawable(Right|Left)="@drawable/custom_icon"
</pre> *
 */
class ClearableEditText : EditText, OnTouchListener, OnFocusChangeListener, TextWatcher {

    private val loc: Location = Location.END

    private var xD: Drawable? = null

    private var l: OnTouchListener? = null
    private var f: OnFocusChangeListener? = null
    private val hintText = hint

    private val displayedDrawable: Drawable?
        get() = compoundDrawablesRelative[loc.idx]

    enum class Location constructor(internal val idx: Int) {
        START(0), END(2)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        this.l = l
    }

    override fun setOnFocusChangeListener(f: OnFocusChangeListener) {
        this.f = f
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (displayedDrawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val left = if (loc == Location.START) 0 else width - paddingRight - xD!!.intrinsicWidth
            val right = if (loc == Location.START) paddingLeft + xD!!.intrinsicWidth else width
            val tappedX = x in left..right && y >= 0 && y <= bottom - top
            if (tappedX) {
                if (event.action == MotionEvent.ACTION_UP) {
                    setText("")
                }
                return true
            }
        }
        return l?.onTouch(v, event) ?: false
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(!text.isEmpty())
        } else {
            setClearIconVisible(false)
        }
        f?.onFocusChange(v, hasFocus)
    }

    override fun setCompoundDrawablesRelative(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawablesRelative(start, top, end, bottom)
        initIcon()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (isFocused) {
            setClearIconVisible(!(text?.isEmpty() ?: true))
        }
    }

    override fun afterTextChanged(s: Editable?) {
        //pass
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // pass
    }


    private fun init() {
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(this)
        initIcon()
        setClearIconVisible(false)
    }

    private fun initIcon() {
        xD = displayedDrawable
        if (xD == null) {
            xD = ContextCompat.getDrawable(context, android.R.drawable.presence_offline)
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        val cd = compoundDrawablesRelative
        val displayed = displayedDrawable
        val wasVisible = displayed != null
        if (visible != wasVisible) {
            hint = if (visible) "" else hintText
            val x = if (visible) xD else null
            super.setCompoundDrawablesRelative(cd[0], cd[1], x, cd[3])
        }
    }
}
