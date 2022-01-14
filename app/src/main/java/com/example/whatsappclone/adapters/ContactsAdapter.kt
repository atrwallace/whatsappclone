package com.example.whatsappclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.activities.ChatActivity
import com.example.whatsappclone.databinding.AdapterContactsBinding
import com.example.whatsappclone.model.Users


class ContactsAdapter(
    contactlist: List<Users>,
    context: Context
) :
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    private val contacts = contactlist
    lateinit var context: Context

    inner class MyViewHolder(val binding: AdapterContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {


                val position: Int = adapterPosition
                val posUser = contacts.get(position)
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("chatContact", posUser )
                context.startActivity(intent)

            }
        }
    }

    interface onListClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            AdapterContactsBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: Users = contacts[position]
        holder.binding.getNome.text = user.nome
        holder.binding.getEmail.text = user.email

        if (user.foto != null) {
            val uri = Uri.parse(user.foto)
            Glide.with(context).load(uri).into(holder.binding.profilepicture)
        } else {
            holder.binding.profilepicture.setImageResource(R.drawable.padrao)
        }
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

}