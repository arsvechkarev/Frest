package com.arsvechkarev.frest.timeline

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.arsvechkarev.frest.additional.datetime.Date
import com.arsvechkarev.frest.additional.datetime.PrettyDate
import com.arsvechkarev.frest.home.views.HomeFragment
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.projects.ProjectsManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList

/**
 * Class for managing with timeline list
 *
 * @author Arseniy Svechkarev
 * @see HomeFragment
 */
class TimelineManager(private val context: Context) {
  
  /** File with tasks */
  private val tasksFile: File
  
  /** For setting pretty view for date */
  private val prettyDate: PrettyDate
  
  private var tasksList: ArrayList<Task>? = null
  
  /** Returns all saved tasks with replenished dates */
  val timelineList: ArrayList<Any>?
    get() = replenishListWithDates(tasksList)
  
  init {
    tasksFile = File(context.getDir(TASKS_DATA, MODE_PRIVATE), TASKS_LIST)
    prettyDate = PrettyDate()
    retrieveFile()
  }
  
  private fun retrieveFile() {
    tasksList = getTasksList()
  }
  
  /** Actually, saving task to tasks list */
  fun saveTask(task: Task) {
    if (tasksList == null) {
      tasksList = ArrayList()
    }
    if (wasTaskTrackedThisDate(task)) {
      // If the task was tracked this date, we should updateBy previous duration,
      // but not create new task
      val newTask = createUpdatedTask(tasksList!!, task)
      tasksList!!.add(newTask)
    } else {
      tasksList!!.add(task)
    }
    trimToMaxSize(tasksList!!)
    saveTaskList(tasksList)
  }
  
  /** Removing particular task from tasks list */
  fun removeTask(task: Task) {
    if (tasksList != null) {
      tasksList!!.remove(task)
      saveTaskList(tasksList)
    }
  }
  
  /**
   * If user already deleted a project, all tasks associated with this project should be
   * removed from tasks list
   */
  fun removeRedundantProjects() {
    if (tasksList != null) {
      val projectsManager = ProjectsManager(context)
      val iterator = tasksList!!.iterator()
      while (iterator.hasNext()) {
        val task = iterator.next()
        if (!projectsManager.isProjectAlreadyExists(task.name)) {
          iterator.remove()
        }
      }
    }
    saveTaskList(tasksList)
  }
  
  // Creating new task with updated duration
  private fun createUpdatedTask(tasksList: ArrayList<Task>, oldTask: Task): Task {
    val todayTask = findTaskTrackedThisDate(oldTask)
    tasksList.remove(todayTask)
    val totalTime = todayTask!!.duration + oldTask.duration
    return Task(oldTask.name, oldTask.color, totalTime, Date(oldTask.date))
  }
  
  /**
   * Shows if project, which associated with this task was already tracked with task's
   * date and task duration has to be updated instead of creating new task
   *
   * @see saveTask
   * @see findTaskTrackedThisDate
   */
  private fun wasTaskTrackedThisDate(task: Task): Boolean {
    val todayTask = findTaskTrackedThisDate(task)
    return todayTask != null
  }
  
  private fun findTaskTrackedThisDate(task: Task): Task? {
    if (tasksList == null) {
      return null
    }
    for (currentTask in tasksList!!) {
      if (areTaskInSameDay(currentTask, task)) {
        return currentTask
      }
    }
    return null
  }
  
  private fun saveTaskList(tasksList: ArrayList<Task>?) {
    ObjectOutputStream(FileOutputStream(tasksFile)).apply {
      writeObject(tasksList)
      flush()
      close()
    }
  }
  
  @Suppress("UNCHECKED_CAST")
  private fun getTasksList(): ArrayList<Task>? {
    return if (tasksFile.exists()) {
      val inputStream = ObjectInputStream(FileInputStream(tasksFile))
      val tasksList = inputStream.readObject() as? ArrayList<Task>
      inputStream.close()
      tasksList
    } else {
      null
    }
  }
  
  /**
   * Replenishing tasks list with dates of appropriate task
   *
   * @see fillTimelineList
   */
  private fun replenishListWithDates(tasksList: ArrayList<Task>?): ArrayList<Any>? {
    if (tasksList == null)
      return null
    tasksList.sort()
    return fillTimelineList(tasksList)
  }
  
  /** Inserting dates into tasks list */
  private fun fillTimelineList(tasksList: ArrayList<Task>): ArrayList<Any> {
    val timelineList = ArrayList<Any>()
    for (i in tasksList.indices) {
      timelineList.add(tasksList[i])
      val currentTask = tasksList[i]
      val currentDate = currentTask.date
      if (i < tasksList.size - 1) {
        val nextTask = tasksList[i + 1]
        if (!areTasksDatesEquals(currentTask, nextTask)) {
          timelineList.add(prettyDate.getPrettyDateView(currentDate))
        }
      } else {
        timelineList.add(prettyDate.getPrettyDateView(currentDate))
      }
    }
    return timelineList
  }
  
  // Comparing two task dates
  private fun areTasksDatesEquals(task1: Task, task2: Task): Boolean {
    return task1.date == task2.date
  }
  
  // Comparing tasks names and dates
  private fun areTaskInSameDay(task1: Task, task2: Task): Boolean {
    val areTasksDatesSame = areTasksDatesEquals(task1, task2)
    val areTasksNamesSame = task1.name == task2.name
    return areTasksDatesSame && areTasksNamesSame
  }
  
  /** Removing last task if tasks list size more than [MAX_SIZE] */
  private fun trimToMaxSize(tasksList: ArrayList<Task>) {
    if (tasksList.size > MAX_SIZE) {
      tasksList.removeAt(0)
    }
  }
  
  companion object {
    
    /** Max possible number of tasks in list */
    private const val MAX_SIZE = 300
    
    /** Name of file directory where tasks are saved */
    private const val TASKS_DATA = "tasks_file_data"
    
    /** Name of file with tasks list */
    private const val TASKS_LIST = "tasks_list"
  }
}
