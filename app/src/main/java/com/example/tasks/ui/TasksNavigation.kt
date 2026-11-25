package com.example.tasks.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasks.ui.details.TaskDetailsScreen
import com.example.tasks.ui.entry.TaskEntryScreen
import com.example.tasks.ui.home.HomeScreen

@Composable
fun TasksNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(
                navigateToTaskEntry = { navController.navigate("task_entry/-1") },
                navigateToTaskDetails = { taskId -> navController.navigate("task_details/$taskId") }
            )
        }
        composable(
            route = "task_entry/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) {
            TaskEntryScreen(navigateBack = { navController.navigateUp() })
        }
        composable(
            route = "task_details/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) {
            TaskDetailsScreen(
                navigateToEdit = { taskId -> navController.navigate("task_entry/$taskId") },
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
