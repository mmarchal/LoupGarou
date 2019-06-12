package projet.maxime.loupgarouandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.json.JSONArray
import kotlinx.android.synthetic.main.activity_attribution.*

class Attribution : AppCompatActivity() {

    private lateinit var bdd : CartesDB
    private var jsonArray = JSONArray()

    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null
    var secondaryHandler: Handler? = Handler()
    var primaryProgressStatus = 0
    var secondaryProgressStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribution)

        bdd = CartesDB(CartesDbHelper(applicationContext))

        this.initCompo()
        jsonArray = JSONArray(intent.getStringExtra("listeNoms"))

        btnProgressBarSecondary.setOnClickListener {
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

                    startSecondaryProgress()
                    secondaryProgressStatus = 0

                    secondaryHandler?.post {
                        progressBarSecondary.progress = primaryProgressStatus
                        textViewPrimary.text = "Complete $primaryProgressStatus% of 100"

                        if (primaryProgressStatus == 100) {
                            textViewPrimary.text = "All tasks completed"
                        }
                    }
                }
            }).start()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startSecondaryProgress() {
        Thread(Runnable {
            while (secondaryProgressStatus < 100) {
                secondaryProgressStatus += 1

                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                secondaryHandler?.post {
                    progressBarSecondary.secondaryProgress = secondaryProgressStatus
                    textViewSecondary.text = "Current task progress\n$secondaryProgressStatus% of 100"

                    if (secondaryProgressStatus == 100) {
                        textViewSecondary.text = "Single task complete."
                    }
                }
            }
        }).start()
    }

    private fun initCompo() {
        val police = Police()

    }
}
