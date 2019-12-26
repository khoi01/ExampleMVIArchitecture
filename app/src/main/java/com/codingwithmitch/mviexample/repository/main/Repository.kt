package com.codingwithmitch.mviexample.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.codingwithmitch.mviexample.api.MyRetrofitBuilder
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.ui.main.state.MainViewState
import com.codingwithmitch.mviexample.util.*


object Repository{

    fun getBlogPosts(): LiveData<DataState<MainViewState>>{
     return object :NetworkBoundResource<List<BlogPost>,MainViewState>(){
         override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
            result.value = DataState.data(
                data = MainViewState(
                    blogPosts =  response.body
                )
            )
         }

         override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
            return MyRetrofitBuilder.apiService.getBlogPosts()
         }

     }.asLiveData()
    }

    fun getUser(userId:String): LiveData<DataState<MainViewState>>{
        return object :NetworkBoundResource<User,MainViewState>(){
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user =  response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()

    }



}