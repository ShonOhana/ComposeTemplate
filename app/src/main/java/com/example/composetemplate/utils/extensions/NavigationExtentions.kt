package com.example.composetemplate.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateSingleTopTo(route: String) {
    navigate(route = route) {
        popUpTo(
            graph.findStartDestination().id
        )
        launchSingleTop = true
        restoreState = true
    }
}
