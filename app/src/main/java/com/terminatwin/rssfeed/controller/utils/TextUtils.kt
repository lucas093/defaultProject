package com.terminatwin.rssfeed.controller.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

object TextUtils {
    fun isEmpty(text: String?): Boolean {
        return text == null || text.trim { it <= ' ' }.isEmpty()
    }

    fun setTextViewTypeface(context: Context, vararg textViews: TextView) {
        val type = Typeface.createFromAsset(context.assets, "fonts/roboto/Roboto-Light.ttf")
        for (txtVw in textViews)
            txtVw.typeface = type
    }

    fun setEditTextTypeface(context: Context, vararg editTexts: EditText) {
        val type = Typeface.createFromAsset(context.assets, "fonts/roboto/Roboto-Light.ttf")
        for (txtVw in editTexts)
            txtVw.typeface = type
    }

    fun setButtonTypeface(context: Context, vararg buttons: Button) {
        val type = Typeface.createFromAsset(context.assets, "fonts/roboto/Roboto-Light.ttf")
        for (button in buttons)
            button.typeface = type
    }

    fun capitalize(name: String): String? {
        var name = name
        if (isEmpty(name))
            return name

        name = name.toLowerCase()

        return ("" + name[0]).toUpperCase() + name.substring(1)
    }
}