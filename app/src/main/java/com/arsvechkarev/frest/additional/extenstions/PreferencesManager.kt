package com.arsvechkarev.frest.additional.extenstions

interface PreferencesManager {
  
  fun getInt(key: String, defaultValue: Int): Int
  
  fun getString(key: String, defaultValue: String): String?
  
  fun putInt(key: String, value: Int)
  
  fun putString(key: String, value: String)
}