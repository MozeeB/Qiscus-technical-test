package com.cikup.qiscuschat

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.qiscus.sdk.chat.core.QiscusCore
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.one.EmojiOneProvider


class MainApp : Application(){
    override fun onCreate() {
        super.onCreate()

        QiscusCore.setup(this, BuildConfig.QISCUS_SDK_APP_ID)
        EmojiManager.install(EmojiOneProvider())
        Hawk.init(this).build();

    }
}