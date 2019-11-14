package com.arsvechkarev.frest.timeline;

import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.arsvechkarev.frest.R;
import com.arsvechkarev.frest.home.managers.TimelineActionsListener;
import com.arsvechkarev.frest.home.views.HomeFragment;
import com.arsvechkarev.frest.models.main.Task;
import com.arsvechkarev.frest.timeline.TimelineAdapter.BaseViewHolder;
import java.util.ArrayList;

/**
 * Adapter for timeline list
 *
 * @author Arseniy Svechkarev
 * @see TimelineManager
 */
public class TimelineAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  /**
   * Type flag for date of task
   *
   * @see DateViewHolder
   */
  private static final int TYPE_DATE = 2;

  /**
   * Type flag for task
   *
   * @see TaskViewHolder
   */
  private static final int TYPE_TASK = 4;

  // Context for inflating item layout
  private final Context context;

  // Context for inflating item layout
  private ArrayList<Object> timelineList;

  /**
   * Listener for sending callbacks to {@link HomeFragment}
   */
  private final TimelineActionsListener actionsListener;

  public TimelineAdapter(Context context, TimelineActionsListener actionsListener,
      ArrayList<Object> timelineList) {
    this.context = context;
    this.actionsListener = actionsListener;
    this.timelineList = timelineList;
  }

  public void setData(ArrayList<Object> mTimelineList) {
    this.timelineList = mTimelineList;
  }

  public ArrayList<Object> getData() {
    return timelineList;
  }

  @NonNull
  @Override
  public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    if (viewType == TYPE_TASK) {
      View view = LayoutInflater.from(context)
          .inflate(R.layout.item_task, viewGroup, false);
      return new TaskViewHolder(view);
    } else if (viewType == TYPE_DATE) {
      View view = LayoutInflater.from(context)
          .inflate(R.layout.item_date_header, viewGroup, false);
      return new DateViewHolder(view);
    }
    throw new IllegalArgumentException("Invalid viewType" + viewType);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    Object element = timelineList.get(position);
    holder.bind(element);
  }

  @Override
  public int getItemViewType(int position) {
    if (timelineList.get(position) instanceof Task) {
      return TYPE_TASK;
    }
    return TYPE_DATE;
  }

  @Override
  public int getItemCount() {
    return timelineList.size();
  }

  /**
   * Base abstract class for binding view holder
   *
   * @param <T> Type of holder that needed to bind
   */
  abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    BaseViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    abstract void bind(T type);
  }

  /**
   * View holder class for binding task item
   */
  class TaskViewHolder extends BaseViewHolder<Task> {

    @BindView(R.id.card_task)
    CardView cardMain;

    @BindView(R.id.card_continue)
    CardView cardContinue;

    @BindView(R.id.textProjectName)
    TextView textProjectName;

    @BindView(R.id.text_duration)
    TextView textDuration;

    @BindView(R.id.image_continue)
    ImageView imgContinue;

    TaskViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    @Override
    public void bind(Task task) {
      imgContinue.setColorFilter(task.getColor());
      textProjectName.setText(task.getName());
      textProjectName.setTextColor(task.getColor());
      textDuration.setTextColor(task.getColor());
      textDuration.setText(task.getFormattedDuration());
    }

    @OnClick(R.id.card_task)
    void onCardClick() {
      Task task = (Task) timelineList.get(getAdapterPosition());
      actionsListener.onTaskClick(task);
    }

    @OnLongClick(R.id.card_task)
    boolean onCardLongClick(View view) {
      view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
      Task task = (Task) timelineList.get(getAdapterPosition());
      actionsListener.onTaskLongClick(task);
      return true;
    }

    @OnClick(R.id.card_continue)
    void onContinueTaskClick() {
      Task task = (Task) timelineList.get(getAdapterPosition());
      actionsListener.onContinueTaskClick(task);
    }
  }

  /**
   * View holder class for binding date item
   */
  class DateViewHolder extends BaseViewHolder<String> {

    @BindView(R.id.text_date_header)
    TextView textDate;

    DateViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    @Override
    public void bind(String date) {
      textDate.setText(date);
    }
  }
}
