package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class Attribution : AppCompatActivity() {

    private lateinit var bdd : CartesDB
    private lateinit var jsonO : JSONObject

    private lateinit var linear : LinearLayout
    private lateinit var textV_titre : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        linear = findViewById(R.id.linearLayoutAttribution)
        textV_titre = findViewById(R.id.attributionTitre)
        bdd = CartesDB(CartesDbHelper(applicationContext))

        this.initCompo()

        Log.d("Verif", intent.getStringExtra("listeNoms"))

    }

    private fun generateJson(nomChoisi: String, roleChoisi: String) {
        jsonO = JSONObject()
        doAsync {
            val carteByName = bdd.getDatasByName(roleChoisi)
            uiThread {
                jsonO.put("id", carteByName[0].id)
                jsonO.put("image", carteByName[0].imageCarte)
                jsonO.put("nuit1", carteByName[0].premiereNuit)
                jsonO.put("posNuit1", carteByName[0].positionPremiereNuit)
                jsonO.put("autresNuits", carteByName[0].nuitSuivante)
                jsonO.put("posAutresNuits", carteByName[0].positionNuitSuivante)
                Log.d("VerifJson", jsonO.toString())
            }
        }
    }

    private fun initCompo() {
        val police = Police()

        police.initTV(applicationContext, textV_titre, R.font.policelg)
    }
}
