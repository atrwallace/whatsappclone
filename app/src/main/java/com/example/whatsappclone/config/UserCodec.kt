package com.example.whatsappclone.config

import android.net.Uri
import android.util.Log
import com.example.whatsappclone.model.Users
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserCodec {

    companion object {

        fun getUserCode(): String {
            val auth = ConfigFirebase.getAuth()
            val user = auth.currentUser?.email.toString()
            val coding = Base64Custom.codificar(user)

            return coding
        }

        fun getUserData(): FirebaseUser {
            val auth = ConfigFirebase.getAuth()

            return auth.currentUser!!

        }

        fun updatePicture(url: Uri): Boolean {
            try {
                val user = getUserData()
                val profile = UserProfileChangeRequest.Builder().setPhotoUri(url).build()
                user.updateProfile(profile).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.")
                    } else {

                    }
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }

        fun updateUserName(nome: String): Boolean {
            try {
                val user = getUserData()
                val profile = UserProfileChangeRequest.Builder().setDisplayName(nome).build()
                user.updateProfile(profile).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.")
                    } else {

                    }
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }

        fun loggedUserData(): Users {
            val fbUser: FirebaseUser = getUserData()
            val user = Users()
            user.email = fbUser.email.toString()
            user.nome = fbUser.displayName.toString()

            if (fbUser.photoUrl == null) {
                user.foto = ""
            } else {
                user.foto = fbUser.photoUrl.toString()
            }

            return user
        }
    }
}