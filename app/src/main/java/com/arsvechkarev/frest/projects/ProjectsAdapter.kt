package com.arsvechkarev.frest.projects

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import butterknife.BindDimen
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnLongClick
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.projects.ProjectsAdapter.ProjectViewHolder
import com.arsvechkarev.frest.reports.ReportsActivity
import com.arsvechkarev.frest.starttask.StartTaskActivity
import java.util.ArrayList

/**
 * Recycler adapter for presenting project list
 *
 * @author Arseniy Svechkarev
 * @see ProjectsManager
 */
class ProjectsAdapter(
  var data: ArrayList<Project>?,
  private val context: Context,
  private val itemActionsListener: ProjectItemActionsListener
) : RecyclerView.Adapter<ProjectViewHolder>() {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
    val view = LayoutInflater.from(context)
        .inflate(R.layout.item_project, parent, false)
    return ProjectViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
    val project = data!![position]
    holder.textProjectName!!.text = project.name
    holder.textProjectName!!.setTextColor(project.color)
  }
  
  override fun getItemCount(): Int {
    return data!!.size
  }
  
  
  /**
   * View holder for project item
   */
  inner class ProjectViewHolder(itemView: View) : ViewHolder(itemView) {
    
    @BindDimen(R.dimen.text_standard)
    var reportsTextSize: Float = 0.toFloat()
    
    @BindView(R.id.textProjectName)
    var textProjectName: TextView? = null
    
    init {
      ButterKnife.bind(this, itemView)
      // If recycler shows in ReportsActivity
      if (context is ReportsActivity) {
        textProjectName!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, reportsTextSize)
      }
    }
    
    @OnClick(R.id.card_task)
    fun onCardClick() {
      val project = data!![adapterPosition]
      itemActionsListener.onProjectItemClick(project)
    }
    
    @OnLongClick(R.id.card_task)
    fun onCardLongClick(): Boolean {
      if (context is StartTaskActivity) {
        val project = data!![adapterPosition]
        itemActionsListener.onProjectItemLongClick(project)
        return true
      }
      return false
    }
  }
  
}
