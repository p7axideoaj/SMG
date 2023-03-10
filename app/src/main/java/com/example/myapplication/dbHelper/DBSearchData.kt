package com.example.myapplication.dbHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.dataModel.SearchData

class DBSearchData(context: Context?):
        SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){
            override fun onCreate(db: SQLiteDatabase) {
                val query = ("CREATE TABLE " + TABLE_NAME + " ("
                        + TIMER_COL + " INTEGER PRIMARY KEY,"
                        + IMAGE_COL + " TEXT,"
                        + NAME_COL + " TEXT,"
                        + TITLE_COL + " TEXT)")

                // at last we are calling a exec sql method to execute above sql query
                db.execSQL(query)
            }
            fun addSearchData(
                searchData: SearchData
            ) {
                val db = this.writableDatabase
                val values = ContentValues()
                values.put(TIMER_COL, searchData.time)
                values.put(IMAGE_COL, searchData.image)
                values.put(NAME_COL, searchData.name)
                values.put(TITLE_COL, searchData.title)
                db.insert(TABLE_NAME, null, values)
                db.close()
            }
            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
                onCreate(db)
            }
            companion object {
                // creating a constant variables for our database.
                // below variable is for our database name.
                private const val DB_NAME = "coursedb"

                // below int is our database version
                private const val DB_VERSION = 1

                // below variable is for our table name.
                private const val TABLE_NAME = "SearchData"

                // below variable is for our id column.
                private const val TIMER_COL = "timestamp"

                // below variable is for our course name column
                private const val IMAGE_COL = "image"

                // below variable id for our course duration column.
                private const val NAME_COL = "name"

                // below variable for our course description column.
                private const val TITLE_COL = "title"

            }
        fun selectData(): MutableList<SearchData?> {
            val list = mutableListOf<SearchData?>()
            val select = "select * from $TABLE_NAME order by 1 desc"
            val rd = readableDatabase
            val cursor = rd.rawQuery(select, null)
            while (cursor.moveToNext()) {
                val timer = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))

                list.add(SearchData(timer, image, name, title))
            }
            cursor.close()
            rd.close()
            return list
        }
        fun selectDataByName(str: String): SearchData? {
            val select = "select * from $TABLE_NAME where name like '$str'"
            val rd = readableDatabase
            val cursor = rd.rawQuery(select, null)
            if(!cursor.moveToNext()) {
                return null
            }

                val timer = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            cursor.close()
            rd.close()
            return SearchData(timer, image, name, title)
        }
    fun selectTest(): Long? {
        val select = "select timestamp from $TABLE_NAME order by timestamp desc limit 9, 1"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        if(!cursor.moveToNext()) {
            return null
        }
        val timer = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"))
        cursor.close()
        rd.close()
        return timer
    }
    fun updateData(searchData: SearchData) {
        val values = ContentValues()
        values.put(TIMER_COL, searchData.time)
        values.put(IMAGE_COL, searchData.image)
        values.put(NAME_COL, searchData.name)
        values.put(TITLE_COL, searchData.title)
        val wd = writableDatabase // ?????? ????????????
        wd.update(TABLE_NAME, values, "name = ${searchData.name}", null) // ???????????????, ?????????, ??????
        wd.close() // ???????????? ??????
    }
    // ?????? ????????? ????????? && ????????????
    fun deleteData(str: String?) {
        var delete = if(str == null) {
                "delete from $TABLE_NAME where timestamp in (select timestamp from $TABLE_NAME order by timestamp desc limit 10, 1)"  // ?????? ?????? ??????
        } else {
            "delete from $TABLE_NAME where name like '$str'"  // ?????? ?????? ??????
        }

        val db = writableDatabase
        db.execSQL(delete)  // ?????? ?????? ??????
        db.close()
    }

}