package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import org.json.JSONArray
import kotlinx.android.synthetic.main.activity_attribution.*
import org.json.JSONObject
import java.lang.Exception

class Attribution : AppCompatActivity() {

    private var listeCartes = arrayListOf<Jeu>()
    private var listeTrierPremier : MutableList<Pair<String?, Int>> = mutableListOf()
    private var listeTrierAutre : MutableList<Pair<String?, Int>> = mutableListOf()

    var secondaryHandler: Handler? = Handler()
    var primaryProgressStatus = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        this.initCompo()
        this.loading(JSONArray(intent.getStringExtra("listeNoms")))

    }

    @SuppressLint("SetTextI18n")
    private fun loading(jsonArray: JSONArray) {

        primaryProgressStatus = 0
        Thread(Runnable {
            while (primaryProgressStatus < 100) {

                val listeCarteVide = listeCartes.isEmpty()
                val listeTrierVidePremier = listeTrierPremier.isEmpty()
                val listeTrierVideAutres = listeTrierAutre.isEmpty()

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if(listeCarteVide) listeCartes = this.jsonToJeu(jsonArray)
                if(!listeCarteVide && listeTrierVidePremier) listeTrierPremier = this.triOrdrePremiereNuit(listeCartes)
                if(!listeCarteVide && !listeTrierVidePremier && listeTrierVideAutres) listeTrierAutre = this.triOrdreAutresNuit(listeCartes)

                secondaryHandler?.post {
                    progressBarSecondary.progress = primaryProgressStatus
                    textViewPrimary.text = "$primaryProgressStatus%"

                    if (primaryProgressStatus == 100) {
                        progressBar.visibility = View.INVISIBLE
                        textViewPrimary.text = "Tâches effectués !"
                        
                    }
                }
            }
        }).start()
    }

    private fun jsonToJeu(jsonArray: JSONArray): ArrayList<Jeu> {
        val liste :ArrayList<Jeu> = arrayListOf()
        val longueur = jsonArray.length() -1
        try {
            for(i in 0..longueur) {
                val json = jsonArray[i] as JSONObject
                val reveilPremiereNuit = json.getInt("nuit1") != 0
                val posPremiereNuit = json.getInt("posNuit1")
                val reveilAutresNuits = json.getInt("autresNuits") != 0
                val posAutresNuits = json.getInt("posAutresNuits")
                val mapPremiere = mapOf(reveilPremiereNuit to posPremiereNuit)
                val mapAutres = mapOf(reveilAutresNuits to posAutresNuits)
                val jeu = Jeu(json.getString("nom"), json.getString("role"), mapPremiere, mapAutres)
                liste.add(jeu)
            }
        } catch (e : Exception) {
            Log.e("ErreurAttribution", e.message)
        } finally {
            primaryProgressStatus+=33
            return liste
        }
    }

    private fun triOrdrePremiereNuit(list: ArrayList<Jeu>): MutableList<Pair<String?, Int>> {

        val map: MutableList<Pair<String?, Int>> = mutableListOf()

        try {
            for(i in 0..list.size) {
                for (data in list[i].premiereNuit) {
                    if(data.key) map.add(("${list[i].nomJoueur}/${list[i].carteJoueur}" to data.value))
                }
            }

        } catch (e : Exception) {
            Log.e("VerifErreurAttribution", e.message)
        } finally {
            map.sortBy{ it.second }
            primaryProgressStatus+=33
            return map
        }

    }

    private fun triOrdreAutresNuit(list : ArrayList<Jeu>) : MutableList<Pair<String?, Int>> {
        val map: MutableList<Pair<String?, Int>> = mutableListOf()

        try {
            for(i in 0..list.size) {
                for (data in list[i].nuitSuivante) {
                    if(data.key) map.add(("${list[i].nomJoueur}/${list[i].carteJoueur}" to data.value))
                }
            }

        } catch (e : Exception) {
            Log.e("VerifErreurAttribution", e.message)
        } finally {
            map.sortBy{ it.second }
            primaryProgressStatus+=34
            return map
        }
    }

    private fun initCompo() {
        val police = Police()
        police.initTV(applicationContext, textViewPrimary, R.font.policelg)
        police.initTV(applicationContext, textViewSecondary, R.font.policelg)
    }

}
