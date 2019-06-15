package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import android.widget.TextView
import android.widget.Spinner
import org.jetbrains.anko.uiThread
import android.app.ProgressDialog
import android.provider.ContactsContract
import android.util.Log
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var textView_titre : TextView
    private lateinit var textView_bienvenue : TextView
    private lateinit var textView_nB : TextView
    private lateinit var linearL : LinearLayout
    private lateinit var okButton : Button
    private lateinit var bouton : Button
    private lateinit var nombrePicker : Spinner
    private var listEdit : ArrayList<EditText> = ArrayList()
    private var listSpinner : ArrayList<Spinner> = ArrayList()
    private lateinit var dataInLinear : LinearLayout
    private lateinit var textView : TextView
    private lateinit var edit : EditText
    private lateinit var textViewSpinner : TextView
    private lateinit var spinner : Spinner

    private var listCartes : ArrayList<String> = arrayListOf(/*"Cupidon", "Le Loup Garou Blanc",*/"L Ancien", "Le Chasseur", "Le Loup Garou",
                                                             /*"Le Renard",*/ "Le Salvateur","La Sorcière","Le Simple Villageois","La Voyante")
    lateinit var database : Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Database(applicationContext)

        this.initCompo()
        this.initDatabase(database)
        this.okButton.setOnClickListener { generateEdit() }
    }

    @SuppressLint("SetTextI18n")
    private fun generateEdit() {
        okButton.visibility = View.INVISIBLE
        nombrePicker.visibility = View.INVISIBLE
        textView_nB.visibility = View.INVISIBLE

        val cartesArray = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCartes)
        cartesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dataInLinear = LinearLayout(this)
        val nbJ = (nombrePicker.selectedItem as String).toInt()
        for(count in 0 until nbJ) {

            val couleur = Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))

            textView = TextView(this)
            textView.id = count
            textView.text = "Joueur n°${count+1}"
            textView.textSize = resources.getDimensionPixelSize(R.dimen.main_titre_size_bienvenue).toFloat() / resources.displayMetrics.density
            textView.setTextColor(Color.BLACK)
            textView.setTypeface(null, Typeface.BOLD)
            textView.setBackgroundColor(couleur)
            Police().initTV(applicationContext, textView, R.font.policelg)

            edit = EditText(this)
            edit.hint = "Ecrivez le prénom du joueur"
            edit.id = count
            edit.maxLines = 1
            edit.setTextColor(Color.BLACK)
            edit.setBackgroundColor(couleur)
            edit.setTypeface(null, Typeface.BOLD)

            textViewSpinner = TextView(this)
            textViewSpinner.id = count
            textViewSpinner.text = "Rôle : "
            textViewSpinner.textSize = resources.getDimensionPixelSize(R.dimen.main_titre_size_bienvenue).toFloat() / resources.displayMetrics.density
            textViewSpinner.setTextColor(Color.BLACK)
            textViewSpinner.setBackgroundColor(couleur)
            textViewSpinner.setTypeface(null, Typeface.BOLD)
            Police().initTV(applicationContext, textViewSpinner, R.font.policelg)

            spinner = Spinner(this)
            spinner.id = count
            spinner.adapter = cartesArray
            spinner.setBackgroundColor(couleur)
            cartesArray.notifyDataSetChanged()

            listEdit.add(edit)
            listSpinner.add(spinner)
            dataInLinear.orientation = LinearLayout.VERTICAL
            dataInLinear.gravity = Gravity.CENTER
            dataInLinear.addView(textView)
            dataInLinear.addView(edit)
            dataInLinear.addView(textViewSpinner)
            dataInLinear.addView(spinner)

            if (dataInLinear.parent != null) {
                (dataInLinear.parent as ViewGroup).removeView(dataInLinear) // <- fix
            }
            linearL.addView(dataInLinear)
        }
        bouton = Button(this)
        bouton.text = resources.getText(R.string.lancer_partie)
        bouton.setOnClickListener { buttonClicked(nbJ) }
        linearL.addView(bouton)
    }

    private fun initDatabase(database: Database) {
        database.deleteAll()
        database.saveCarte(Carte(null, "L Ancien", "https://maxping.alwaysdata.net/cartesLG/ancien.jpg", 0, 0))
        database.saveCarte(Carte(null, "Le Chasseur", "https://maxping.alwaysdata.net/cartesLG/chasseur.jpg", 0, 0))
        //database.saveCarte(Carte(null, "Cupidon", "https://maxping.alwaysdata.net/cartesLG/cupidon.jpg", 1, 3, 0, 0))
        //database.saveCarte(Carte(null, "Le Loup Garou Blanc", "https://maxping.alwaysdata.net/cartesLG/loup_blanc.jpg", 1, 15, 1, 9))
        database.saveCarte(Carte(null, "Le Loup Garou", "https://maxping.alwaysdata.net/cartesLG/loup_garou.jpg", 1, 15))
        //database.saveCarte(Carte(null, "Le Renard", "https://maxping.alwaysdata.net/cartesLG/renard.jpg", 1, 5, 1, 3))
        database.saveCarte(Carte(null, "Le Salvateur", "https://maxping.alwaysdata.net/cartesLG/salvateur.jpg", 1, 14))
        database.saveCarte(Carte(null, "La Sorcière", "https://maxping.alwaysdata.net/cartesLG/sorciere.jpg", 1, 19))
        database.saveCarte(Carte(null, "Le Simple Villageois", "https://maxping.alwaysdata.net/cartesLG/villageois.jpg", 0, 0))
        database.saveCarte(Carte(null, "La Voyante", "https://maxping.alwaysdata.net/cartesLG/voyante.jpg", 1, 4))
    }

    private fun buttonClicked(nbJ: Int) {
        val dialog = ProgressDialog.show(
            this@MainActivity, "Loup Garou",
            "Récupération des données en cours ...", true
        )
        val jsonA = JSONArray()
        try {
            for(i in 0..nbJ) {
                val json = JSONObject()
                val info = database.getCartesByName(listSpinner[i].selectedItem.toString())
                info.forEach{ carte ->
                    json.put("nom", listEdit[i].text.toString())
                    json.put("role", listSpinner[i].selectedItem.toString())
                    json.put("id", carte.id)
                    json.put("image", carte.imageCarte)
                    json.put("nuit", carte.nuit)
                    json.put("posNuit", carte.positionNuit)
                    jsonA.put(json)
                    if(i+1==listEdit.size) changeActivity(jsonA)
                }
            }
        } catch (e: Exception) {
            Log.e("ErreurMain", e.message)
        }
    }

    private fun changeActivity(jsonA: JSONArray) {
        Log.d("VerifMain", jsonA.toString())
        val intent = Intent(this@MainActivity, Attribution::class.java)
        intent.putExtra("listeNoms", jsonA.toString())
        startActivity(intent)
    }

    private fun initCompo() {

        val police = Police()

        textView_titre = findViewById(R.id.titre)
        textView_bienvenue = findViewById(R.id.bienvenue)
        linearL = findViewById(R.id.linear)
        textView_nB = findViewById(R.id.main_nombreJoueurs)
        nombrePicker = findViewById(R.id.spinner)
        okButton = findViewById(R.id.main_generateEdit)

        police.initTV(applicationContext, textView_titre, R.font.policelg)
        police.initTV(applicationContext, textView_bienvenue, R.font.policelg)
        police.initTV(applicationContext, textView_nB, R.font.policelg)

        val nombresAdapter = ArrayAdapter.createFromResource(this, R.array.choixNombre, android.R.layout.simple_list_item_1)
        nombresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nombrePicker.adapter = nombresAdapter
    }
}
