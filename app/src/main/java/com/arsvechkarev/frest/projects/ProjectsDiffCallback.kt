package com.arsvechkarev.frest.projects

import androidx.recyclerview.widget.DiffUtil
import com.arsvechkarev.frest.models.main.Project

/**
 * Diff callback to update recycler projects data
 *
 * @author Arseniy Svechkarev
 * @see ProjectsListFragment
 */
class ProjectsDiffCallback(
  private val oldList: List<Project>?,
  private val newList: List<Project>?
) : DiffUtil.Callback() {
  
  override fun getOldListSize() = oldList?.size ?: 0
  override fun getNewListSize() = newList?.size ?: 0
  
  override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    val oldProject = oldList!![oldPosition]
    val newProject = newList!![newPosition]
    return oldProject.name == newProject.name
  }
  
  override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    val oldProject = oldList!![oldPosition]
    val newProject = newList!![newPosition]
    return oldProject == newProject
  }
}
