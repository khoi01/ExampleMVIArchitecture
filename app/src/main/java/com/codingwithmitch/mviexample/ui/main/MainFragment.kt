package com.codingwithmitch.mviexample.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.mviexample.R
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent

class MainFragment: Fragment(){

    lateinit var  viewModel:MainViewModel

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //enable menu
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw  Exception("Invalid activity")

        subscribeObservers()
    }

    fun subscribeObservers(){

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("Debug Datastate: ${dataState}")

            //Handle Data<T>
            dataState.data?.let { mainViewState ->
                mainViewState.blogPosts?.let{
                    //set blogposts data
                    viewModel.setBlogListData(it)

                }
                mainViewState.user?.let {
                    //set user data
                    viewModel.setUser(it)
                }
            }

            //Handle Error
            dataState.message?.let {

            }
            //Handle Loading
            dataState.loading?.let {

            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let{
                print("Debug: Setting blog posts to recyclerview: ${it} ")
            }

            viewState.user?.let{
                print("Debug: Setting user data: ${it}")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_getuser -> triggerGetUserEent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent())
    }

    private fun triggerGetUserEent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }
}