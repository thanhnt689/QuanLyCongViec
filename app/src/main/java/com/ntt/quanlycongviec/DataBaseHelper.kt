package com.ntt.quanlycongviec

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ntt.quanlycongviec.model.Job


class DataBaseHelper(
    context: Context?
) : SQLiteOpenHelper(context, "note.db", null, 1) {

    private val TABLE_CONTACTS = "Job"
    private val KEY_ID = "id"
    private val KEY_TITLE = "title"
    private val KEY_CONTENT = "content"
    private val KEY_DAY = "day"
    private val KEY_TIME = "time"
    private val KEY_NOTE = "note"

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_CONTACT_TABLE: String =
            ("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " VARCHAR(100),"
                    + KEY_CONTENT + " VARCHAR(100),"
                    + KEY_DAY + " VARCHAR(100),"
                    + KEY_TIME + " VARCHAR(100),"
                    + KEY_NOTE + " VARCHAR(100)" + ")")
        p0?.execSQL(CREATE_CONTACT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(p0)
    }

    fun addJob(job: Job): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, job.title)
        contentValues.put(KEY_CONTENT, job.content)
        contentValues.put(KEY_DAY, job.day)
        contentValues.put(KEY_TIME, job.time)
        contentValues.put(KEY_NOTE, job.note)

        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close()
        return success
    }

    fun viewJob(): ArrayList<Job> {
        val jobList: ArrayList<Job> = ArrayList<Job>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(0)
                val title: String = cursor.getString(1)
                val content: String = cursor.getString(2)
                val day: String = cursor.getString(3)
                val time: String = cursor.getString(4)
                val note: String = cursor.getString(5)
                jobList.add(Job(title, content, day, time, note))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return jobList
    }

    fun deleteJob(job: Job, id: Int): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, id)
        val success = db.delete(TABLE_CONTACTS, "$KEY_ID=$id", null)
        db.close()
        return success
    }

    fun updateJob(job: Job, id: Int): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, job.title)
        contentValues.put(KEY_CONTENT, job.content)
        contentValues.put(KEY_DAY, job.day)
        contentValues.put(KEY_TIME, job.time)
        contentValues.put(KEY_NOTE, job.note)

        val success = db.update(TABLE_CONTACTS, contentValues, "$KEY_ID=$id", null)
        db.close()
        return success
    }

    fun getJobCount(): Int {
        val db: SQLiteDatabase = this.readableDatabase
        val countQuery = "SELECT * FROM $TABLE_CONTACTS"
        val cursor: Cursor = db.rawQuery(countQuery, null)
        cursor.close()

        return cursor.count
    }
}