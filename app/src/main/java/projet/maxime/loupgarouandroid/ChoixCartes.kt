package projet.maxime.loupgarouandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.util.Log
import android.widget.*
import org.jetbrains.anko.doAsync
import java.lang.Exception

class ChoixCartes : AppCompatActivity() {

    private lateinit var textView_titre : TextView
    private lateinit var buttonSend : Button
    private lateinit var listView : ListView

    private val displayMetrics = DisplayMetrics()
    private lateinit var custom : CartesListViewAdapter
    private var list: ArrayList<Carte> = ArrayList()
    private var checkList : ArrayList<String> = ArrayList()
    private var listToRoles : ArrayList<Int> = ArrayList()
    private lateinit var bddCarte : CartesDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_cartes)

        val courseDb by lazy { CartesDB(CartesDbHelper(applicationContext)) }
        bddCarte = courseDb
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        this.initCompo()

        doAsync {
            list = courseDb.requestCartes() as ArrayList<Carte>
            listView = findViewById(R.id.liste)
            custom = CartesListViewAdapter(context = applicationContext, carte = list, width = displayMetrics.widthPixels)
            listView.adapter = custom
        }

        buttonSend.setOnClickListener { checkAllRadios() }
    }

    private fun checkAllRadios() {
        if(custom.selectedAnswers?.size==null) {
            Log.e("checkAllRadios()", "Erreur -> Taille du tableau null")
        } else {
            val count = listView.childCount
            try {
                 for(i in  0..count) {
                     val linear = listView.getChildAt(i).findViewById<LinearLayout>(R.id.linear)
                     if(linear.getChildAt(2).findViewById<Switch>(R.id.simpleSwitch).isChecked) {
                         val data =linear.getChildAt(1).findViewById<TextView>(R.id.listView_text).text.toString()
                         val switch = linear.getChildAt(2).findViewById<Switch>(R.id.simpleSwitch).isChecked
                         val editValue = linear.getChildAt(3).findViewById<EditText>(R.id.editNb).text.toString()
                         checkList.add("$editValue x $data : ")
                         listToRoles.add(i+1)
                     }
                 }
            } catch (e: Exception) {
                Log.e("ErreurCheckAllRadios", e.message)
            } finally {
                this.showDialogCreate(checkList)
            }
        }
    }

    private fun showDialogCreate(checkList: ArrayList<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Attribution des rôles")
        try {
            builder.setItems(checkList.toTypedArray()) { _, _ ->}
             builder.setNegativeButton("Non, je refais !") { distance, which ->
                 checkList.clear()
                 listToRoles.clear()
             }
            builder.setPositiveButton("Oui, on passe à la suite !") { distance, which ->
                if(listToRoles.isNotEmpty()) {
                    val intentC = Intent(this@ChoixCartes, Attribution::class.java)
                    intentC.putIntegerArrayListExtra("listRoles", listToRoles)
                    intentC.putStringArrayListExtra("listeNoms", intent.getStringArrayListExtra("listeNoms"))
                    startActivity(intentC)
                } else {
                    //dialogListeVide()
                }
            }
            val dialog = builder.create()
            dialog.show()
        } catch (e : Exception) {
            Log.e("ErreurChoix", e.message)
        }
    }

    private fun initCompo() {
        val police = Police()
        textView_titre = findViewById(R.id.tV_choix)
        buttonSend = findViewById(R.id.envoieRoles)
        police.initTV(applicationContext, textView_titre, R.font.policelg)
        police.initButton(applicationContext, buttonSend, R.font.policelg)
    }
}
