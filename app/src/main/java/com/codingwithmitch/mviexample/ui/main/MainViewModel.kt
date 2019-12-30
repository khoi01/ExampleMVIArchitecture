package com.codingwithmitch.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.repository.main.Repository
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent
import com.codingwithmitch.mviexample.ui.main.state.MainViewState
import com.codingwithmitch.mviexample.util.AbsentLiveData
import com.codingwithmitch.mviexample.util.DataState

class MainViewModel :ViewModel(){
    private val _stateEvent:MutableLiveData<MainStateEvent> = MutableLiveData();
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData();

    val viewState: LiveData<MainViewState>
    get() = _viewState

    //(2)
    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent){stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }


    fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>>{
        when(stateEvent){
            //(3)
            is MainStateEvent.GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }

            is MainStateEvent.GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }

            is MainStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPost: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPost
        _viewState.value = update
        _viewState.value = update
    }

    //(6)(11)
    fun setUser(user: User){
        val update = getCurrentViewStateOrNew()
        update.user = user;
        _viewState.value = update

    }
    //8
    fun getCurrentViewStateOrNew(): MainViewState{
        val value = viewState.value?.let{
            it
        }?: MainViewState()
        return value
    }


    //set state event
    //(1)
    fun setStateEvent(event:MainStateEvent){
        _stateEvent.value = event
    }
}