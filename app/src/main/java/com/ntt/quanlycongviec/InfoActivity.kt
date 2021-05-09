package com.ntt.quanlycongviec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ntt.quanlycongviec.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}