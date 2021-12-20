package com.example.whatsappclone.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.config.UserCodec
import com.example.whatsappclone.databinding.ActivityConfigBinding
import com.example.whatsappclone.model.Users
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

private const val GALLERY_REQUEST: Int = 1
private const val CAMERA_REQUEST: Int = 2

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
    private val loggedUser: Users = UserCodec.loggedUserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.samara.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViewers()
        initListeners()
    }

    private fun initViewers() {
        val user = UserCodec.getUserData()
        val url: Uri? = user.photoUrl
        if (url != null) {
            Glide.with(this.applicationContext).load(url).into(binding.imgProfilePicture)
        } else {
            binding.imgProfilePicture.setImageResource(R.drawable.usuario)
        }
        binding.inputNameProfile.setText(user.displayName)

    }

    private val validatePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val bitmap = it?.data?.extras?.get("data") as Bitmap

            binding.imgProfilePicture.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            val dados: ByteArray = byteArrayOutputStream.toByteArray()
            val idUser = UserCodec.getUserCode()
            val imgRef = storageReference.child("imagens").child(idUser).child("$idUser.jpeg")
            val uploadTask: UploadTask = imgRef.putBytes(dados)

            uploadTask.addOnFailureListener {

                Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
            }
            uploadTask.addOnSuccessListener {
                Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
                imgRef.downloadUrl.addOnCompleteListener { task ->
                    val url: Uri = task.result!!
                    updateUserPic(url)
                }
            }
        }
    private val validateGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val imguri = it?.data?.data
            binding.imgProfilePicture.setImageURI(imguri)
            val baos = ByteArrayOutputStream()
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imguri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val dados: ByteArray = baos.toByteArray()
            val idUser = UserCodec.getUserCode()
            val imgRef = storageReference.child("imagens").child(idUser).child("$idUser.jpeg")
            val uploadTask: UploadTask = imgRef.putBytes(dados)

            uploadTask.addOnFailureListener {
                Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
            }
            uploadTask.addOnSuccessListener {
                Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
                imgRef.downloadUrl.addOnCompleteListener { task ->
                    val url: Uri = task.result!!
                    updateUserPic(url)
                }
            }
        }

    private fun initListeners() {
        binding.imgProfilePicture.setOnClickListener {
            permissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE, "galeria",
                GALLERY_REQUEST
            )
        }
        binding.imgTakePicture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            validatePicture.launch(intent)
        }
        binding.imgGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            validateGallery.launch(intent)
        }
        binding.btnSaveName.setOnClickListener {
            UserCodec.updateUserName(binding.inputNameProfile.text.toString())
            loggedUser.nome = binding.inputNameProfile.text.toString()
            loggedUser.updateNameDB()
            Toast.makeText(applicationContext, "Nome alterado com sucesso", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun permissions(permission: String, name: String, requestcode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                )
                        == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        applicationContext,
                        "$name permissão concedida!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    dialogPerm(permission, name, requestcode)
                }
                else -> {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(permission),
                        requestcode
                    )

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permissão negada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Permissão já aceita", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            GALLERY_REQUEST -> innerCheck("Galeria")
            CAMERA_REQUEST -> innerCheck("Câmera")
        }
    }

    private fun dialogPerm(permission: String, name: String, requestcode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permissão para acessar sua $name é requisitado para usar este aplicativo")
            setTitle("Permissão necessária")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@ConfigActivity,
                    arrayOf(permission),
                    requestcode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateUserPic(url: Uri) {
        UserCodec.updatePicture(url)
        loggedUser.foto = url.toString()
        loggedUser.updateNameDB()
    }
}