package com.arsvechkarev.frest.models.main

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.arsvechkarev.frest.projects.ProjectsManager
import java.io.Serializable
import java.util.Objects

/**
 * This class presents a type for "Project" object. Project is one of base entities, which
 * is used to count time user spent on it
 *
 * @author Arseniy Svechkarev
 * @see Task
 * @see ProjectsManager
 */
@Keep
open class Project : Serializable, Parcelable {
  
  /**
   * Name of project. Each project should have original name, because name is also an
   * identifier
   */
  val name: String
  
  /**
   * Color of project. Unlike [.name], there can be a lot of projects with the same
   * color
   */
  val color: Int
  
  constructor(name: String, color: Int) {
    this.name = name
    this.color = color
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other == null || javaClass != other.javaClass) {
      return false
    }
    val that = other as Project?
    return color == that!!.color && name == that.name
  }
  
  override fun hashCode(): Int {
    return Objects.hash(name, color)
  }
  
  override fun toString(): String {
    return "Project{" +
        "name='" + name + '\''.toString() +
        ", color=" + color +
        '}'.toString()
  }
  
  protected constructor(parcel: Parcel) {
    name = parcel.readString()!!
    color = parcel.readInt()
  }
  
  override fun describeContents(): Int {
    return 0
  }
  
  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(name)
    dest.writeInt(color)
  }
  
  companion object {
    
    private const val serialVersionUID = 1L
    
    @JvmField
    val CREATOR: Parcelable.Creator<Project> = object : Parcelable.Creator<Project> {
      override fun createFromParcel(`in`: Parcel): Project {
        return Project(`in`)
      }
      
      override fun newArray(size: Int): Array<Project?> {
        return arrayOfNulls(size)
      }
    }
  }
}
