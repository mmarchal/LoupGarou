package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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

    private var listCartes : ArrayList<String> = arrayListOf("L Ancien", "Le Chasseur", "Cupidon", "Le Loup Garou Blanc","Le Loup Garou",
                                                             "Le Renard", "Le Salvateur","La Sorcière","Le Simple Villageois","La Voyante")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initCompo()
        this.initBDD()
        this.okButton.setOnClickListener { generateEdit() }
    }

    @SuppressLint("SetTextI18n")
    private fun generateEdit() {
        okButton.visibility = View.INVISIBLE
        nombrePicker.visibility = View.INVISIBLE
        textView_nB.visibility = View.INVISIBLE

        val cartesArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCartes)
        cartesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dataInLinear = LinearLayout(this)
        val nbJ = (nombrePicker.selectedItem as String).toInt()
        for(count in 0 until nbJ) {

            val couleur = Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))

            textView = TextView(this)
            textView.id = count
            textView.text = "Joueur n°${count+1}"
            textView.textSize = resources.getDimensionPixelSize(R.dimen.main_titre_size_bienvenue).toFloat() / resources.displayMetrics.density
            textView.setTextColor(couleur)

            edit = EditText(this)
            edit.hint = "Prénom"
            edit.id = count
            edit.maxLines = 1

            textViewSpinner = TextView(this)
            textViewSpinner.id = count
            textViewSpinner.text = "Rôle : "
            textViewSpinner.textSize = resources.getDimensionPixelSize(R.dimen.main_titre_size_bienvenue).toFloat() / resources.displayMetrics.density
            textViewSpinner.setTextColor(couleur)

            spinner = Spinner(this)
            spinner.id = count
            spinner.adapter = cartesArrayAdapter
            cartesArrayAdapter.notifyDataSetChanged()

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
        bouton.setOnClickListener { buttonClicked() }
        linearL.addView(bouton)
    }

    private fun initBDD() {
        val carteDb by lazy { CartesDB(CartesDbHelper(applicationContext)) }
        carteDb.deleteAll()
        if (carteDb.requestCartes().isEmpty()) {
            doAsync {
                carteDb.saveCarte(Carte(null, "L Ancien", "https://maxping.alwaysdata.net/cartesLG/ancien.jpg", false, 0, false, 0))
                //carteDb.saveCarte(Carte(null, "Le Juge Begue", "https://maxping.alwaysdata.net/cartesLG/begue.jpg", true, 7, false, 0))
                carteDb.saveCarte(Carte(null, "Le Chasseur", "https://maxping.alwaysdata.net/cartesLG/chasseur.jpg", false, 0, false, 0))
                //carteDb.saveCarte(Carte(null, "Le Chien Loup", "https://maxping.alwaysdata.net/cartesLG/chien_loup.jpg", true, 15, true, 7))
                carteDb.saveCarte(Carte(null, "Cupidon", "https://maxping.alwaysdata.net/cartesLG/cupidon.jpg", true, 3, false, 0))
                carteDb.saveCarte(Carte(null, "Le Loup Garou Blanc", "https://maxping.alwaysdata.net/cartesLG/loup_blanc.jpg", true, 15, true, 9))
                carteDb.saveCarte(Carte(null, "Le Loup Garou", "https://maxping.alwaysdata.net/cartesLG/loup_garou.jpg", true, 15, true, 7))
                carteDb.saveCarte(Carte(null, "Le Renard", "https://maxping.alwaysdata.net/cartesLG/renard.jpg", true, 5, true, 3))
                carteDb.saveCarte(Carte(null, "Le Salvateur", "https://maxping.alwaysdata.net/cartesLG/salvateur.jpg", true, 14, true, 6))
                //carteDb.saveCarte(Carte(null, "La Servante Dévouée", "https://maxping.alwaysdata.net/cartesLG/servante.jpg", false, 0, false, 0))
                carteDb.saveCarte(Carte(null, "La Sorcière", "https://maxping.alwaysdata.net/cartesLG/sorciere.jpg", true, 19, true, 12))
                carteDb.saveCarte(Carte(null, "Le Simple Villageois", "https://maxping.alwaysdata.net/cartesLG/villageois.jpg", false, 0, false, 0))
                carteDb.saveCarte(Carte(null, "La Voyante", "https://maxping.alwaysdata.net/cartesLG/voyante.jpg", true, 4, true, 2))
            }
        }
    }

    private fun buttonClicked() {
        val jsonA = JSONArray()
        for(i in 0 until listEdit.size) {
            val jsonO = JSONObject()
            jsonO.put("nom", listEdit[i].text.toString())
            jsonO.put("role", listSpinner[i].selectedItem.toString())
            jsonA.put(jsonO)
        }
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
