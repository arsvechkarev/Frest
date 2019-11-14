package com.arsvechkarev.frest.additional

import android.graphics.Color

/** Enum of colors for projects  */
enum class ProjectColors(private val colorHex: String) {
  
  RED("#F44336"),
  ORANGE("#FF6F00"),
  TEAL("#009688"),
  BLUE_GRAY("#577482"),
  CYAN("#2196F3"),
  BROWN("#795548"),
  BLUE("#3F51B5"),
  PURPLE("#9C27B0"),
  PINK("#E91E63"),
  GREEN("#4CAF50");
  
  val color: Int
    get() = Color.parseColor(colorHex)
}