package com.arsvechkarev.frest.starttask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.additional.Codes.Key;
import com.arsvechkarev.frest.models.main.Project;

/**
 * Alert to define if user wants to start new session or add record
 *
 * @author Arseniy Svechkarev
 * @see StartTaskActivity
 */
class SessionOrRecordDialog {

  /**
   * If user wants to start session, flag for {@link SelectingActionTypeListener}
   */
  static final int ACTION_START_SESSION = 8;

  /**
   * If user wants add record session, flag for {@link SelectingActionTypeListener}
   */
  static final int ACTION_ADD_RECORD = 9;

  @BindView(R.id.textProjectName)
  TextView textProjectName;

  @BindView(R.id.card_start_session)
  CardView cardStartSession;

  // Uses to set stroke color
  @BindView(R.id.card_stroke_add_record)
  CardView cardAddRecordStroke;

  @BindView(R.id.text_add_record)
  TextView textAddRecord;

  private final Project project;
  private final SelectingActionTypeListener listener;

  private AlertDialog mAlertDialog;
  private final Context mContext;

  SessionOrRecordDialog(Context mContext, Project project,
      SelectingActionTypeListener listener) {
    this.mContext = mContext;
    this.project = project;
    this.listener = listener;
  }

  void show() {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    View view = LayoutInflater.from(mContext)
        .inflate(R.layout.dialog_session_or_record, null);
    builder.setView(view);
    mAlertDialog = builder.create();
    assert mAlertDialog.getWindow() != null;
    mAlertDialog.getWindow().setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT));
    ButterKnife.bind(this, view);
    prepareLayout();
    mAlertDialog.show();
  }

  private void prepareLayout() {
    textProjectName.setText(project.getName());
    textProjectName.setTextColor(project.getColor());
    cardStartSession.setCardBackgroundColor(project.getColor());
    cardAddRecordStroke.setCardBackgroundColor(project.getColor());
    textAddRecord.setTextColor(project.getColor());
  }

  /**
   * When user chooses to start session
   */
  @OnClick(R.id.card_start_session)
  void onStartSessionClick() {
    Intent startSessionData = new Intent();
    startSessionData.putExtra(Key.PROJECT, (Parcelable) project);
    listener.onActionSelected(ACTION_START_SESSION, startSessionData);
    mAlertDialog.dismiss();
  }

  /**
   * When user chooses to start record
   */
  @OnClick(R.id.card_add_record)
  void onAddRecordClick() {
    Intent intent = new Intent();
    intent.putExtra(Key.PROJECT, (Parcelable) project);
    listener.onActionSelected(ACTION_ADD_RECORD, intent);
    mAlertDialog.dismiss();
  }

  /**
   * Callback to define what type of action user selected
   */
  public interface SelectingActionTypeListener {

    void onActionSelected(int actionType, Intent data);
  }
}
