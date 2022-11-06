package com.example.stalk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.databinding.ActivityMainBinding
import com.example.stalk.home.HomeActivity
import com.example.stalk.login.LoginActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (!Comm().checkPermission(this)){
//            Comm().requestPermission(this)
//        }

//        var uri: Uri? = null
//        binding.imageView.setOnClickListener {
//            val kwikPicker = KwikPicker.Builder(this@MainActivity,
//                imageProvider = { imageView, uri ->
//                    Glide.with(this@MainActivity)//Any image provider here!
//                        .load(uri)
//                        .into(imageView)
//                },
//                onImageSelectedListener = {
//                    var s : String = it.toString()
//                    Glide.with(this@MainActivity)
//                    .load(s)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(binding.imageView)
//                    uri = it
//                    Log.d("hai.bd", it.toString())
//                },
//                peekHeight = 1200)
//                .create(this@MainActivity)
//            kwikPicker.show(supportFragmentManager)
//        }
//

//        Firebase.storage.reference.child("user/test1.jpg")
//            .downloadUrl
//            .addOnSuccessListener {
//                Glide.with(this@MainActivity)
//                    .load(it)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(binding.imageView)
//                Log.d("hai.bd", it.toString())
//
//            }
//            .addOnFailureListener {
//                Log.d("hai.bd", "get Image fail")
//            }
//
//        binding.button.setOnClickListener {
//            if (binding.imageView.drawable == null){
//                Log.d("hai.bd", "image null")
//            } else {
//                Log.d("hai.bd", binding.imageView.drawable.toString())
//            }
//            uri?.let { it1 ->
//                Firebase.storage.reference.child("user/test.jpg")
//                    .putFile(it1)
//                    .addOnSuccessListener {
//
//                        Log.d("hai.bd", "upload success")
//                    }
//                    .addOnFailureListener {
//                        Log.d("hai.bd", "upload fail")
//                    }
//                    .addOnCompleteListener {
//                        Glide.with(this@MainActivity)
//                            .load(it.result)
//                            .apply(RequestOptions.circleCropTransform())
//                            .into(binding.imageView2)
//                        Log.d("hai.bd", "com: " + it.result.uploadSessionUri)
//                    }
//            }
//        }


//        val storageReference = Firebase.storage.reference.child("user/IMG_1609854273190_1610075527451.jpg")
//
//        storageReference.downloadUrl.addOnSuccessListener {
//            Glide.with(this)
//                .load(it)
//                .apply(RequestOptions.circleCropTransform())
//                .into(binding.imageView)
//            Log.d("hai.bd", it.toString())
//        }


        //if (MyFirebase().auth.currentUser != null) MyFirebase().auth.signOut()
        if (MyFirebase().auth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}