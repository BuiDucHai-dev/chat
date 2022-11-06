package com.example.stalk.home.ui.newgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewGroupModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewGroupViewModel::class.java)){
            return NewGroupViewModel(
                repository = NewGroupRepository(
                    dataSource = NewGroupDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}