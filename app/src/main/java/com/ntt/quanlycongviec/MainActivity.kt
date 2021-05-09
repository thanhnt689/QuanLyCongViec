package com.ntt.quanlycongviec

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntt.quanlycongviec.adapter.JobAdapter
import com.ntt.quanlycongviec.callback.OnJobItemClickListener
import com.ntt.quanlycongviec.databinding.ActivityMainBinding
import com.ntt.quanlycongviec.model.Job
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.widget.SearchView;
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnJobItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JobAdapter
    private var jobs = arrayListOf<Job>()
    private val REQUEST_EDIT = 10
    private val REQUEST_ADD = 1

    private lateinit var dataBase: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = DataBaseHelper(this)
        jobs = dataBase.viewJob()

        adapter = JobAdapter(jobs, this)
        binding.rvJob.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvJob.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        binding.navMain.setNavigationItemSelectedListener(this)


        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddJobActivity::class.java)
            setResult(RESULT_OK, intent)
            startActivityForResult(intent, REQUEST_ADD)
        }


        binding.btnSort.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(it?.context, it).also {
                it.inflate(R.menu.menu_sort)
                it.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        return when (p0?.itemId) {
                            R.id.sortTitle -> {
                                jobs.sortBy { it.title }
                                adapter.change()
                                return true
                            }
                            R.id.sortContent -> {
                                jobs.sortBy { it.content }
                                adapter.change()
                                return true
                            }
                            R.id.sortTime -> {
                                jobs.sortWith(compareBy<Job> { it.day }.thenBy { it.time })
                                adapter.change()
                                return true
                            }
                            else -> return false
                        }
                    }
                })
            }
            popupMenu.show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.actionSearch)?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter?.filter?.filter(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter?.filter?.filter(p0)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }
        if (id == R.id.actionSearch) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onClick(job: Job) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("job", job)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClickEdit(job: Job, position: Int) {
        val intent = Intent(this, EditJobActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("job", job)
        intent.putExtras(bundle)
        intent.putExtra("position", jobs.indexOf(job))
        startActivityForResult(intent, REQUEST_EDIT)
    }


    override fun onClickDelete(job: Job) {
        val index = jobs.indexOf(job) + 1
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Warring")

        builder.setMessage("Are you sure you want delete this!")
        builder.setNegativeButton(
            "No"
        ) { p0, p1 ->
            p0.dismiss()
            Toast.makeText(this, "You are not agree", Toast.LENGTH_SHORT).show()
        }

        builder.setPositiveButton(
            "Yes"
        ) { p0, p1 ->
//            dataBase.QueryData("DELETE FROM Job WHERE id = '$index'")
            val db = DataBaseHelper(this)
            db.deleteJob(job, index)
            adapter.removeJob(job)
            p0.dismiss()
        }
        builder.show()
    }

    override fun onClickCheckBox(position: Int) {

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val amIntent = Intent(this, AlarmReceiver::class.java)


//        var strDate = "${jobs[position].day} ${jobs[position].time}"


        val firstApiFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
//        val date =
//            LocalDate.parse("${jobs[position].day} ${jobs[position].time}", firstApiFormat)
        val time =
            LocalTime.parse("${jobs[position].day} ${jobs[position].time}", firstApiFormat)

        var calendar: Calendar = Calendar.getInstance()


        calendar.set(Calendar.HOUR_OF_DAY, time.hour)
        calendar.set(Calendar.MINUTE, time.minute)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, amIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    override fun onClickShare(job: Job) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Content: ${job.content}, Title: ${job.title}, Day: ${job.day}, Time: ${job.time}, Note: ${job.note}"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share to..."), null)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) {
                val job = data?.getSerializableExtra("job") as Job
                val pos = data?.getIntExtra("position", -1)

                val id = pos + 1
                val db = DataBaseHelper(this)
                db.updateJob(job, id)

                adapter.editJob(job, pos)

            }
        } else if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                val job = data?.getSerializableExtra("n_job") as Job

                val db = DataBaseHelper(this)
                db.addJob(job)

                adapter.addJob(job)
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawer.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_contact -> {
                val intent = Intent(this, ContactUsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_game -> {
                val intent = Intent(this, MiniGameActivity::class.java)
                startActivity(intent)
                true
            }
            else -> throw  IllegalArgumentException()
        }
    }


}