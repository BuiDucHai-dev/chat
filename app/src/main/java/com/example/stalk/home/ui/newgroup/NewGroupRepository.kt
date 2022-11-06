package com.example.stalk.home.ui.newgroup;

import com.example.stalk.common.model.User


class NewGroupRepository(val dataSource: NewGroupDataSource) {
    fun getUserList(callback: NewGroupRepositoryCallback) {
        dataSource.getUserList(object : NewGroupDataSource.NewGroupDataSourceCallback{
            override fun onReceiverUserList(listUser: MutableList<User>) {
                callback.onReceiverUserList(listUser)
            }
        })
    }

    interface NewGroupRepositoryCallback{
        fun onReceiverUserList(listUser: MutableList<User>)
    }
}
