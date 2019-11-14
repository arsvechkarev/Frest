package com.arsvechkarev.frest.session;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.models.main.Project;
import com.arsvechkarev.frest.models.main.Task;
import com.arsvechkarev.frest.starttask.StartTaskActivity;

/**
 * Class for manipulating alert dialog which invokes when user want to cancel current
 * session
 *
 * @author Arseniy Svechkarev
 */
public class CancelSessionDialog {

  /**
   * When user want to cancel current task only
   */
  public static final int CANCEL = 3;

  /**
   * When user want to cancel current task and go to {@link StartTaskActivity}
   */
  public static final int CANCEL_AND_START_NEW = 4;

  /**
   * When user want to cancel current task continue other
   */
  private static final int CANCEL_AND_CONTINUE_OTHER = 5;

  @BindView(R.id.text_no)
  TextView textNo;

  @BindView(R.id.card_no)
  CardView cardNo;

  @BindView(R.id.card_yes)
  CardView cardYes;

  // Context for inflating dialog layout
  private final Context context;

  // Listener for cancelling session
  private final CancelSessionListener cancelSessionListener;

  // Actually, dialog.
  // (I don't know why I wrote this the most obvious code in the world)
  private AlertDialog alertDialog;

  /**
   * Current project to color cards
   */
  private final Project currentProject;

  /**
   * Task model if user wants to continue another task
   *
   * @see #CANCEL_AND_CONTINUE_OTHER
   */
  private Task taskToContinue;

  /**
   * Request flag for cancelSessionListener
   *
   * @see #CANCEL_AND_START_NEW
   * @see #CANCEL
   */
  private final int requestActionType;

  /**
   * Standard constructor, if user just want to cancel current task
   */
  public CancelSessionDialog(Context context,
      CancelSessionListener cancelSessionListener,
      int requestActionType, Project currentProject) {
    this.context = context;
    this.cancelSessionListener = cancelSessionListener;
    this.requestActionType = requestActionType;
    this.currentProject = currentProject;
  }

  /**
   * If user want to cancel current task and continue other
   */
  public CancelSessionDialog(Context context,
      CancelSessionListener cancelSessionListener,
      Task taskToContinue, Project currentProject) {
    this.context = context;
    this.cancelSessionListener = cancelSessionListener;
    this.taskToContinue = taskToContinue;
    this.currentProject = currentProject;
    this.requestActionType = CANCEL_AND_CONTINUE_OTHER;
  }

  public void show() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View layout = LayoutInflater.from(context)
        .inflate(R.layout.dialog_cancel_session, null);
    builder.setView(layout);
    alertDialog = builder.create();
    assert alertDialog.getWindow() != null;
    alertDialog.getWindow().setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT));
    ButterKnife.bind(this, layout);
    textNo.setTextColor(currentProject.getColor());
    cardNo.setCardBackgroundColor(currentProject.getColor());
    cardYes.setCardBackgroundColor(currentProject.getColor());
    alertDialog.show();
  }

  @OnClick(R.id.card_inner_no)
  void onNoClick() {
    alertDialog.dismiss();
  }

  @OnClick(R.id.card_yes)
  void onYesClick() {
    if (requestActionType == CANCEL_AND_CONTINUE_OTHER) {
      cancelSessionListener.onCancelSessionSelected(taskToContinue);
    } else {
      cancelSessionListener.onCancelSessionSelected(requestActionType);
    }
    alertDialog.dismiss();
  }

  /**
   * Callback to previous activity when user want to cancel
   */
  public interface CancelSessionListener {

    void onCancelSessionSelected(int actionType);

    void onCancelSessionSelected(Task task);
  }
}
