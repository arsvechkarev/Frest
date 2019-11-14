package com.arsvechkarev.frest.projects

import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.reports.ReportsActivity
import com.arsvechkarev.frest.starttask.StartTaskActivity

/**
 * Interface for checking when user clicks on any project item in project list
 *
 * @author Arseniy Svechkarev
 * @see StartTaskActivity
 *
 * @see ReportsActivity
 */
interface OnProjectClickListener {
  
  fun onProjectClick(project: Project)
}
