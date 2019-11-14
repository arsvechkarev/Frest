package com.arsvechkarev.frest.projects

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.App
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.reports.ReportsActivity
import com.arsvechkarev.frest.starttask.StartTaskActivity
import kotlinx.android.synthetic.main.fragment_projects_list.cardNewProject
import kotlinx.android.synthetic.main.fragment_projects_list.noProjectsStub
import kotlinx.android.synthetic.main.fragment_projects_list.recyclerProjects
import kotlinx.android.synthetic.main.stub_template.stubDescription
import kotlinx.android.synthetic.main.stub_template.stubImage
import kotlinx.android.synthetic.main.stub_template.stubTitle
import java.util.ArrayList

/**
 * Fragment to presentation list of projects
 *
 * @author Arseniy Svechkarev
 *
 * @see ProjectsAdapter
 * @see ProjectsManager
 */
class ProjectsListFragment : androidx.fragment.app.Fragment(), DeleteProjectListener,
  ProjectItemActionsListener {
  
  private lateinit var unbinder: Unbinder
  private lateinit var activityContext: Context
  private lateinit var recyclerAdapter: ProjectsAdapter
  
  private var projectsList: ArrayList<Project>? = null
  private var layoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
  
  /** Manager for manipulating with projects list */
  private lateinit var projectsManager: ProjectsManager
  
  /** Listener to send callbacks when user clicks on project */
  private lateinit var projectClickListener: OnProjectClickListener
  
  /** Listener to send callbacks when user want to create new project */
  private lateinit var createProjectActionListener: CreateProjectActionListener
  
  override fun onAttach(context: Context) {
    super.onAttach(context)
    activityContext = activity as Context
    if (activityContext is StartTaskActivity) {
      createProjectActionListener = activityContext as CreateProjectActionListener
      projectClickListener = activityContext as OnProjectClickListener
    } else if (activityContext is ReportsActivity) {
      projectClickListener = activityContext as OnProjectClickListener
    }
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_projects_list, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view)
    projectsManager = ProjectsManager(activityContext)
    projectsList = projectsManager.allProjects
    recyclerAdapter = ProjectsAdapter(projectsList, activityContext, this)
    if (activityContext is ReportsActivity) applyReportsParams() else applyStandardParams()
    if (projectsList.isNullOrEmpty()) showNoTaskLayout() else prepareRecycler()
    initClickListeners()
  }
  
  private fun applyReportsParams() {
    recyclerProjects.setPadding(0, 0, 0, 0)
    cardNewProject.visibility = GONE
    stubImage.setImageDrawable(ContextCompat.getDrawable(activityContext,
      R.drawable.no_projects_reports_image))
    stubTitle.text = getString(R.string.text_no_projects_reports_title)
    stubDescription.visibility = GONE
  }
  
  private fun applyStandardParams() {
    stubImage.setImageDrawable(ContextCompat.getDrawable(activityContext,
      R.drawable.no_projects_image))
    stubTitle.text = getString(R.string.text_no_projects_title)
    stubDescription.text = getString(R.string.text_no_projects_description)
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    unbinder.unbind()
  }
  
  /** @see [makeRecyclerUpdate] */
  fun updateRecycler(scrollToTop: Boolean) {
    projectsManager.retrieveFile()
    projectsList = projectsManager.allProjects
    if (projectsList != null) {
      if (projectsList!!.isEmpty()) {
        makeRecyclerUpdate(scrollToTop)
        showNoTaskLayout()
      } else {
        if (recyclerAdapter.data != null && recyclerAdapter.data!!.isNotEmpty()) {
          makeRecyclerUpdate(scrollToTop)
        } else {
          prepareRecycler()
          makeRecyclerUpdate(scrollToTop)
        }
      }
    } else {
      showNoTaskLayout()
    }
  }
  
  /**
   * Sending callback about clicked project
   *
   * @see OnProjectClickListener.onProjectClick
   */
  override fun onProjectItemClick(project: Project) {
    projectClickListener.onProjectClick(project)
  }
  
  /** Handling delete [project] request */
  override fun onProjectItemLongClick(project: Project) {
    val dialog = DeleteProjectDialog(activityContext, project, this)
    dialog.show()
  }
  
  /** If user really wants to delete project */
  override fun onDeleteProject(project: Project) {
    projectsManager.deleteProject(project)
    updateRecycler(false)
  }
  
  /** Initializing recyclerTimeline and all his components */
  private fun prepareRecycler() {
    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activityContext)
        .apply {
          reverseLayout = true
          stackFromEnd = true
        }
    recyclerProjects.layoutManager = layoutManager
    recyclerProjects.adapter = recyclerAdapter
    showNormalLayout()
  }
  
  private fun initClickListeners() {
    cardNewProject.setOnClickListener {
      createProjectActionListener.onCreateProjectClick()
    }
  }
  
  /**
   * Updating recycler
   *
   * @param scrollToTop Flag to know if we need to scroll recyclerTimeline to top or not.
   * This scrolling needs to pass [App.screenHeight] as a parameter instead of
   * `recyclerTimeline.getBottom()`. This way is uses because standard method
   * returns 0 when this method invokes at first time, so date scroll goes to bottom
   * instead of top.
   */
  private fun makeRecyclerUpdate(scrollToTop: Boolean) {
    val diffCallback = ProjectsDiffCallback(recyclerAdapter.data, projectsList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    recyclerAdapter.data = projectsList
    diffResult.dispatchUpdatesTo(recyclerAdapter)
    if (scrollToTop && layoutManager != null) {
      recyclerProjects.smoothScrollToPosition(App.screenHeight)
    }
    showNormalLayout()
  }
  
  private fun showNormalLayout() {
    noProjectsStub.visibility = GONE
    recyclerProjects.visibility = VISIBLE
  }
  
  private fun showNoTaskLayout() {
    noProjectsStub.visibility = VISIBLE
    recyclerProjects.visibility = GONE
  }
}
