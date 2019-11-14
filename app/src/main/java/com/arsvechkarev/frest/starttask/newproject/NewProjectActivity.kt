package com.arsvechkarev.frest.starttask.newproject

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.Codes.Result
import com.arsvechkarev.frest.additional.ProjectColors.RED
import com.arsvechkarev.frest.additional.utils.afterTextChanged
import com.arsvechkarev.frest.projects.ProjectsManager
import com.arsvechkarev.frest.starttask.newproject.ColorPresentationManager.ProjectColorChangeListener
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_ALREADY_EXISTS
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_CONSIST_JUST_OF_SPACES
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_IS_EMPTY
import kotlinx.android.synthetic.main.activity_new_project.cardSaveProject
import kotlinx.android.synthetic.main.activity_new_project.editTextProjectName
import kotlinx.android.synthetic.main.activity_new_project.textWarning
import kotlinx.android.synthetic.main.partial_toolbar_new_project.toolbar
import kotlinx.android.synthetic.main.partial_toolbar_new_project.toolbarTextSaveProject

/**
 * This activity used to creating new projects. Contains [.editTextName] where user
 * have to enter project name, and also image views with possible project colors
 *
 * @author Arseniy Svechkarev
 */
class NewProjectActivity : AppCompatActivity(), ProjectCreator,
  ProjectColorChangeListener {
  
  /** Main presenter */
  private lateinit var presenter: NewProjectPresenter
  
  /** Project color that we will change */
  override var projectColor: Int = RED.color
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_project)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    ButterKnife.bind(this)
    presenter = NewProjectPresenter(this, ProjectsManager(this))
    val colorsManager = ColorPresentationManager(this)
    colorsManager.initColorViews()
  }
  
  override fun onResume() {
    super.onResume()
    setAfterTextChangedListener()
    editTextProjectName.requestFocus()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    presenter.detachView()
  }
  
  private fun setAfterTextChangedListener() {
    editTextProjectName.afterTextChanged {
      presenter.afterTextChanged(it)
    }
  }
  
  /**
   * When color changed
   *
   * @see ColorPresentationManager.onImageClick
   */
  override fun onProjectColorChanged(color: Int) {
    projectColor = color
  }
  
  @Suppress("UNUSED_PARAMETER")
  fun onSaveProjectClick(clickedView: View) {
    presenter.saveProject(editTextProjectName.text.toString())
    setResult(Result.PROJECT_CREATED)
    finish()
  }
  
  /** When project name is fully correct and project can be saved */
  override fun onAllowToSave() {
    textWarning.visibility = INVISIBLE
    cardSaveProject.visibility = VISIBLE
    toolbarTextSaveProject.visibility = VISIBLE
  }
  
  /**
   * When project name is not correct
   *
   * @param reason Reason to know why saving has rejected
   */
  override fun onRejectToSave(reason: Int) {
    textWarning.visibility = VISIBLE
    cardSaveProject.visibility = INVISIBLE
    toolbarTextSaveProject.visibility = INVISIBLE
    when (reason) {
      NAME_CONSIST_JUST_OF_SPACES -> // Just invisible card save and do not show message
        textWarning.visibility = INVISIBLE
      NAME_IS_EMPTY -> textWarning.text = getString(R.string.error_project_name_is_empty)
      NAME_ALREADY_EXISTS -> textWarning.text = getString(R.string.error_project_already_exists)
    }
  }
}
