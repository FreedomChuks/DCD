package com.example.cariaid.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat


fun isValidEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.count() >= 6
}

fun Context.showMaterialDialog(
    title: String,
    message: String
): MaterialDialog {
    return MaterialDialog(this).show {
        cornerRadius(5F)
        title(text = title)
        message(text = message)
        positiveButton(text = "Ok")
    }
}


fun Fragment.showMaterialDialog(
    title: String="Error",
    message: String
): MaterialDialog {
    return MaterialDialog(requireContext()).show {
        cornerRadius(5F)
        title(text = title)
        message(text = message)
        positiveButton(text = "Ok")
    }
}

fun TextInputLayout.autoClearError(){
    this.editText?.doOnTextChanged { _, _, _, count ->
        if (count>0){
            this.error = null
            this.isErrorEnabled = false
        }
    }
}
val String.removeSpaces:String
    get() = this.replace("","")

val String.removeCurrency:String
    get() = this.replace("£","").removeSpaces

val String.removeComma: String
    get() =  this.replace(",", "").removeSpaces

val EditText.removeComma: String
    get() = this.trimmedText.removeComma

val EditText.trimmedText: String
    get() = this.text?.toString()?.trim() ?: ""

val Double.addComma: String
    get() =  DecimalFormat("##,###,###.##").format(this)

val Float.addComma: String
    get() =  DecimalFormat("##,###,###.##").format(this)

fun EditText.amountFormattingTextWatcher() {
    val amountEt = this
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val mValue = amountEt.removeComma
            if (mValue.isNotEmpty()) {
                val amv = mValue.replace(",", "").replace(" ", "")
                if (amv.isNotEmpty()) {
                    var value = 0.0
                    try {
                        value = java.lang.Double.parseDouble(amv)
                    } catch (ex: NumberFormatException) {
                        amountEt.error = "Invalid amount provided"
                    }
                    if (value > 0) {
                        amountEt.removeTextChangedListener(this)
                        amountEt.setText(value.addComma)

                        val length = amountEt.text?.length
                        if (length != null) {
                            amountEt.setSelection(length)
                        }
                        amountEt.addTextChangedListener(this)
                    }
                }
            }
        }
    })
}


