package com.example.composetemplate.navigation.destination

/**
 * Contract for information needed on every Login navigation destination
 */
interface LoginDestination {
    val route: String
}

/**
 * Rally app navigation destinations
 */
object Login : LoginDestination {
    override val route = "login"
}

object Register : LoginDestination {
    override val route = "register"
}