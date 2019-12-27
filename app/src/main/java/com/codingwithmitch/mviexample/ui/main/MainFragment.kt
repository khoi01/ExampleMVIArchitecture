package com.codingwithmitch.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.mviexample.R
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent
import java.lang.ClassCastException

class MainFragment: Fragment(){

    lateinit var  viewModel:MainViewModel
    lateinit var dataStateHandler:DataStateListener
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            dataStateHandler = context as DataStateListener
        }catch (e:ClassCastException){
            println("Debug: $context must implement DataStateListener")
        }
    }

    fun subscribeObservers(){

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("Debug Datastate: ${dataState}")

            //handle loading and message
            dataStateHandler.onDataStageChange(dataState)

            //Handle Data<T>
            dataState.data?.let { event ->

                //has the content been look before?
                event.getContentIfNotHandled()?.let {
                    it.blogPosts?.let{
                        //set blogposts data
                        viewModel.setBlogListData(it)

                    }
                    it.user?.let {
                        //set user data
                        viewModel.setUser(it)
                    }
                }
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