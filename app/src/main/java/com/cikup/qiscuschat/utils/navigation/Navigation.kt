package com.cikup.qiscuschat.utils.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cikup.qiscuschat.R.anim.*
import com.cikup.qiscuschat.ui.chat_room.ChatRoomActivity
import com.cikup.qiscuschat.ui.contact.ContactActivity
import com.cikup.qiscuschat.ui.list_room.MainActivity
import com.cikup.qiscuschat.ui.login.LoginActivity

fun navigationToMain(context: Context){
    if (context != null && context is Activity) {
        val activity = context
        val flags = context.flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.start<MainActivity>(flags, right_in, left_out)
    }
}

fun navigationToContacts(context: Context){
    if (context != null && context is Activity) {
        val activity = context
        val flags = context.flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.start<ContactActivity>(flags, right_in, left_out)
    }
}

fun navigationToChatRoom(context: Context, bundle: Bundle){
    if (context != null && context is Activity) {
        val activity = context
        val flags = context.flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.start<ChatRoomActivity>(bundle, flags, right_in, left_out)
    }
}

fun backToLogin(context: Context){
    if (context != null && context is Activity) {
        val activity = context
        val flags = context.flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.start<LoginActivity>(flags, left_in, right_out)
    }
}
fun backToMain(context: Context){
    if (context != null && context is Activity) {
        val activity = context
        val flags = context.flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.start<MainActivity>(flags, left_in, right_out)
    }
}