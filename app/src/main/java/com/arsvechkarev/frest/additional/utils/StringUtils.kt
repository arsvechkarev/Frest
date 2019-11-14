package com.arsvechkarev.frest.additional.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun String.consistOfSpaces(): Boolean = (this.isNotEmpty()) and (this.trim().isEmpty())

fun String.reduceIfLong() = if (this.length > 9) this.substring(0, 8) + "..." else this

fun Double.toPrettyString(): String {
  val symbols = DecimalFormatSymbols(Locale.US)
  return DecimalFormat("#0\u002E00", symbols).format(this)
}