package com.arsvechkarev.frest.reports.all

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.INVISIBLE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import com.arsvechkarev.frest.models.reports.ReportsTriple
import kotlinx.android.synthetic.main.item_project_reports.view.textProjectDuration
import kotlinx.android.synthetic.main.item_project_reports.view.textProjectName
import kotlinx.android.synthetic.main.item_project_reports.view.textProjectPercent
import kotlinx.android.synthetic.main.partial_popup_dates.view.cardDateRanges
import kotlinx.android.synthetic.main.partial_popup_dates.view.textDateRanges
import kotlinx.android.synthetic.main.partial_reports_all_header.view.chartAllActivities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Adapter for recycler in [AllReportsFragment]
 *
 * @author Arseniy Svechkarev
 */
class ProjectsAllReportsAdapter(
  private val context: Context,
  private val adapterHeaderCallbacks: ReportsAdapterHeaderCallbacks
) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {
  
  var data: List<ReportsTriple>? = null
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (viewType) {
      TYPE_HEADER -> {
        HeaderViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.partial_reports_all_header, parent, false))
      }
      TYPE_ITEM -> {
        ItemViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_project_reports, parent, false))
      }
      else -> throw IllegalArgumentException("Invalid view type")
    }
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    var copyPos = position
    copyPos-- // because of header
    if (holder is ItemViewHolder) {
      holder.apply {
        textProjectName.text = data!![copyPos].project.name
        textProjectName.setTextColor(data!![copyPos].project.color)
        textProjectPercent.text = String.format("%s%%", data!![copyPos].formattedPercent())
        textProjectDuration.text = data!![copyPos].duration.toStandardTime()
      }
    }
  }
  
  override fun getItemCount(): Int {
    return if (data != null) data!!.size + 1 else 1
  }
  
  override fun getItemViewType(position: Int): Int {
    return if (position == 0) TYPE_HEADER else TYPE_ITEM
  }
  
  /**
   * View holder with header
   */
  inner class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {
    
    init {
      setGraphData()
      itemView.cardDateRanges.setOnClickListener { onCardClick() }
    }
    
    private fun setGraphData() {
      val pieEntries = adapterHeaderCallbacks.graphEntries
      val colors = adapterHeaderCallbacks.graphColors
      itemView.chartAllActivities
          .setEntries(pieEntries)
          .setColors(colors)
          .update()
    }
    
    private fun onCardClick() {
      val popup = PopupMenu(context, itemView.textDateRanges)
      popup.inflate(R.menu.date_ranges)
      popup.setOnMenuItemClickListener { item ->
        itemView.apply {
          textDateRanges.text = item.title
          chartAllActivities.visibility = INVISIBLE
          cardDateRanges.isEnabled = false
        }
        GlobalScope.launch(Dispatchers.Main) {
          adapterHeaderCallbacks.onDataRangeChanged(item.itemId)
          itemView.apply {
            chartAllActivities.visibility = VISIBLE
            cardDateRanges.isEnabled = true
          }
          setGraphData()
        }
        true
      }
      popup.show()
    }
  }
  
  /**
   * View holder with item
   */
  inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
    val textProjectName: TextView = itemView.textProjectName
    val textProjectPercent: TextView = itemView.textProjectPercent
    val textProjectDuration: TextView = itemView.textProjectDuration
  }
  
  companion object {
    private const val TYPE_HEADER = 0
    private const val TYPE_ITEM = 1
  }
}
