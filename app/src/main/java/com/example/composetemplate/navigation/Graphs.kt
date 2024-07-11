package com.example.composetemplate.navigation

import kotlinx.serialization.Serializable

/**
 * Graph interface represents a navigation graph within the application.
 * A navigation graph is a collection of routes and navigation actions that define a complete navigation flow.
 */
interface Graph


/** EntryGraph represents the navigation graph for the entry flow of the application.
 * This includes routes like Splash, Authentication, and Advertisement.
 */
@Serializable
data object EntryGraph : Graph

/** MainGraph represents the navigation graph for the main flow of the application.
 * This includes routes like Home and Settings.
 */
@Serializable
data object MainGraph : Graph
