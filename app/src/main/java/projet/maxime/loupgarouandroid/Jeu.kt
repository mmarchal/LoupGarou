package projet.maxime.loupgarouandroid

class Jeu(nom: String, carte: String, pN: Map<String, Boolean>, nS: Map<String, Boolean>) {
    var nomJoueur : String? = nom
    var carteJoueur : String? = carte
    var premiereNuit : Map<String, Boolean>? = pN
    var nuitSuivante : Map<String, Boolean>? = nS

}
