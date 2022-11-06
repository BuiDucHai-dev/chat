package com.example.stalk.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder

public class Util {
    companion object{
        private var gson: Gson? = null

        public fun getGsonParse() : Gson{
            if (gson == null){
                var builder = GsonBuilder()
                gson = builder.create()
            }
            return gson!!
        }

        fun getDefaultAvatar(): String {
            return "https://firebasestorage.googleapis.com/v0/b/stalk-427ad.appspot.com/o/user%2FI7m54Nw8HlR8bJI1TJ0sBOLt7UH2?alt=media&token=6d39cae5-1408-4dd9-9ece-3ec110f78bc2"
        }
    }

}