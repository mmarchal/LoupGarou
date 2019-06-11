package projet.maxime.loupgarouandroid

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

class CartesListViewAdapter(var context : Context, var carte: ArrayList<Carte>, var width : Int) : BaseAdapter(){

    var selectedAnswers: ArrayList<Boolean>? = ArrayList()
    private lateinit var viewH : ViewHolder

    private class ViewHolder(row: View?) {
        var imgLG : ImageView = row?.findViewById(R.id.listView_image) as ImageView
        var txtLG : TextView = row?.findViewById(R.id.listView_text) as TextView
        var switchValue : Switch = row?.findViewById(R.id.simpleSwitch) as Switch
    }

    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
        if(i==0&& selectedAnswers?.size!! <carte.size) {
            for (count in 0 until carte.size) {
                selectedAnswers?.add(false)
            }
        }

        val view : View?
        val viewHolder : ViewHolder
        if(p1 == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.custom_listview, p2, false)
            viewHolder = ViewHolder(view)
            viewH = viewHolder
            view.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }
        val carte : Carte = getItem(i) as Carte
        viewHolder.txtLG.text = carte.nomCarte
        Police().initTV(context, viewHolder.txtLG, R.font.policelg)
        Picasso.get().load(carte.imageCarte).resize((width/8), (width/8)).into(viewHolder.imgLG)

        viewHolder.switchValue.setOnCheckedChangeListener { _, isChecked ->
            selectedAnswers?.set(i, isChecked)
        }
        return view as View
    }

    override fun getItem(p0: Int): Any {
        return carte[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return carte.count()
    }
}