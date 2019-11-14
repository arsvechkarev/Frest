package com.arsvechkarev.frest.additional

import com.arsvechkarev.frest.home.views.MainActivity
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.starttask.AddRecordActivity
import com.arsvechkarev.frest.starttask.StartTaskActivity

/**
 * Main constants used in app
 *
 * @author Arseniy Svechkarev
 */
object Codes {
  
  /** Flags to notify someone about something */
  object Flag {
    
    /** Flag to know that message from [MainActivity] */
    const val FROM_MAIN_ACTIVITY = "from_main_activity"
  }
  
  /** Keys constants to pass data within intents */
  object Key {
    
    /** To pass [Project] */
    const val PROJECT = "project"
    
    /** To pass [Task] */
    const val TASK = "task"
    
    /** To pass [Project] in [AddRecordActivity] */
    const val PROJECT_ADDING_RECORD = "project_adding_record"
  }
  
  /**
   * Request for `startActivityForResult()`
   */
  object Request {
    
    /** To know user want start session or add record (in [MainActivity]) */
    const val MAIN = 219
    
    /** For add record (in [StartTaskActivity]) */
    const val ADD_RECORD = 18
    
    /** For creating new project (in [StartTaskActivity]) */
    const val Create_PROJECT = 16
  }
  
  /**
   * Result codes for `onActivityResult`
   */
  object Result {
    
    /** If user starts a new session */
    const val START_SESSION = 229
    
    /** If user adds record */
    const val ADD_RECORD = 244
    
    /** Interim constant for recording task */
    const val ADD_RECORD_INTERIM = 19
    
    /** When user created project */
    const val PROJECT_CREATED = 20
  }
}


