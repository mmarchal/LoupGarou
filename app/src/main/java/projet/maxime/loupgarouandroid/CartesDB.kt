package projet.maxime.loupgarouandroid

import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class CartesDB(private val dbHelper: CartesDbHelper) {

    fun requestCartes() = dbHelper.use {
        select(DBContract.UserEntry.TABLE_NAME).parseList(classParser<Carte>())
    }

    fun getCartesById(id : Int) = dbHelper.use {
        select(DBContract.UserEntry.TABLE_NAME)
            .whereArgs("_id = {idName}",
                "idName" to id).parseList(classParser<Carte>())
    }

    fun getDatasByName(nom : String) = dbHelper.use {
        select(DBContract.UserEntry.TABLE_NAME)
            .whereArgs("nomCarte = {valueName}",
                "valueName" to nom).parseList(classParser<Carte>())
    }

    fun saveCarte(carte: Carte) = dbHelper.use {
        insert(DBContract.UserEntry.TABLE_NAME,
            DBContract.UserEntry.COLUMN_ID to carte.id,
            DBContract.UserEntry.COLUMN_NAME to carte.nomCarte,
            DBContract.UserEntry.COLUMN_IMAGE to carte.imageCarte,
            DBContract.UserEntry.COLUMN_PREMIERE_NUIT to carte.premiereNuit,
            DBContract.UserEntry.COLUMN_POS_PREMIERE_NUIT to carte.positionPremiereNuit,
            DBContract.UserEntry.COLUMN_NUIT_SUIVANTE to carte.nuitSuivante,
            DBContract.UserEntry.COLUMN_POS_NUIT_SUIVANTE to carte.positionNuitSuivante)
    }

    fun saveCartes(courseList: List<Carte>) {
        for (c in courseList)
            saveCarte(c)
    }

    fun deleteAll() = dbHelper.use {
        delete(DBContract.UserEntry.TABLE_NAME)
    }

}