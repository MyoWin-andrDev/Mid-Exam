package com.talentProgramming.midExam.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.model.UserModel

@RequiresApi(Build.VERSION_CODES.P)
class UserDB(context: Context) : SQLiteOpenHelper(context, "USER_DB",  null, 1) {
    val TBL_USER= "tbl_user"
    val TBL_STATUS = "tbl_status"
    private lateinit var db : SQLiteDatabase

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL("""CREATE TABLE $TBL_USER (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)""")
        sqLiteDatabase?.execSQL("CREATE TABLE $TBL_STATUS (status_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, username TEXT NOT NULL ,status TEXT NOT NULL, uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (user_id) REFERENCES $TBL_USER(user_id) ON DELETE CASCADE)")
    }

    override fun onUpgrade(sqliteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        sqliteDatabase?.execSQL("DROP TABLE IF EXISTS $TBL_USER")
        sqliteDatabase?.execSQL("DROP TABLE IF EXISTS $TBL_STATUS")
        onCreate(sqliteDatabase)
    }
    //TBL_USER Functions
    fun insertUser(userName : String, password : String) : Boolean {
        db = this@UserDB.writableDatabase
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
        db = this@UserDB.readableDatabase
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

    fun updateUser(userId : Int ,username : String) : Boolean{
        db = this@UserDB.writableDatabase
        val cv = ContentValues()
        cv.put("username", username)
        try {
            db.update(TBL_USER, cv, "user_id = $userId", null)
            db.close()
            return true
        }catch (e : Exception){
            db.close()
            return false
        }
    }

    fun deleteUser(userId : Int){
        db = this@UserDB.writableDatabase
        try {
            db.delete(TBL_USER, "g_id = $userId", null)
            db.close()
        }catch(_ : Exception) {
            db.close()
        }
    }

    fun getUserId(username: String) : Int {
        db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_USER WHERE username = ?", arrayOf(username))
        var userId = 0
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return userId
    }

    fun checkUsernameExist(username : String) : Boolean {
        db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_USER WHERE username = ?", arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun checkPassword(username : String) : String{
        db = this.readableDatabase
        val cursor = db.rawQuery("SELECT password FROM $TBL_USER WHERE username = $username", null)
        var password : String = ""
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast){
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return password
    }
    //TBL_STATUS Functions

    fun insertStatus(userId : Int, username : String ,status : String) : Boolean {
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put("user_id", userId)
        cv.put("username", username)
        cv.put("status", status)
        return try {
            db.insert(TBL_STATUS , null, cv)
            true
        }
        catch (e : Exception){
            false
        }
        finally {
            db.close()
        }
    }

    fun getUserUploadStatus(username : String) : List<StatusModel>{
        db = this.readableDatabase
        val statusList = arrayListOf<StatusModel>()
        val cursor = db.rawQuery("SELECT * FROM $TBL_STATUS us JOIN $TBL_USER u ON us.user_id = u.user_id WHERE u.username = ?", arrayOf(username))
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                statusList.add(StatusModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("status_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status")),
                    cursor.getString(cursor.getColumnIndexOrThrow("uploaded_at"))
                ))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return statusList
    }

    fun deleteStatus(id : Int) : Boolean {
        db = this@UserDB.writableDatabase
        return try{
            db.delete(TBL_STATUS, "status_id = ?" , arrayOf(id.toString()))
            true
        }
        catch( _ : Exception){
            false
        }
        finally {
            db.close()
        }
    }

    fun updateStatus(updateStatus : String , id : Int) : Boolean {
        db = this@UserDB.writableDatabase
        val cv = ContentValues()
        cv.put("status", updateStatus)
        return try{
            db.update(TBL_STATUS, cv, "status_id = ?", arrayOf(id.toString()))
            true
        }
        catch (_ : Exception){
            false
        }
        finally {
            db.close()
        }
    }
}