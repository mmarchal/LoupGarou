package projet.maxime.loupgarouandroid

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.widget.Button
import android.widget.TextView


class Police {

    fun initTV(context: Context, textView: TextView, pathFont: Int) {

        val tf = ResourcesCompat.getFont(context, pathFont)
        textView.typeface = tf

    }

    fun initButton(context: Context, button: Button, pathFont: Int) {

        val tf = ResourcesCompat.getFont(context, pathFont)
        button.typeface = tf

    }

}
