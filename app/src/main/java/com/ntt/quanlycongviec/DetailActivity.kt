package com.ntt.quanlycongviec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ntt.quanlycongviec.databinding.ActivityDetailBinding
import com.ntt.quanlycongviec.model.Job

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val job = intent.getSerializableExtra("job") as Job
        binding.tvTitle.text = job.title
        binding.tvContent.text = job.content
        binding.tvDay.text = job.day
        binding.tvTime.text = job.time
        binding.tvNote.text = job.note
    }
}