package projet.maxime.loupgarouandroid

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class CartesDbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "course.db"
        val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(DBContract.UserEntry.TABLE_NAME, true,
            DBContract.UserEntry.COLUMN_ID to INTEGER + PRIMARY_KEY,
            DBContract.UserEntry.COLUMN_NAME to TEXT,
            DBContract.UserEntry.COLUMN_IMAGE to TEXT,
            DBContract.UserEntry.COLUMN_PREMIERE_NUIT to INTEGER, // 0->false / 1->true
            DBContract.UserEntry.COLUMN_POS_PREMIERE_NUIT to INTEGER,
            DBContract.UserEntry.COLUMN_NUIT_SUIVANTE to INTEGER, // 0->false / 1->true
            DBContract.UserEntry.COLUMN_POS_NUIT_SUIVANTE to INTEGER

        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(DBContract.UserEntry.TABLE_NAME, true)
        onCreate(db)
    }

}