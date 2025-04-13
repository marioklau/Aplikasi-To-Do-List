package com.example.jejakharian

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Bantuan_Kalender(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "kalender.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "kalender_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "judul"
        private const val COLUMN_DESCRIPTION = "deskripsi"
        private const val COLUMN_DATE = "tanggal"
        private const val COLUMN_TIME = "waktu"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Menambahkan catatan baru ke database
    fun addKalender(judul: String, deskripsi: String, tanggal: String, waktu: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, judul)
        values.put(COLUMN_DESCRIPTION, deskripsi)
        values.put(COLUMN_DATE, tanggal)
        values.put(COLUMN_TIME, waktu)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    // Menampilkan semua catatan dari database
    fun showData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    // Menghapus catatan berdasarkan ID
    fun deleteKalender(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Memperbarui catatan berdasarkan ID
    fun updateKalender(id: Int, judul: String, deskripsi: String, tanggal: String, waktu: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, judul)
        values.put(COLUMN_DESCRIPTION, deskripsi)
        values.put(COLUMN_DATE, tanggal)
        values.put(COLUMN_TIME, waktu)

        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun isKalenderExists(judul: String): Boolean {
        // Logika untuk memeriksa apakah catatan dengan judul yang sama sudah ada
        val db = this.readableDatabase
        val cursor = db.query("kalender_table", null, "judul = ?", arrayOf(judul), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }


}
