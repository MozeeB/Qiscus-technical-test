package com.cikup.qiscuschat.ui.login

interface LoginContruct{
    interface View{
        fun onSuccess(message:String)
        fun onFailed(message: String)
        fun showLoading()
        fun hideLoading()

    }

    interface Presenter{
        fun doStart(userId:String, password:String, displayName:String)
    }
}