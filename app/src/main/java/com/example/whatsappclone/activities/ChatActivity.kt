package com.example.whatsappclone.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.adapters.MsgAdapter
import com.example.whatsappclone.config.Base64Custom
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.config.UserCodec
import com.example.whatsappclone.databinding.ActivityChatBinding
import com.example.whatsappclone.model.Msgs
import com.example.whatsappclone.model.Users
import com.google.firebase.auth.FirebaseUser

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var userReached: Users
    val sendingUserMsg = UserCodec.getUserCode()
    lateinit var reachedUserMsg: String
    lateinit var adapter: MsgAdapter
    private var msgs = ArrayList<Msgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarchat.toolbar.title = ""
        initviewers()
        initListeners()
        recoverIntent()

    }

    fun initviewers() {}
    fun initListeners() {

        binding.contentChat.fabSend.setOnClickListener { sendMsg() }
    }

    private fun recoverIntent() {

        val bundle = intent.extras
        if (bundle != null) {
            userReached = bundle.getSerializable("chatContact") as Users
            binding.userName.text = userReached.nome
            val foto = userReached.foto
            val url: Uri = Uri.parse(userReached.foto)
            Glide.with(this@ChatActivity).load(url).into(binding.imgProfilePicture)

        }
        reachedUserMsg = Base64Custom.codificar(userReached.email)
    }

    private fun sendMsg() {
        val msg = binding.contentChat.inputMsg.text.toString()
        val msgcontent = Msgs()
        if (!msg.isEmpty()) {
            msgcontent.idUsuario = sendingUserMsg
            msgcontent.msgs = msg
            saveMsg(sendingUserMsg, reachedUserMsg, msg)
        } else {
            Toast.makeText(
                applicationContext,
                "Digite uma mensagem para salvar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun adapterandRecycler() {

        adapter = MsgAdapter(msgs, applicationContext)

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val msgsList = binding.contentChat.recyclerMsgs
        msgsList.layoutManager = layoutmanager
        msgsList.setHasFixedSize(true)
        msgsList.adapter = adapter


    }

    fun saveMsg(idRemet: String, idDestin: String, msg: String) {
        val ref = ConfigFirebase.getDBRef()
        val msgref = ref.child("mensagens")

        msgref.child(idRemet).child(idDestin).push().setValue(msg)
        binding.contentChat.inputMsg.setText("")
    }
}