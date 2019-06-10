package projet.maxime.loupgarouandroid

import android.provider.BaseColumns

object DBContract {

    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "carte"
            val COLUMN_ID = "_id"
            val COLUMN_NAME = "nomCarte"
            val COLUMN_IMAGE = "imageCarte"
            val COLUMN_PREMIERE_NUIT = "premiereNuit"
            val COLUMN_POS_PREMIERE_NUIT = "positionPremiereNuit"
            val COLUMN_NUIT_SUIVANTE = "nuitSuivante"
            val COLUMN_POS_NUIT_SUIVANTE = "positionNuitSuivante"
        }
    }
}