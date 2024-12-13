package com.alaturing.incidentify.common.validation

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout


fun EditText.isNotEmpty(label:TextInputLayout?=null):Boolean {
    val isNotEmpty = this.text.isNotEmpty()
    label?.error = if (isNotEmpty) null else "Field cannot be empty"
    return isNotEmpty
}

fun EditText.sameContent(compare:EditText,label1:TextInputLayout?=null,label2: TextInputLayout?=null):Boolean {
    val sameContent = this.text.toString() == compare.text.toString()
    label1?.error = if (sameContent) null else "Does not match"
    label2?.error = if (sameContent) null else "Does not match"
    return sameContent

}