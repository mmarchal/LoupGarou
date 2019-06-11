package projet.maxime.loupgarouandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException



class Attribution : AppCompatActivity() {

    private lateinit var bdd : CartesDB
    private var jsonArray = JSONArray()

    private lateinit var textV_titre : TextView
    private lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        textV_titre = findViewById(R.id.attributionTitre)
        listView = findViewById(R.id.attributionListe)
        bdd = CartesDB(CartesDbHelper(applicationContext))

        this.initCompo()

        val jsonArrayM = JSONArray(intent.getStringExtra("listeNoms"))
        Log.d("Verif", jsonArrayM.toString())

    }

    private fun initCompo() {
        val police = Police()

        police.initTV(applicationContext, textV_titre, R.font.policelg)
    }
}
