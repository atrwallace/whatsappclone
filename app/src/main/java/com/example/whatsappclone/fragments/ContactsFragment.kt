package com.example.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.activities.LoginActivity
import com.example.whatsappclone.adapters.ContactsAdapter
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.config.UserCodec
import com.example.whatsappclone.databinding.FragmentContactsBinding
import com.example.whatsappclone.model.Users
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context

class ContactsFragment : Fragment(R.layout.fragment_contacts) {
    private lateinit var binding: FragmentContactsBinding
    private val auth = ConfigFirebase.getAuth()
    var lista = ArrayList<Users>()
    lateinit var adapter: ContactsAdapter
    private lateinit var contactEventListener: ValueEventListener
    val useref: DatabaseReference = ConfigFirebase.getDBRef().child("usuarios")
    lateinit var usernow: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setHasOptionsMenu(true)
        adapter = activity?.let { ContactsAdapter(lista, it) }!!
        contactRecycler()
        usernow = UserCodec.getUserData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        recoverContact()
    }

    override fun onStop() {
        super.onStop()
        useref.removeEventListener(contactEventListener)
        lista.clear()
    }

    fun contactRecycler() {
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        val rvContactList = binding.recyclerViewContactList
        rvContactList.layoutManager = layoutmanager
        rvContactList.setHasFixedSize(true)
        rvContactList.adapter = adapter
    }

    fun recoverContact() {

        contactEventListener = useref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dadosUser in snapshot.children) {
                    val users = dadosUser.getValue(Users::class.java)
                    if (!usernow.email.equals(users?.email)) {
                        lista.add(users!!)
                    } else {
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}