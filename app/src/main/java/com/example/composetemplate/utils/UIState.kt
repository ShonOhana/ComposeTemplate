package com.example.composetemplate.utils


    sealed class UIState<T>( val data: T? = null){
        class Error<T>(val message:String?) : UIState<T>()
        class Success<T>(data:T?) : UIState<T>(data)
        class Loading<T>:UIState<T>()
    }
