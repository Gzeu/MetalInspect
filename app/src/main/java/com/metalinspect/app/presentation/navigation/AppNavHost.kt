package com.metalinspect.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.metalinspect.app.presentation.ui.InspectionEditorScreen
import com.metalinspect.app.presentation.ui.InspectionListScreen

sealed class Routes(val route: String) {
    data object List : Routes("list")
    data object Editor : Routes("editor")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.List.route) {
        composable(Routes.List.route) {
            InspectionListScreen(
                onAddInspection = { navController.navigate(Routes.Editor.route) },
                onOpenInspection = { /* TODO: detail */ }
            )
        }
        composable(Routes.Editor.route) {
            InspectionEditorScreen(
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
