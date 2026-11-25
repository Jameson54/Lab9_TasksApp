package com.example.tasks

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.example.tasks.data.TaskDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDaoTests {

    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskDatabase

    private val task1 = Task(
        title = "Полить растения", description = "На кухне",
        date = "2026-11-25", hour = 9, minute = 0
    )
    private val task2 = Task(
        title = "Купить продукты", description = "Магазин",
        date = "2026-11-26", hour = 18, minute = 30
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        taskDao.insert(task1)
        val allItems = taskDao.getAllTasks().first()
        assertEquals(1, allItems.size)
        assertEquals(task1.title, allItems[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        taskDao.insert(task1)
        taskDao.insert(task2)
        assertEquals(2, taskDao.getAllTasks().first().size)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdate_updatesItemInDB() = runBlocking {
        taskDao.insert(task1)
        val loaded = taskDao.getAllTasks().first()[0]
        taskDao.update(loaded.copy(title = "Новое название"))
        assertEquals("Новое название", taskDao.getAllTasks().first()[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_deletesItemFromDB() = runBlocking {
        taskDao.insert(task1)
        val loaded = taskDao.getAllTasks().first()[0]
        taskDao.delete(loaded)
        assertEquals(0, taskDao.getAllTasks().first().size)
    }
}
