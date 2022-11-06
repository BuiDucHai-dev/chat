package com.example.stalk.common.firebase

import com.example.stalk.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MyFirebase {
    val db get() = Firebase.firestore

    val colRef = Firebase.firestore.collection("conversation")

    val auth: FirebaseAuth = Firebase.auth

    val storageRef = Firebase.storage.reference

    var currentUser: User? = null

    fun setCurrentUserFirst(user: User?){
        this.currentUser = user
    }
}