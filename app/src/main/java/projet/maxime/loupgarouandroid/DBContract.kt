package projet.maxime.loupgarouandroid

import android.provider.BaseColumns

object DBContract {

    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "carte"
            val COLUMN_ID = "_id"
            val COLUMN_NAME = "nomCarte"
            val COLUMN_IMAGE = "imageCarte"
            val COLUMN_NUIT = "nuit"
            val COLUMN_POS_NUIT = "positionNuit"
        }
    }
}