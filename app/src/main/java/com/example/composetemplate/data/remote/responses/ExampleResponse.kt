package com.example.composetemplate.data.remote.responses

import com.example.composetemplate.Source
import kotlinx.serialization.Serializable

/** Here is an example of a base server response.
 * We want all server responses to be data classes that implement the `BaseResponse` interface. */

@Serializable
data class ExampleResponse(
    val sources: List<Source>,
    val status: String
)
