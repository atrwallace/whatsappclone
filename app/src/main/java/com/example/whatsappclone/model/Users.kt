package com.example.whatsappclone.model

import com.example.whatsappclone.config.Base64Custom
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.config.UserCodec
import com.google.firebase.database.Exclude

class Users(
    var nome: String = "",
    var email: String = "",
    var foto: String = "",
    @get: Exclude var senha: String = "",
    @get: Exclude var idUser: String = ""
) {

    fun salvar() {
        val db = ConfigFirebase.getDBRef()

        db.child("usuarios").child(this.idUser).setValue(this)
    }

    fun updateNameDB() {
        val dbd = ConfigFirebase.getDBRef()
        val iduser = UserCodec.getUserCode()

        val dbref = dbd.child("usuarios").child(iduser)
        val uservalue: Map<String, Any?> = mapV()
        dbref.updateChildren(uservalue)

    }

    @Exclude
    fun mapV(): Map<String, Any?> {
        val usermap: HashMap<String, Any?> = HashMap()
        usermap.put("email", email)
        usermap.put("nome", nome)
        usermap.put("foto", foto)

        return usermap
    }
}