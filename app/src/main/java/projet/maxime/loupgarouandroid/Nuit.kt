package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_nuit.*
import java.lang.Exception

class Nuit : AppCompatActivity() {

    private var TAILLE = "Taille"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuit)

        Police().initTV(applicationContext, textViewNomCarte, R.font.policelg)

        val taille = SharedPreferencesUtils(applicationContext).getValueInt(TAILLE)

        try {
            for(i in 0..taille) {
                val data = SharedPreferencesUtils(applicationContext).getValueString(i.toString())?.split('>')
                Log.d("VerifGame", data?.toString())
                textViewNomCarte.text = data?.get(1)
                Picasso.get().load(data?.get(2)).into(imageViewCarte)

                val test = SharedPreferencesUtils(applicationContext).getValueString(i.toString())
                Log.d("VerifGameRecu", test)
            }
        } catch (e : Exception) {
            Log.e("ErreurGame", e.message)
        }

    }

}
