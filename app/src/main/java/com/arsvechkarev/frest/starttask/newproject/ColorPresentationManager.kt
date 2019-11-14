package com.arsvechkarev.frest.starttask.newproject

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.ProjectColors
import com.arsvechkarev.frest.additional.ProjectColors.BLUE
import com.arsvechkarev.frest.additional.ProjectColors.BLUE_GRAY
import com.arsvechkarev.frest.additional.ProjectColors.BROWN
import com.arsvechkarev.frest.additional.ProjectColors.CYAN
import com.arsvechkarev.frest.additional.ProjectColors.GREEN
import com.arsvechkarev.frest.additional.ProjectColors.ORANGE
import com.arsvechkarev.frest.additional.ProjectColors.PINK
import com.arsvechkarev.frest.additional.ProjectColors.PURPLE
import com.arsvechkarev.frest.additional.ProjectColors.TEAL

/**
 * This class is a colors layout manager for [NewProjectActivity]. It provides a
 * presentation and functionality of color image views. Image views provides possible
 * variants of project colors that user can choose.
 *
 * @author Arseniy Svechkarev
 */
class ColorPresentationManager(
  private val newProjectActivity: NewProjectActivity,
  private val colorChangeListener: ProjectColorChangeListener = newProjectActivity
) {
  
  /*
   * Image views with colors
   */
  private lateinit var imageRed: ImageView
  private lateinit var imageOrange: ImageView
  private lateinit var imageTeal: ImageView
  private lateinit var imageBlueGray: ImageView
  private lateinit var imageCyan: ImageView
  private lateinit var imageBrown: ImageView
  private lateinit var imageBlue: ImageView
  private lateinit var imagePurple: ImageView
  private lateinit var imagePink: ImageView
  private lateinit var imageGreen: ImageView
  
  /*
   * Checkmarks to show what color user selected
   */
  private lateinit var checkmarkRed: ImageView
  private lateinit var checkmarkOrange: ImageView
  private lateinit var checkmarkTeal: ImageView
  private lateinit var checkmarkBlueGray: ImageView
  private lateinit var checkmarkCyan: ImageView
  private lateinit var checkmarkBrown: ImageView
  private lateinit var checkmarkBlue: ImageView
  private lateinit var checkmarkPurple: ImageView
  private lateinit var checkmarkPink: ImageView
  private lateinit var checkmarkGreen: ImageView
  
  
  fun initColorViews() {
    bindImageColors()
    bindCheckmarks()
    setImageColors()
    setImageListeners()
    setImageTags()
    invisibleAllCheckmarks()
    // Setting red, cause this is default value
    checkmarkRed.visibility = VISIBLE
  }
  
  // Finding circle images by ids
  private fun bindImageColors() {
    imageRed = newProjectActivity.findViewById(R.id.imageCircleColor1)
    imageOrange = newProjectActivity.findViewById(R.id.imageCircleColor2)
    imageTeal = newProjectActivity.findViewById(R.id.imageCircleColor3)
    imageBlueGray = newProjectActivity.findViewById(R.id.imageCircleColor4)
    imageCyan = newProjectActivity.findViewById(R.id.imageCircleColor5)
    imageBrown = newProjectActivity.findViewById(R.id.imageCircleColor6)
    imageBlue = newProjectActivity.findViewById(R.id.imageCircleColor7)
    imagePurple = newProjectActivity.findViewById(R.id.imageCircleColor8)
    imagePink = newProjectActivity.findViewById(R.id.imageCircleColor9)
    imageGreen = newProjectActivity.findViewById(R.id.imageCircleColor10)
  }
  
  // Finding checkmark images by ids
  private fun bindCheckmarks() {
    checkmarkRed = newProjectActivity.findViewById(R.id.imageCheckmark1)
    checkmarkOrange = newProjectActivity.findViewById(R.id.imageCheckmark2)
    checkmarkTeal = newProjectActivity.findViewById(R.id.imageCheckmark3)
    checkmarkBlueGray = newProjectActivity.findViewById(R.id.imageCheckmark4)
    checkmarkCyan = newProjectActivity.findViewById(R.id.imageCheckmark5)
    checkmarkBrown = newProjectActivity.findViewById(R.id.imageCheckmark6)
    checkmarkBlue = newProjectActivity.findViewById(R.id.imageCheckmark7)
    checkmarkPurple = newProjectActivity.findViewById(R.id.imageCheckmark8)
    checkmarkPink = newProjectActivity.findViewById(R.id.imageCheckmark9)
    checkmarkGreen = newProjectActivity.findViewById(R.id.imageCheckmark10)
  }
  
  // Setting colors to image views
  private fun setImageColors() {
    imageRed.setColorFilter(ProjectColors.RED.color)
    imageOrange.setColorFilter(ORANGE.color)
    imageTeal.setColorFilter(TEAL.color)
    imageBlueGray.setColorFilter(BLUE_GRAY.color)
    imageCyan.setColorFilter(CYAN.color)
    imageBrown.setColorFilter(BROWN.color)
    imageBlue.setColorFilter(BLUE.color)
    imagePurple.setColorFilter(PURPLE.color)
    imagePink.setColorFilter(PINK.color)
    imageGreen.setColorFilter(GREEN.color)
  }
  
  // Setting images tags by their colors to make
  // "onClick" method more easier
  private fun setImageTags() {
    imageRed.tag = ProjectColors.RED.color
    imageOrange.tag = ORANGE.color
    imageTeal.tag = TEAL.color
    imageBlueGray.tag = BLUE_GRAY.color
    imageCyan.tag = CYAN.color
    imageBrown.tag = BROWN.color
    imageBlue.tag = BLUE.color
    imagePurple.tag = PURPLE.color
    imagePink.tag = PINK.color
    imageGreen.tag = GREEN.color
  }
  
  private fun setImageListeners() {
    imageRed.setOnClickListener { onImageClick(it) }
    imageOrange.setOnClickListener { onImageClick(it) }
    imageTeal.setOnClickListener { onImageClick(it) }
    imageBlueGray.setOnClickListener { onImageClick(it) }
    imageCyan.setOnClickListener { onImageClick(it) }
    imageBrown.setOnClickListener { onImageClick(it) }
    imageBlue.setOnClickListener { onImageClick(it) }
    imagePurple.setOnClickListener { onImageClick(it) }
    imagePink.setOnClickListener { onImageClick(it) }
    imageGreen.setOnClickListener { onImageClick(it) }
  }
  
  private fun invisibleAllCheckmarks() {
    checkmarkRed.visibility = INVISIBLE
    checkmarkOrange.visibility = INVISIBLE
    checkmarkTeal.visibility = INVISIBLE
    checkmarkBlueGray.visibility = INVISIBLE
    checkmarkCyan.visibility = INVISIBLE
    checkmarkBrown.visibility = INVISIBLE
    checkmarkBlue.visibility = INVISIBLE
    checkmarkPurple.visibility = INVISIBLE
    checkmarkPink.visibility = INVISIBLE
    checkmarkGreen.visibility = INVISIBLE
  }
  
  fun onImageClick(image: View) {
    invisibleAllCheckmarks()
    val projectColor = image.tag as Int
    colorChangeListener.onProjectColorChanged(projectColor)
    when (projectColor) {
      ProjectColors.RED.color -> checkmarkRed.visibility = VISIBLE
      ORANGE.color -> checkmarkOrange.visibility = VISIBLE
      TEAL.color -> checkmarkTeal.visibility = VISIBLE
      BLUE_GRAY.color -> checkmarkBlueGray.visibility = VISIBLE
      BLUE.color -> checkmarkBlue.visibility = VISIBLE
      BROWN.color -> checkmarkBrown.visibility = VISIBLE
      CYAN.color -> checkmarkCyan.visibility = VISIBLE
      PURPLE.color -> checkmarkPurple.visibility = VISIBLE
      PINK.color -> checkmarkPink.visibility = VISIBLE
      GREEN.color -> checkmarkGreen.visibility = VISIBLE
    }
  }
  
  /**
   * Interface to know what color image view user clicked
   */
  interface ProjectColorChangeListener {
    
    /**
     * Send color result to [NewProjectActivity]
     *
     * @param color Color which user clicked
     */
    fun onProjectColorChanged(color: Int)
  }
}
