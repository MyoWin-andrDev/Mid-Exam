package com.talentProgramming.midExam.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StatusDB(context : Context) : SQLiteOpenHelper(context, "STATUS_DB", null, 1){
    val tbl_status = "CREATE TABLE "
    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}