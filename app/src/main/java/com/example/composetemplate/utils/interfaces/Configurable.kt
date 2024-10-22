package com.example.composetemplate.utils.interfaces

/**
 * Interface representing a configurable entity.
 *
 * Any class that implements this interface must define the logic for configuration,
 * including initialization, fetching configuration data, updating and loading default values.
 */
interface Configurable {

    /**
     * Initializes the configuration settings.
     * This method should contain the logic to set up the initial configuration state.
     */
    fun initConfiguration()

    /**
     * Fetches the current configuration data.
     * This method should retrieve the necessary configuration values,
     * typically from a data source such as a file, database, or remote server.
     */
    fun fetch()

    /**
     * Updates the configuration settings.
     * This method should contain logic to modify and save new configuration values.
     */
    fun update()

    /**
     * Loads the default configuration values.
     * This method should reset the configuration to its default values when needed.
     */
    fun loadDefaults()
}