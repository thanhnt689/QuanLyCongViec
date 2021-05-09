package com.ntt.quanlycongviec.callback

import com.ntt.quanlycongviec.model.Job

interface OnJobItemClickListener {
    fun onClick(job: Job)
    fun onClickEdit(job: Job, position: Int)
    fun onClickDelete(job: Job)
    fun onClickCheckBox(position: Int)
    fun onClickShare(job: Job)
}