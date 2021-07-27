package com.cikup.qiscuschat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.utils.event.ContactEvent
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import kotlinx.android.synthetic.main.item_contact.view.*
import org.greenrobot.eventbus.EventBus


class ContactAdapter(var userModel: List<QiscusAccount>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    var bus = EventBus.getDefault()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = userModel[position]
        val view = holder.itemView

        view.nameContactTV.text = data.username
        view.setOnClickListener {
            bus.post(ContactEvent(data))
        }
    }

    override fun getItemCount(): Int = userModel.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}