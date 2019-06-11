package projet.maxime.loupgarouandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

class Attribution : AppCompatActivity() {

    private lateinit var bdd : CartesDB
    private var jsonArray = JSONArray()

    private lateinit var linear : LinearLayout
    private lateinit var textV_titre : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        linear = findViewById(R.id.linearLayoutAttribution)
        textV_titre = findViewById(R.id.attributionTitre)
        bdd = CartesDB(CartesDbHelper(applicationContext))

        this.initCompo()

        val jsonArrayM = JSONArray(intent.getStringExtra("listeNoms"))
        for(i in 0 until jsonArrayM.length()) {
            val json = jsonArrayM.getJSONObject(i)
            doAsync {
                val carteByName = bdd.getDatasByName(json.get("role").toString())
                uiThread {
                    json.put("id", carteByName[0].id)
                    json.put("image", carteByName[0].imageCarte)
                    json.put("nuit1", carteByName[0].premiereNuit)
                    json.put("posNuit1", carteByName[0].positionPremiereNuit)
                    json.put("autresNuits", carteByName[0].nuitSuivante)
                    json.put("posAutresNuits", carteByName[0].positionNuitSuivante)
                }
            }
            jsonArray.put(json)
        }
        Log.d("Verif", jsonArray.toString())
    }

    private fun generateJson(json: JSONObject): JSONObject {
        doAsync {
            val carteByName = bdd.getDatasByName(json.get("role").toString())
            uiThread {
                json.put("id", carteByName[0].id)
                json.put("image", carteByName[0].imageCarte)
                json.put("nuit1", carteByName[0].premiereNuit)
                json.put("posNuit1", carteByName[0].positionPremiereNuit)
                json.put("autresNuits", carteByName[0].nuitSuivante)
                json.put("posAutresNuits", carteByName[0].positionNuitSuivante)
            }
        }
        return json
    }

    private fun initCompo() {
        val police = Police()

        police.initTV(applicationContext, textV_titre, R.font.policelg)
    }
}
