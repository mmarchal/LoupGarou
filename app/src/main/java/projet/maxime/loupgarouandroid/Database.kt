package projet.maxime.loupgarouandroid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.d("Database", "CREER")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteAll(): Boolean {
        val db = writableDatabase
        db.execSQL("delete from "+ DBContract.UserEntry.TABLE_NAME)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun saveCarte(carte: Carte): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_ID, carte.id)
        values.put(DBContract.UserEntry.COLUMN_NAME, carte.nomCarte)
        values.put(DBContract.UserEntry.COLUMN_IMAGE, carte.imageCarte)
        values.put(DBContract.UserEntry.COLUMN_NUIT,  carte.nuit)
        values.put(DBContract.UserEntry.COLUMN_POS_NUIT, carte.positionNuit)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(userid: String): Boolean {
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.UserEntry.COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(userid)
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun getCartesByName(userName: String): ArrayList<Carte> {
        val users = ArrayList<Carte>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME + "='" + userName + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id : Int
        var name: String
        var image: String
        var firstNight : Int
        var posFirstNight : Int
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {

                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NAME))
                image = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_IMAGE))
                firstNight = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NUIT))
                posFirstNight = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POS_NUIT))

                users.add(Carte(id, name, image, firstNight, posFirstNight))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readAllUsers(): ArrayList<Carte> {
        val users = ArrayList<Carte>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var userid: Int
        var name: String
        var image: String
        var firstNight : Int
        var posFirstNight : Int

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                userid = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NAME))
                image = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_IMAGE))
                firstNight = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NUIT))
                posFirstNight = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POS_NUIT))
                users.add(Carte(userid, name, image, firstNight, posFirstNight))
                cursor.moveToNext()
            }
        }
        return users
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "CartesLoupGarou.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_NAME + " TEXT," +
                    DBContract.UserEntry.COLUMN_IMAGE + " TEXT, "+
                    DBContract.UserEntry.COLUMN_NUIT + " INTEGER, "+
                    DBContract.UserEntry.COLUMN_POS_NUIT + " INTEGER)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME
    }

}