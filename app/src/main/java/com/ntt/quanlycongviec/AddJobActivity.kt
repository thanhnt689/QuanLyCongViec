package com.ntt.quanlycongviec

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ntt.quanlycongviec.databinding.ActivityAddJobBinding
import com.ntt.quanlycongviec.model.Job
import java.text.SimpleDateFormat

class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.imgDay.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val date: DatePickerDialog = DatePickerDialog(
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
        binding.btnAdd.setOnClickListener {
            val intent = Intent()
            if (binding.edtTitle.text.isEmpty() || binding.edtContent.text.isEmpty() || binding.edtDay.text.isEmpty() || binding.edtTime.text.isEmpty()) {
                Toast.makeText(this, "Vui long nhap day du thong tin", Toast.LENGTH_LONG).show()
            } else {
                val title = binding.edtTitle.text.toString()
                val content = binding.edtContent.text.toString()
                val day = binding.edtDay.text.toString()
                val time = binding.edtTime.text.toString()
                val note = binding.edtNote.text.toString()

                val job = Job(title, content, day, time, note)
                val bundle = Bundle()
                bundle.putSerializable("n_job", job)
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}
