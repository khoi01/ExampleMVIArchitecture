package com.codingwithmitch.mviexample.util

import androidx.lifecycle.LiveData

/**
 * A livedata class that has 'null' value
 */
class AbsentLiveData<T:Any?> private constructor(): LiveData<T>(){
    init{
        //use post insted of set since this can be created on any thread
        postValue(null)
    }
    companion object{
        fun <T> create(): LiveData<T>{
            return AbsentLiveData()
        }
    }
}