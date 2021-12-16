package com.rikkei.tranning.basekotlin

import android.content.Context
import android.widget.Toast

fun Context.showToastLong(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()

}

fun Context.showToastShort(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}
