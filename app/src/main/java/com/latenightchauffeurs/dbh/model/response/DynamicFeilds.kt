package com.latenightchauffeurs.dbh.model.response

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Create by Siru Malayil on 14-04-2023.
 */
/**
 * For checking editfields data dynamically using this data class
 */
data class CardDynamicFields(
    var editText: EditText? = null,
    var inputLayout: TextInputLayout? = null,
    var value: String?,
    var validated: Boolean = false
)
