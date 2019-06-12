package projet.maxime.loupgarouandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONArray

class Attribution : AppCompatActivity() {

    private lateinit var bdd : CartesDB
    private var jsonArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        bdd = CartesDB(CartesDbHelper(applicationContext))

        this.initCompo()
        jsonArray = JSONArray(intent.getStringExtra("listeNoms"))
        //https://www.androidly.net/327/android-progressbar-using-kotlin

    }

    private fun initCompo() {
        val police = Police()

    }
}
