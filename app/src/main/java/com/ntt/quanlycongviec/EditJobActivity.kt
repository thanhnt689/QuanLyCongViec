package com.ntt.quanlycongviec

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ntt.quanlycongviec.databinding.ActivityEditJobBinding
import com.ntt.quanlycongviec.model.Job
import java.text.SimpleDateFormat

class EditJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val job = intent.getSerializableExtra("job") as Job
        binding.edtTitle.setText(job.title)
        binding.edtContent.setText(job.content)
        binding.edtDay.setText(job.day)
        binding.edtTime.setText(job.time)
        binding.edtNote.setText(job.note)

        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.imgDay.setOnClickListener {
            var calendar: Calendar = Calendar.getInstance()

            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var month = calendar.get(Calendar.MONTH)
            var year = calendar.get(Calendar.YEAR)

            var date: DatePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    calendar.set(i, i2, i3, 0, 0)
                    binding.edtDay.setText(simpleDateFormat.format(calendar.time))
                }, year, month, day
            )
            date.show()
        }
        binding.imgTime.setOnClickListener {
            var calendar: Calendar = Calendar.getInstance()

            var hours = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)

            var time: TimePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
                    calendar.set(0, 0, 0, i, i2)
                    binding.edtTime.setText(simpleDateFormat.format(calendar.time))

                }, hours, minute, false
            )
            time.show()
        }
        binding.btnEdit.setOnClickListener {
            if (binding.edtTitle.text.isEmpty() || binding.edtContent.text.isEmpty() || binding.edtDay.text.isEmpty() || binding.edtTime.text.isEmpty()) {
                Toast.makeText(this, "Vui long nhap day du thong tin", Toast.LENGTH_LONG).show()
            } else {
                job.title = binding.edtTitle.text.toString()
                job.content = binding.edtContent.text.toString()
                job.day = binding.edtDay.text.toString()
                job.time = binding.edtTime.text.toString()
                job.note = binding.edtNote.text.toString()

                val result = Intent()
                val bundle = Bundle()
                bundle.putSerializable("job", job)
                result.putExtra("position", intent.getIntExtra("position", -1))
                result.putExtras(bundle)
                setResult(RESULT_OK, result)
                finish()
            }
        }

    }
}