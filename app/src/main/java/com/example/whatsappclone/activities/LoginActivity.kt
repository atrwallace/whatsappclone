package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsappclone.databinding.ActivityLoginBinding
import com.example.whatsappclone.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user = Users()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewers()
        initListeners()
    }

    fun initViewers() {}
    fun initListeners() {
        binding.btnConfirmLogin.setOnClickListener { validatefields() }
    }

    fun validatefields() {
        if (!binding.inputEmail.text.toString().isEmpty()) {
            if (!binding.inputPassword.text.toString().isEmpty()) {
                user.email = binding.inputEmail.text.toString()
                user.senha = binding.inputPassword.text.toString()
                login()
            } else {
                toast("Informe sua senha!")
            }
        } else {
            toast("Informe seu email!")
        }

    }

    fun login() {
        auth.signInWithEmailAndPassword(user.email, user.senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Login bem sucedido!")
                    startActivity(Intent(this@LoginActivity, MessageActivity::class.java))
                    finish()
                } else {
                    var exc = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        exc = "Essa conta não existe ou foi desativada"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        exc = "Senha ou Email inválidos"
                    } catch (e: Exception) {
                        exc = "Erro: " + e.message
                        e.printStackTrace()
                    }
                    toast(exc)
                }
            }
    }

    fun toast(text: String) {

        return Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}