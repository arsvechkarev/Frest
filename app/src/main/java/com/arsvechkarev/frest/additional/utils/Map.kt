package com.arsvechkarev.frest.additional.utils


fun <K, V> Map<K, V>.getKey(value: V): K? {
  for (key in keys) {
    if (value == this[key]) {
      return key
    }
  }
  return null
}