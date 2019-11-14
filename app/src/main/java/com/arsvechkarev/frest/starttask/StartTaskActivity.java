package com.arsvechkarev.frest.starttask;

import static com.arsvechkarev.frest.starttask.SessionOrRecordDialog.ACTION_ADD_RECORD;
import static com.arsvechkarev.frest.starttask.SessionOrRecordDialog.ACTION_START_SESSION;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.additional.Codes.Key;
import com.arsvechkarev.frest.additional.Codes.Request;
import com.arsvechkarev.frest.additional.Codes.Result;
import com.arsvechkarev.frest.models.main.Project;
import com.arsvechkarev.frest.projects.CreateProjectActionListener;
import com.arsvechkarev.frest.projects.OnProjectClickListener;
import com.arsvechkarev.frest.projects.ProjectsListFragment;
import com.arsvechkarev.frest.starttask.SessionOrRecordDialog.SelectingActionTypeListener;
import com.arsvechkarev.frest.starttask.newproject.NewProjectActivity;

/**
 * Activity where user can choose task to do. Contains of list of projects
 *
 * @author Arseniy Svechkarev
 * @see ProjectsListFragment
 */
public class StartTaskActivity extends AppCompatActivity implements
    CreateProjectActionListener, OnProjectClickListener, SelectingActionTypeListener {

  // Fragment with projects list
  private ProjectsListFragment mProjectsFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_task);
    mProjectsFragment = (ProjectsListFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_project_list);
  }

  @Override
  public void onCreateProjectClick() {
    Intent intent = new Intent(this, NewProjectActivity.class);
    startActivityForResult(intent, Request.Create_PROJECT);
  }

  @Override
  public void onProjectClick(Project project) {
    SessionOrRecordDialog alert = new SessionOrRecordDialog(this,
        project, this);
    alert.show();
  }

  @Override
  public void onActionSelected(int actionType, Intent data) {
    if (actionType == ACTION_START_SESSION) {
      setResult(Result.START_SESSION, data);
      finish();
    } else if (actionType == ACTION_ADD_RECORD) {
      Intent intentAddRecord = new Intent(this, AddRecordActivity.class);
      Project project = data.getParcelableExtra(Key.PROJECT);
      intentAddRecord.putExtra(Key.PROJECT_ADDING_RECORD, (Parcelable) project);
      startActivityForResult(intentAddRecord, Request.ADD_RECORD);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
      @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Request.ADD_RECORD) {
      if (resultCode == Result.ADD_RECORD_INTERIM) {
        setResult(Result.ADD_RECORD, data);
        finish();
      }
    } else if (requestCode == Request.Create_PROJECT) {
      if (resultCode == Result.PROJECT_CREATED) {
        mProjectsFragment.updateRecycler(true);
      }
    }
  }
}
