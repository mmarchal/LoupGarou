package projet.maxime.loupgarouandroid

import org.json.JSONArray
import org.json.JSONObject

class Carte {

    var id : Int? = 0
    var nomCarte : String? = null
    var imageCarte : String? = null
    var nuit : Int = 0
    var positionNuit : Int = 0

    constructor(
        id: Int?,
        nomCarte: String?,
        imageCarte: String?,
        nuit: Int,
        positionNuit: Int
    ) {
        this.id = id
        this.nomCarte = nomCarte
        this.imageCarte = imageCarte
        this.nuit = nuit
        this.positionNuit = positionNuit
    }

}