package com.example.tasks

import android.app.Application
import com.example.tasks.data.AppContainer

class TasksApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
