package com.codingwithmitch.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingwithmitch.mviexample.R
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent
import com.codingwithmitch.mviexample.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_blog_list_item.*
import java.lang.ClassCastException

class MainFragment: Fragment(),BlogListAdapter.Interaction{

    lateinit var  viewModel:MainViewModel
    lateinit var dataStateHandler:DataStateListener
    lateinit var  blogListAdapter: BlogListAdapter
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
        initRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            dataStateHandler = context as DataStateListener
        }catch (e:ClassCastException){
            println("Debug: $context must implement DataStateListener")
        }
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)

            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration((topSpacingItemDecoration))

            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    fun subscribeObservers(){

        //(5)(10)
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("Debug Datastate: ${dataState}")

            //handle loading and message
            dataStateHandler.onDataStageChange(dataState)

            //Handle Data<T>
            dataState.data?.let { event ->

                //has the content been look before?
                event.getContentIfNotHandled()?.let {
                    it.blogPosts?.let{ list ->
                        //set blogposts data
                        viewModel.setBlogListData(list)

                    }
                    it.user?.let {
                        //set user data
                        viewModel.setUser(it)
                    }
                }
            }
        })

        //(11)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let{
                print("Debug: Setting blog posts to recyclerview: ${it} ")
                //add item to recyclerview
                blogListAdapter.submitList(it)
            }

            viewState.user?.let{
                print("Debug: Setting user data: ${it}")
                setUserProperties(it)
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

    private fun setUserProperties(user:User){
        email.text = user.email
        username.text = user.username

        view?.let{
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent())
    }

    private fun triggerGetUserEent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: CLICKED $position")
        println("DEBUG: CLICKED $item")
    }
}