package com.talentProgramming.midExam.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.talentProgramming.midExam.model.UserModel

@RequiresApi(Build.VERSION_CODES.P)
class UserDB(context: Context) : SQLiteOpenHelper(context, "USER_DB",  null, 1) {
    val TBL_USER= "tbl_user"
    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL("""CREATE TABLE $TBL_USER (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT , status TEXT)""")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertUser(userName : String, password : String) : Boolean {
        val db = this@UserDB.writableDatabase
        val cv = ContentValues()
        cv.put("username", userName)
        cv.put("password", password)
         return try {
            db.insertOrThrow(TBL_USER, null, cv)
            true
        }catch (e : Exception){
            false
        }
        finally {
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getAllUser() : List<UserModel>{
        val userList = ArrayList<UserModel>()
        val db = this@UserDB.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_USER",null)
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                userList.add(UserModel(
                    cursor.getInt(cursor.getColumnIndex("user_id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("status"))
                    ))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return userList
    }

    fun updateGenre(userId : Int ,genreName : String) : Boolean{
        val db = this@UserDB.writableDatabase
        val cv = ContentValues()
        cv.put("g_name", genreName)
        try {
            db.update(TBL_USER, cv, "user_id = $userId", null)
            db.close()
            return true
        }catch (e : Exception){
            db.close()
            return false
        }
    }

    fun deleteGenre(genreId : Int){
        val db = this@UserDB.writableDatabase
        try {
            db.delete(TBL_USER, "g_id = $genreId", null)
            db.close()
        }catch(_ : Exception) {
            db.close()
        }
    }

    fun checkUsernameExist(username : String) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_USER WHERE username = ?", arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
    fun getCurrentUsername(userId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT username FROM $TBL_USER WHERE user_id = ?", null)
        var username: String? = null
        cursor.use {
            if (it.moveToFirst()) {
                username = it.getString(it.getColumnIndexOrThrow("username"))
            }
        }
        db.close()
        return username
    }

}