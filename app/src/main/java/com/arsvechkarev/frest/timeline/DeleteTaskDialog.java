package com.arsvechkarev.frest.timeline;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.home.managers.HomeFragmentPresenter;
import com.arsvechkarev.frest.models.main.Task;

/**
 * Alert dialog when user wants to delete task
 *
 * @author Arseniy Svechkarev
 * @see HomeFragmentPresenter
 */
public class DeleteTaskDialog {

  @BindView(R.id.text_task_name)
  public TextView textName;

  @BindView(R.id.text_task_duration)
  public TextView textDuration;

  // Clickable textView for deleting
  @BindView(R.id.text_delete_task)
  public TextView textDelete;

  // Task for presenting views color and text
  private final Task task;

  private AlertDialog alertDialog;
  private final Context context;
  private final DeleteTaskListener deleteTaskListener;

  public DeleteTaskDialog(Context context, Task task,
      DeleteTaskListener deleteTaskListener) {
    this.context = context;
    this.task = task;
    this.deleteTaskListener = deleteTaskListener;
  }

  public void show() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View view = LayoutInflater.from(context)
        .inflate(R.layout.dialog_delete_record, null);
    builder.setView(view);
    alertDialog = builder.create();
    assert alertDialog.getWindow() != null;
    alertDialog.getWindow().setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT));
    ButterKnife.bind(this, view);
    textName.setText(task.getName());
    textName.setTextColor(task.getColor());
    textDuration.setText(task.getFormattedDuration());
    textDuration.setTextColor(task.getColor());
    alertDialog.show();
  }

  /**
   * When user clicks "Delete project"
   */
  @OnClick(R.id.text_delete_task)
  void onDeleteTextViewClick() {
    deleteTaskListener.onAgreeToDelete(task);
    alertDialog.dismiss();
  }

  /**
   * Interface for checking what project user deletes
   */
  public interface DeleteTaskListener {

    void onAgreeToDelete(Task task);
  }
}
