package com.example.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.databinding.AdapterContactsBinding
import com.example.whatsappclone.databinding.ContentChatBinding
import com.example.whatsappclone.model.Msgs

//TODO "I need to create a new layout for the reached user messages display on screen"

class MsgAdapter(
    msglist: List<Msgs>,
    context: Context
) : RecyclerView.Adapter<MsgAdapter.MyViewHolder>() {
    private val msgs = msglist
    lateinit var context: Context

    inner class MyViewHolder(val binding: ContentChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            ContentChatBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return msgs.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}