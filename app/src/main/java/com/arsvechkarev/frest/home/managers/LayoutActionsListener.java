package com.arsvechkarev.frest.home.managers;

import com.arsvechkarev.frest.home.views.HomeFragment;
import com.arsvechkarev.frest.timeline.TimelineAdapter;
import java.util.ArrayList;

/**
 * Actions with {@link HomeFragment} layout
 */
public interface LayoutActionsListener {

  /**
   * If timeline recycler is not prepared
   */
  void prepareRecycler(TimelineAdapter adapter);

  /**
   * If timeline recycler already prepared and it can be updated
   */
  void updateRecycler(ArrayList<Object> currentList);

  /**
   * If timeline list is empty or null, and we should notify user about it
   */
  void showNoTasksLayout();
}
