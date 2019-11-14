package com.arsvechkarev.frest.additional.myviews

import android.content.Context

class CancelableLayoutManager(
  context: Context?
) : androidx.recyclerview.widget.LinearLayoutManager(context) {
  
  var isScrollEnabled: Boolean = true
  
  override fun canScrollVertically(): Boolean {
    return isScrollEnabled and super.canScrollVertically()
  }
}