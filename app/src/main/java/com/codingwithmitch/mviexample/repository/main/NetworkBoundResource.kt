package com.codingwithmitch.mviexample.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.codingwithmitch.mviexample.ui.main.state.MainViewState
import com.codingwithmitch.mviexample.util.*
import com.codingwithmitch.mviexample.util.Constants.Companion.TESTING_NETWORK_DELAY
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource <ResponseObject,ViewStateType>{
    protected  val result = MediatorLiveData<DataState<ViewStateType>>()
    init{
        result.value = DataState.loading(true)

        GlobalScope.launch(IO){
            delay(TESTING_NETWORK_DELAY)
            withContext(Main){
                val apiResponse = createCall()
                result.addSource(apiResponse){response ->
                    result.removeSource(apiResponse)
                    handleNetWorkCall(response)
                }
            }
        }
    }

    private fun handleNetWorkCall(response: GenericApiResponse<ResponseObject>) {
        when(response){
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                // Handle Error?
                println("DEBUG: NetworkBoundResource: ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }

            is ApiEmptyResponse -> {
                // Handle Empty? (error)
                println("DEBUG: NetworkBoundResource: HTTP 204. Retured NOTHING.")
                onReturnError("HTTP 204. Returning NOTHING.")

            }
        }
    }

    fun onReturnError(message:String){
        result.value = DataState.error(message)
    }

    abstract  fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)
    //return user object or blogs object
    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    //retturn  as LIVEDATA
    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}