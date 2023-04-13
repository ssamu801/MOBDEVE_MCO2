package com.mobdeve.s11.group12.mco2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    companion object {
        private var instance: MyDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDbHelper? {
            if (instance == null) {
                instance = MyDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        onCreate(sqLiteDatabase)
    }

    fun getAllEntriesDefault(): ArrayList<Entry>  {
        val database: SQLiteDatabase = this.readableDatabase

        val e : Cursor = database.query(
                DbReferences.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            DbReferences._ID + " ASC, " + DbReferences.COLUMN_NAME_ENTRY_DATE + " ASC",
            null
        )

        val entries : ArrayList<Entry>  = ArrayList()

        while(e.moveToNext()) {
            entries.add(Entry(
                    e.getDouble(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LATITUDE)),
                    e.getDouble(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LONGITUDE)),
                    e.getString(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_ENTRY_LOCATION)),
                    e.getString(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_ENTRY_DATE)),
                    e.getString(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_ENTRY_NOTES)),
                    e.getString(e.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_IMAGE_URI)),
                    e.getLong(e.getColumnIndexOrThrow(DbReferences._ID))
            ))
        }

        e.close()

        return entries
    }

    @Synchronized
    fun insertEntry(e: Entry): Long {

        val database = this.writableDatabase

        val values = ContentValues()
        values.put(DbReferences.COLUMN_NAME_LATITUDE, e.latitude)
        values.put(DbReferences.COLUMN_NAME_LONGITUDE, e.longitude)
        values.put(DbReferences.COLUMN_NAME_ENTRY_LOCATION, e.entryLocation)
        values.put(DbReferences.COLUMN_NAME_ENTRY_DATE, e.entryDate)
        values.put(DbReferences.COLUMN_NAME_ENTRY_NOTES, e.entryNotes)
        values.put(DbReferences.COLUMN_NAME_IMAGE_URI, e.imageUri)

        val id = database.insert(DbReferences.TABLE_NAME, null, values)

        return id
    }

    fun editEntry(e: Entry){
        val database = this.writableDatabase

        val values = ContentValues()
        values.put(DbReferences.COLUMN_NAME_LATITUDE, e.latitude)
        values.put(DbReferences.COLUMN_NAME_LONGITUDE, e.longitude)
        values.put(DbReferences.COLUMN_NAME_ENTRY_LOCATION, e.entryLocation)
        values.put(DbReferences.COLUMN_NAME_ENTRY_DATE, e.entryDate)
        values.put(DbReferences.COLUMN_NAME_ENTRY_NOTES, e.entryNotes)
        values.put(DbReferences.COLUMN_NAME_IMAGE_URI, e.imageUri)
        values.put(DbReferences._ID,e.id)

        database.update(DbReferences.TABLE_NAME,
            values,
            DbReferences._ID + "=?",
            arrayOf(e.id.toString())
        )

    }

    fun deleteEntry(entry: Entry){
        val database = this.writableDatabase

        database.delete(DbReferences.TABLE_NAME, DbReferences._ID + "=?", arrayOf(entry.id.toString()))

    }



    private object DbReferences {
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "pinsy_database.db"

        const val TABLE_NAME = "entries"
        const val _ID = "id"
        const val COLUMN_NAME_LATITUDE = "latitude"
        const val COLUMN_NAME_LONGITUDE = "longitude"
        const val COLUMN_NAME_ENTRY_LOCATION = "entry_location"
        const val COLUMN_NAME_ENTRY_DATE = "entry_date"
        const val COLUMN_NAME_ENTRY_NOTES = "entry_notes"
        const val COLUMN_NAME_IMAGE_URI = "image_uri"

        const val CREATE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_LATITUDE + " TEXT, " +
                    COLUMN_NAME_LONGITUDE + " TEXT, " +
                    COLUMN_NAME_ENTRY_LOCATION + " TEXT, " +
                    COLUMN_NAME_ENTRY_DATE + " TEXT, " +
                    COLUMN_NAME_ENTRY_NOTES + " TEXT, " +
                    COLUMN_NAME_IMAGE_URI + " TEXT)"

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}