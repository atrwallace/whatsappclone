package com.example.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.whatsappclone.R
import com.example.whatsappclone.activities.LoginActivity
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.databinding.FragmentMsgsBinding

class MsgsFragment : Fragment() {
private lateinit var binding: FragmentMsgsBinding
private val auth = ConfigFirebase.getAuth()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMsgsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}