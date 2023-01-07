package br.com.weatherapp.base

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import br.com.weatherapp.R


class SearchEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.SearchEditText
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DRAWABLE_AREA = 2
    }


    private var listener: ((String) -> Unit)? = null
    private val editText: EditText
    private val errorTextView: TextView


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
        val hint = typedArray.getString(R.styleable.SearchEditText_searchHint)
        orientation = VERTICAL
        val view = View.inflate(context, R.layout.view_search_edit_text, this)
        editText = view.findViewById(R.id.edit_text)
        errorTextView = view.findViewById(R.id.error_text)

        setDrawableToEnd(R.drawable.ic_baseline_search_24)
        setupDrawable()
        setupImeActions()
        setupHint(hint)
        setupTouchEvent()

        typedArray.recycle()
    }

    private fun setupHint(hint: String?) {
        hint?.let { editText.hint = it }
    }

    fun setOnSearchListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    private fun setupDrawable() {
        editText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                setDrawableToEnd(R.drawable.ic_baseline_search_24)
            } else {
                setDrawableToEnd(R.drawable.ic_baseline_close_24)
            }
        }
    }

    private fun setupTouchEvent() {
        editText.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val leftEdgeOfRightDrawable: Int =
                        (right - editText.compoundDrawables[DRAWABLE_AREA].bounds.width())
                    if (motionEvent.rawX >= leftEdgeOfRightDrawable) {
                        if (!editText.text.isNullOrEmpty()) {
                            editText.text?.clear()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    performClick()
                }

            }
            performClick()
        }
    }

    fun setError(errorMessage: String) {
        errorTextView.show()
        errorTextView.text = errorMessage
    }

    fun clearError() {
        errorTextView.gone()
        errorTextView.text = ""
    }

    fun getText(): String = editText.text.toString()

    fun setText(city: String) {
        editText.setText(city)
    }

    private fun setupImeActions() {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listener?.invoke(editText.text.toString())
                context.hideKeyboard(editText)
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setDrawableToEnd(@DrawableRes id: Int) {
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            id,
            0
        )
    }


}