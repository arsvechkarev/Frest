package com.arsvechkarev.frest.additional.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.ProjectColors.BLUE
import com.arsvechkarev.frest.additional.ProjectColors.BLUE_GRAY
import com.arsvechkarev.frest.additional.ProjectColors.BROWN
import com.arsvechkarev.frest.additional.ProjectColors.CYAN
import com.arsvechkarev.frest.additional.ProjectColors.GREEN
import com.arsvechkarev.frest.additional.ProjectColors.ORANGE
import com.arsvechkarev.frest.additional.ProjectColors.PINK
import com.arsvechkarev.frest.additional.ProjectColors.PURPLE
import com.arsvechkarev.frest.additional.ProjectColors.RED
import com.arsvechkarev.frest.additional.ProjectColors.TEAL
import com.arsvechkarev.frest.models.main.Project

/**
 * Returns [DatePickerDialog] or [TimePickerDialog] theme depends on project
 * parameter
 */
fun getPickerDialogStyleId(project: Project): Int {
  return when (project.color) {
    RED.color -> R.style.DialogPicker_RED_Theme
    ORANGE.color -> R.style.DialogPicker_ORANGE_Theme
    TEAL.color -> R.style.DialogPicker_TEAL_Theme
    BLUE_GRAY.color -> R.style.DialogPicker_BLUE_GRAY_Theme
    CYAN.color -> R.style.DialogPicker_CYAN_Theme
    BROWN.color -> R.style.DialogPicker_BROWN_Theme
    BLUE.color -> R.style.DialogPicker_BLUE_Theme
    PURPLE.color -> R.style.DialogPicker_PURPLE_Theme
    PINK.color -> R.style.DialogPicker_PINK_Theme
    GREEN.color -> R.style.DialogPicker_GREEN_Theme
    else -> throw IllegalStateException("Color doesn't exists")
  }
}