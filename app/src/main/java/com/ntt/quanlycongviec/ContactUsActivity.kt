package com.ntt.quanlycongviec

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ntt.quanlycongviec.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEmailUs.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                type = "text/plain"
                data = Uri.parse("mailto:thanhnt.B17AT168@.stu.ptit.edu.vn")
            }
            startActivity(emailIntent)
        }
        binding.btnVisitFb.setOnClickListener {
            val fbIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.facebook.com/thanh.nguyentuan.9634")
            }
            startActivity(fbIntent)
        }
    }
}