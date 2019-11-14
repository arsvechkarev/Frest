package com.arsvechkarev.frest.timeline;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

/**
 * Diff callback for updating timeline list
 */
public class TimelineDiffCallback extends DiffUtil.Callback {

  private final List<Object> oldList;

  private final List<Object> newList;

  public TimelineDiffCallback(List<Object> oldList, List<Object> newList) {
    this.oldList = oldList;
    this.newList = newList;
  }

  @Override
  public int getOldListSize() {
    return (oldList != null) ? oldList.size() : 0;
  }

  @Override
  public int getNewListSize() {
    return (newList != null) ? newList.size() : 0;
  }

  @Override
  public boolean areItemsTheSame(int oldItemPos, int newItemPos) {
    Object oldObject = oldList.get(oldItemPos);
    Object newObject = newList.get(newItemPos);
    return oldObject.equals(newObject);
  }

  @Override
  public boolean areContentsTheSame(int oldItemPos, int newItemPos) {
    Object oldObject = oldList.get(oldItemPos);
    Object newObject = newList.get(newItemPos);
    return oldObject.equals(newObject);
  }
}
