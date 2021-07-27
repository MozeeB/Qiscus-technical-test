package com.cikup.qiscuschat.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.utils.EXTRAS
import com.cikup.qiscuschat.utils.navigation.navigationToMain
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContruct.View {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        startBTN.setOnClickListener {
            val userId = userIdEDT.text.toString()
            val password = passwordEDT.text.toString()
            val displayName = displayNameEDT.text.toString()

            if (userId.isNullOrEmpty()){
                Toast.makeText(this, "user id cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isNullOrEmpty()){
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (displayName.isNullOrEmpty()){
                Toast.makeText(this, "display name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            presenter.doStart(userId, password, displayName)
        }
    }

    override fun onSuccess(message: String) {
        Hawk.put(EXTRAS.logged, EXTRAS.logged)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        navigationToMain(this)
    }

    override fun onFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressBarHolderCL.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBarHolderCL.visibility = View.GONE
    }

    override fun onStart() {
        if (Hawk.get(EXTRAS.logged, "").isNotEmpty()){
            navigationToMain(this)
        }
        super.onStart()
    }
}