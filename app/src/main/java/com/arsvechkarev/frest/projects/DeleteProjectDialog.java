package com.arsvechkarev.frest.projects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.models.main.Project;

/**
 * Alert dialog appears when user makes long click on project list item
 *
 * @author Arseniy Svechkarev
 * @see ProjectsAdapter
 * @see ProjectsListFragment
 */
public class DeleteProjectDialog {

  @BindView(R.id.textProjectName)
  public TextView textName;

  @BindView(R.id.divider)
  View divider;

  // Clickable textView for deleting
  @BindView(R.id.text_delete_project)
  public TextView textDelete;

  // Layout shows after user clicks textDelete
  @BindView(R.id.layout_really_delete)
  public LinearLayout layoutYesNo;

  //Textview that shows after user clicks textDelete
  @BindView(R.id.text_project_description)
  public TextView textDescription;

  @BindView(R.id.text_no)
  public TextView textNo;

  // Dismiss dialog card
  @BindView(R.id.card_no)
  public CardView cardNo;

  // Really deleting project card
  @BindView(R.id.card_yes)
  public CardView cardYes;

  /**
   * Listener for {@link DeleteProjectListener}
   */
  private final DeleteProjectListener deleteProjectListener;

  private AlertDialog alertDialog;
  private final Context context;

  // Project instance to set text of textView name
  private final Project project;

  DeleteProjectDialog(Context context, Project project,
      DeleteProjectListener deleteProjectListener) {
    this.context = context;
    this.project = project;
    this.deleteProjectListener = deleteProjectListener;
  }

  void k(int l, Runnable r) {

  }

  void show() {
    k(9, () -> textDelete.setText("F"));
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View view = LayoutInflater.from(context)
        .inflate(R.layout.dialog_delete_project, null);
    builder.setView(view);
    alertDialog = builder.create();
    assert alertDialog.getWindow() != null;
    alertDialog.getWindow().setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT));
    ButterKnife.bind(this, view);
    textName.setText(project.getName());
    textName.setTextColor(project.getColor());
    textNo.setTextColor(project.getColor());
    cardNo.setCardBackgroundColor(project.getColor());
    cardYes.setCardBackgroundColor(project.getColor());
    alertDialog.show();
  }

  @OnClick(R.id.text_delete_project)
  void onDeleteTextViewClick() {
    divider.setVisibility(GONE);
    textDelete.setVisibility(GONE);
    textDescription.setVisibility(VISIBLE);
    layoutYesNo.setVisibility(VISIBLE);
  }

  @OnClick(R.id.card_inner_no)
  void onNoClick() {
    alertDialog.dismiss();
  }

  // Really delete project
  @OnClick(R.id.card_yes)
  void onYesClick() {
    deleteProjectListener.onDeleteProject(project);
    alertDialog.dismiss();
  }
}
