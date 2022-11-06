package com.example.stalk.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User
import com.example.stalk.databinding.ActivityHomeBinding
import com.example.stalk.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("hai.bd", "HomeAc - This is user: " + MyFirebase().auth.currentUser?.phoneNumber)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_new_group, R.id.nav_invite_friends, R.id.nav_contacts, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser?.uid.toString())
            .get()
            .addOnFailureListener {

            }
            .addOnCompleteListener {
                val user = it.result.toObject(User::class.java)
                if (user == null) {
                    Log.d("hai.bd", "No User")
                    MyFirebase().auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                val sharedPreferences: SharedPreferences = getSharedPreferences("NAME", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString("KEY", Gson().toJson(user))
                }.apply()
                editor.apply()

                navView.getHeaderView(0).findViewById<TextView>(R.id.tv_user_name).text = getUser().fullName
                navView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone_number).text = getUser().phoneNum
                Glide.with(this)
                    .load(getUser().avatar)
                    .circleCrop()
                    .into(navView.getHeaderView(0).findViewById(R.id.imageView))
            }

    }

    fun getUser(): User {
        return Gson().fromJson(
            getSharedPreferences("NAME", MODE_PRIVATE).getString(
                "KEY",
                "nothing"
            ), User::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser?.uid.toString())
            .update("dtmLastSeen", Comm().getCurrentTime())
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}