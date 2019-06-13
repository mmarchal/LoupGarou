package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import org.json.JSONArray
import kotlinx.android.synthetic.main.activity_attribution.*
import java.lang.Exception

class Attribution : AppCompatActivity() {

    private var jsonArray = JSONArray()

    private var listeActions = arrayListOf<String>("")

    var secondaryHandler: Handler? = Handler()
    var primaryProgressStatus = 0
    var secondaryProgressStatus = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        this.initCompo()
        jsonArray = JSONArray(intent.getStringExtra("listeNoms"))
        this.jsonToJeu(jsonArray)
        this.loading()

    }

    private fun jsonToJeu(jsonArray: JSONArray) {
        try {
            for(i in 0..jsonArray.length()) {
                Log.d("VerifAttribution", jsonArray[i].toString())
            }
        } catch (e : Exception) {
            Log.e("ErreurAttribution", e.message)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loading() {
        primaryProgressStatus = 0
        secondaryProgressStatus = 0

        Thread(Runnable {
            while (primaryProgressStatus < 100) {
                primaryProgressStatus += 1

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                secondaryProgressStatus = 0

                secondaryHandler?.post {
                    progressBarSecondary.progress = primaryProgressStatus
                    textViewPrimary.text = "$primaryProgressStatus%"

                    if (primaryProgressStatus == 100) {
                        textViewPrimary.text = "Tâches effectués !"
                    }
                }
            }
        }).start()
    }

    private fun initCompo() {
        val police = Police()
        police.initTV(applicationContext, textViewPrimary, R.font.policelg)
        police.initTV(applicationContext, textViewSecondary, R.font.policelg)
    }
}
