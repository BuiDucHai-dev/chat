package com.example.stalk.home.ui.invite_friend

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.stalk.invite_friend.InviteFriendActivity


class InviteFriendFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(requireActivity(), InviteFriendActivity::class.java))


//        val smsIntent = Intent(Intent.ACTION_VIEW)
//        smsIntent.type = "vnd.android-dir/mms-sms"
//        smsIntent.putExtra("address", "1234567890;9876543210;453678920")
//        smsIntent.putExtra("sms_body", "Body of Message")
//        startActivity(smsIntent)
    }
}