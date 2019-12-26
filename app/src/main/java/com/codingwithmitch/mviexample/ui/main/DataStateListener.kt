package com.codingwithmitch.mviexample.ui.main

import com.codingwithmitch.mviexample.util.DataState

interface DataStateListener {
    fun onDataStageChange(dataState:DataState<*>?)
}