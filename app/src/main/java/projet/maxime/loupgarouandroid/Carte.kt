package projet.maxime.loupgarouandroid

import org.json.JSONObject

class Carte {

    var id : Int? = 0
    var nomCarte : String? = null
    var imageCarte : String? = null
    var premiereNuit : Boolean? = null
    var positionPremiereNuit : Int = 0
    var nuitSuivante : Boolean? = null
    var positionNuitSuivante : Int = 0

    constructor(
        id: Int?,
        nomCarte: String?,
        imageCarte: String?,
        premiereNuit: Boolean?,
        positionPremiereNuit: Int,
        nuitSuivante: Boolean?,
        positionNuitSuivante: Int
    ) {
        this.id = id
        this.nomCarte = nomCarte
        this.imageCarte = imageCarte
        this.premiereNuit = premiereNuit
        this.positionPremiereNuit = positionPremiereNuit
        this.nuitSuivante = nuitSuivante
        this.positionNuitSuivante = positionNuitSuivante
    }

}