package projet.maxime.loupgarouandroid

class Jeu(nom: String, carte: String, pN: Map<Boolean, Int>, nS: Map<Boolean, Int>) {
    var nomJoueur : String? = nom
    var carteJoueur : String? = carte
    var premiereNuit : Map<Boolean, Int> = pN
    var nuitSuivante : Map<Boolean, Int> = nS

}
