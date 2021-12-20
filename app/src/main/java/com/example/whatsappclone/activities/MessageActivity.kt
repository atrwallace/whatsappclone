package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityMessageBinding
import com.example.whatsappclone.databinding.ToolbarBinding
import com.example.whatsappclone.fragments.ContactsFragment
import com.example.whatsappclone.fragments.MsgsFragment
import com.example.whatsappclone.fragments.PagerAdapter
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewers()
        initListeners()
        setupViewPager()
        setSupportActionBar(binding.toolbar.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.menu_main, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                logoutUser()
                finish()
            }
            R.id.menuConfig -> {
                startActivity(Intent(this@MessageActivity, ConfigActivity::class.java))
            }

            else -> Toast.makeText(applicationContext, "Celular irá explodir!", Toast.LENGTH_SHORT)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun initViewers() {}
    fun initListeners() {
    }

    fun logoutUser() {
        Toast.makeText(applicationContext, "Deslogado com sucesso", Toast.LENGTH_SHORT).show()
        try {
            auth.signOut()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFrag(MsgsFragment(), "Mensagens")
        adapter.addFrag(ContactsFragment(), "Contatos")
        binding.viewpager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)


    }
}