package com.ntt.quanlycongviec.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu

import androidx.recyclerview.widget.RecyclerView
import com.ntt.quanlycongviec.R
import com.ntt.quanlycongviec.callback.OnJobItemClickListener
import com.ntt.quanlycongviec.databinding.ItemJobBinding
import com.ntt.quanlycongviec.model.Job

class JobAdapter(private var jobs: ArrayList<Job>, private var callback: OnJobItemClickListener) :
    RecyclerView.Adapter<JobAdapter.ViewHolder>(), Filterable {

    var jobFiltered = ArrayList<Job>()

    init {
        jobFiltered.addAll(jobs)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Job) {
            binding.tvTitle.text = job.title
            binding.tvContent.text = job.content
            binding.tvDay.text = job.day
            binding.tvTime.text = job.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ItemJobBinding =
            ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobFiltered[position])

        holder.binding.ibMore.setOnClickListener {
            showPopupMenu(it, position)
        }
        holder.binding.root.setOnClickListener {
            callback.onClick(jobFiltered[position])
        }
        holder.binding.cbNotify.setOnCheckedChangeListener { button, b ->
            if (b) {
                callback.onClickCheckBox(position)
            }
        }

    }

    private fun showPopupMenu(it: View?, position: Int) {
        val popupMenu: PopupMenu = PopupMenu(it?.context, it).also {
            it.inflate(R.menu.popup_menu)
            it.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    when (p0?.itemId) {
                        R.id.actionPopupView -> callback.onClick(jobFiltered[position])
                        R.id.actionPopupEdit -> callback.onClickEdit(
                            jobFiltered[position],
                            position
                        )
                        R.id.actionPopupShare -> callback.onClickShare(jobFiltered[position])
                        R.id.actionPopupDelete -> callback.onClickDelete(jobFiltered[position])
                    }
                    return true
                }
            })
        }
        popupMenu.show()
    }


    override fun getItemCount(): Int = jobFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val jobSearch = p0.toString()
                if (jobSearch.isEmpty()) {
                    jobFiltered.clear()
                    jobFiltered.addAll(jobs)
                } else {
                    jobFiltered.clear()
                    val resultList = ArrayList<Job>()
                    for (job in jobs) {
                        if (job.title.toLowerCase().contains(
                                p0.toString().toLowerCase()
                            ) || job.day.contains(p0.toString())
                        ) {
                            resultList.add(job)
                        }
                    }
                    jobFiltered.addAll(resultList)
                }
                val filterResults = FilterResults()
                filterResults.values = jobFiltered
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                jobFiltered = p1?.values as ArrayList<Job>
                notifyDataSetChanged()
            }

        }
    }

    fun addJob(job: Job) {
        this.jobs.add(job)
        this.jobFiltered.clear()
        this.jobFiltered.addAll(jobs)
        notifyDataSetChanged()

    }

    fun removeJob(job: Job) {
        this.jobs.remove(job)
        this.jobFiltered.clear()
        this.jobFiltered.addAll(jobs)
        notifyDataSetChanged()
    }

    fun editJob(job: Job, pos: Int) {
        this.jobs[pos] = job
        this.jobFiltered.clear()
        this.jobFiltered.addAll(jobs)
        notifyDataSetChanged()
    }

    fun change() {
        this.jobFiltered.clear()
        this.jobFiltered.addAll(jobs)
        notifyDataSetChanged()
    }


}