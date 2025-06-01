package com.talentProgramming.midExam.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.model.UserModel

class UserDB(context: Context) : SQLiteOpenHelper(context, "USER_DB", null, 1) {
    private val TBL_USER = "tbl_user"
    private val TBL_STATUS = "tbl_status"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
        CREATE TABLE $TBL_USER (
            user_id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE,
            password TEXT
        )
    """.trimIndent()
        )

        db?.execSQL(
            """
        CREATE TABLE $TBL_STATUS (
            status_id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            username TEXT NOT NULL,
            status TEXT NOT NULL,
            uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES $TBL_USER(user_id) ON DELETE CASCADE
        )
    """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_STATUS")
        db?.execSQL("DROP TABLE IF EXISTS $TBL_USER")
        onCreate(db)
    }


    //TBL_USER Functions
    fun insertUser(userName: String, password: String): Boolean = runCatching {
        val values = ContentValues().apply {
            put("username", userName)
            put("password", password)
        }

        writableDatabase.use { db ->
            db.insertOrThrow(TBL_USER, null, values)
        }
        true
    }.getOrDefault(false)


    fun updateUser(userId: Int, username: String): Boolean = runCatching {
        val values = ContentValues().apply {
            put("username", username)
        }

        writableDatabase.use { db ->
            db.update(TBL_USER, values, "user_id = ?", arrayOf(userId.toString()))
        }
        true
    }.getOrDefault(false)

    fun updatePassword(id: Int, password: String): Boolean = runCatching {
        val values = ContentValues().apply {
            put("password", password)
        }

        writableDatabase.use { db ->
            db.update(TBL_USER, values, "user_id = ?", arrayOf(id.toString()))
        }
        true
    }.getOrDefault(false)

    fun deleteUser(userId: Int): Boolean = runCatching {
        writableDatabase.use { db ->
            db.delete(TBL_USER, "user_id = ?", arrayOf(userId.toString()))
        }
        true
    }.getOrDefault(false)

    fun checkUsernameExist(username: String): Boolean {
        val query = "SELECT 1 FROM $TBL_USER WHERE username = ? LIMIT 1"

        return readableDatabase.use { db ->
            db.rawQuery(query, arrayOf(username)).use { cursor ->
                cursor.moveToFirst() // returns true if at least one row exists
            }
        }
    }

    fun checkPassword(username: String): String {
        val query = "SELECT password FROM $TBL_USER WHERE username = ?"

        return readableDatabase.use { db ->
            db.rawQuery(query, arrayOf(username)).use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndexOrThrow("password"))
                } else {
                    ""
                }
            }
        }
    }

    fun getUser(username: String, password: String): UserModel? {
        val query = "SELECT * FROM $TBL_USER WHERE username = ? AND password = ?"

        return readableDatabase.use { db ->
            db.rawQuery(query, arrayOf(username, password)).use { cursor ->
                if (cursor.moveToFirst()) {
                    UserModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        userName = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    )
                } else null
            }
        }
    }

    //TBL_STATUS Functions

    fun insertStatus(userId: Int, username: String, status: String): Boolean = runCatching {
        val values = ContentValues().apply {
            put("user_id", userId)
            put("username", username)
            put("status", status)
        }

        writableDatabase.use { db ->
            db.insert(TBL_STATUS, null, values)
        }

        true
    }.getOrDefault(false)

    fun getUserUploadStatus(userId: Int): List<StatusModel> {
        val query = """
        SELECT * FROM $TBL_STATUS us
        JOIN $TBL_USER u ON us.user_id = u.user_id
        WHERE us.user_id = ?
    """.trimIndent()

        return readableDatabase.use { db ->
            db.rawQuery(query, arrayOf(userId.toString())).use { cursor ->
                val statusList = mutableListOf<StatusModel>()
                while (cursor.moveToNext()) {
                    statusList.add(
                        StatusModel(
                            statusId = cursor.getInt(cursor.getColumnIndexOrThrow("status_id")),
                            userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                            username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                            status = cursor.getString(cursor.getColumnIndexOrThrow("status")),
                            timeStamp = cursor.getString(cursor.getColumnIndexOrThrow("uploaded_at")),
                        )
                    )
                }
                statusList
            }
        }
    }

    fun deleteStatus(id: Int): Boolean = runCatching {
        writableDatabase.use { db ->
            db.delete(TBL_STATUS, "status_id = ?", arrayOf(id.toString()))
        }
        true
    }.getOrDefault(false)

    fun updateStatus(status: String, id: Int): Boolean = runCatching {
        val values = ContentValues().apply {
            put("status", status)
        }
        writableDatabase.use { db ->
            db.update(TBL_STATUS, values, "status_id = ?", arrayOf(id.toString()))
        }
        true
    }.getOrDefault(false)

}