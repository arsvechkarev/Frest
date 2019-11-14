package com.arsvechkarev.frest.additional.extenstions

import android.content.Context

class AndroidPreferencesManager(context: Context) : PreferencesManager {
  
  companion object {
    private const val FILE_PREFERENCES = "BasePreferences"
  }
  
  private val preferences = context.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
  
  override fun getInt(key: String, defaultValue: Int): Int {
    return preferences.getInt(key, defaultValue)
  }
  
  override fun getString(key: String, defaultValue: String): String? {
    return preferences.getString(key, defaultValue)
  }
  
  override fun putInt(key: String, value: Int) {
    preferences.edit().putInt(key, value).apply()
  }
  
  override fun putString(key: String, value: String) {
    preferences.edit().putString(key, value).apply()
  }
  
}
