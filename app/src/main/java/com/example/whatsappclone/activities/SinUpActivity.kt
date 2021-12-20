package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsappclone.config.Base64Custom
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.config.UserCodec
import com.example.whatsappclone.databinding.ActivitySinUpBinding
import com.example.whatsappclone.model.Users
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class SinUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySinUpBinding
    private var user = Users("Lombra")
    private var auth = ConfigFirebase.getAuth()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewers()
        initListeners()
    }

    fun initViewers() {}
    fun initListeners() {
        binding.btnConfirmLogin.setOnClickListener { validateFields() }
    }

    fun validateFields() {
        if (!binding.inputName.text.toString().isEmpty()) {
            if (!binding.inputEmail.text.toString().isEmpty()) {
                if (!binding.inputPassword.text.toString().isEmpty()) {
                    user.nome = binding.inputName.text.toString()
                    user.email = binding.inputEmail.text.toString()
                    user.senha = binding.inputPassword.text.toString()
                    savingUser()
                } else {
                    toast("Insira uma senha!")
                }
            } else {
                toast("Insira um email!")
            }
        } else {
            toast("Insira um nome")
        }

    }

    fun savingUser() {
        auth.createUserWithEmailAndPassword(user.email, user.senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = Base64Custom.codificar(user.email)
                    user.idUser = id
                    user.salvar()
                    toast("Usuário criado com sucesso!")
                    UserCodec.updateUserName(user.nome)
                    startActivity(Intent(this@SinUpActivity, LoginActivity::class.java))
                    finish()
                } else {
                    var exc = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        exc = "Digite uma senha mais forte"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        exc = "Digite um email válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        exc = "Essa conta já existe!"
                    } catch (e: Exception) {
                        exc = "Erro ao cadastrar usuário" + e.message
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