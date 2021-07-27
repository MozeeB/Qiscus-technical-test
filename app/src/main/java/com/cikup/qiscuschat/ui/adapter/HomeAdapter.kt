package com.cikup.qiscuschat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.utils.DateUtil
import com.cikup.qiscuschat.utils.OnItemClickListener
import com.cikup.qiscuschat.utils.SortedRecyclerViewAdapter
import com.cikup.qiscuschat.utils.event.ContactPosition
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.vanniktech.emoji.EmojiTextView
import org.greenrobot.eventbus.EventBus


class HomeAdapter(private val context: Context) : SortedRecyclerViewAdapter<QiscusChatRoom, HomeAdapter.VH>() {
    private var onItemClickListener: OnItemClickListener? = null
    val eventBus = EventBus.getDefault()


    override fun getItemClass(): Class<QiscusChatRoom> {
        return QiscusChatRoom::class.java
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false)
        return VH(view, onItemClickListener)  }


    override fun compare(item1: QiscusChatRoom, item2: QiscusChatRoom): Int {
        return item2.lastComment.time.compareTo(item1.lastComment.time)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        data.get(position)?.let { holder.bind(it) }

        holder.itemView.setOnClickListener {
            eventBus.post(ContactPosition(position))
        }
    }

    override fun addOrUpdate(items: List<QiscusChatRoom>) {
        for (chatRoom in items) {
            val index = findPosition(chatRoom)
            if (index == -1) {
                data.add(chatRoom)
            } else {
                data.updateItemAt(index, chatRoom)
            }
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    class VH internal constructor(itemView: View, private val onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {


        private val name: TextView = itemView.findViewById(R.id.name)
        private val lastMessage: EmojiTextView = itemView.findViewById(R.id.tv_last_message)
        private val tv_unread_count: TextView = itemView.findViewById(R.id.tv_unread_count)
        private val tv_time: TextView = itemView.findViewById(R.id.tv_time)
        private val layout_unread_count: FrameLayout = itemView.findViewById(R.id.layout_unread_count)


        fun bind(chatRoom: QiscusChatRoom) {
            name.text = chatRoom.name
            val lastComment = chatRoom.lastComment
            if (lastComment != null && lastComment.id > 0) {
                if (lastComment.sender != null) {
                    var lastMessageText: String? =
                        if (lastComment.isMyComment) "You: " else lastComment.sender.split(" ")
                            .toTypedArray()[0] + ": "
                    lastMessageText += if (chatRoom.lastComment.type == QiscusComment.Type.IMAGE) "\uD83D\uDCF7 send an image" else lastComment.message
                    lastMessage.text = lastMessageText
                } else {
                    var lastMessageText: String? = ""
                    lastMessageText += if (chatRoom.lastComment.type == QiscusComment.Type.IMAGE) "\uD83D\uDCF7 send an image" else lastComment.message
                    lastMessage.text = lastMessageText
                }
                tv_time.text = DateUtil.getLastMessageTimestamp(lastComment.time)
            } else {
                lastMessage.text = ""
                tv_time.text = ""
            }
            tv_unread_count.text = String.format("%d", chatRoom.unreadCount)
            if (chatRoom.unreadCount == 0) {
                layout_unread_count.visibility = View.GONE
            } else {
                layout_unread_count.visibility = View.VISIBLE
            }
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(adapterPosition)
        }
    }


}


