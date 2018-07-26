package uneatlantico.unehorario

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uneatlantico.unehorario.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_dia_detail.*
import kotlinx.android.synthetic.main.dia_detail.view.*
import objetos.Eventos
import java.util.HashMap

/**
 * A fragment representing a single Dia detail screen.
 * This fragment is either contained in a [DiaListActivity]
 * in two-pane mode (on tablets) or a [DiaDetailActivity]
 * on handsets.
 */
class DiaDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: objetos.Eventos? = null
    var ITEMS: MutableMap<String, Eventos> = list_map()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                //Log.d("idDetalle",it.getString(ARG_ITEM_ID).toString())
                item = ITEMS[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.nombre
            }
        }
    }
    fun list_map():MutableMap<String,Eventos>{
        val map:  MutableMap<String, objetos.Eventos> = HashMap()
        Log.d("longitud",Tool.ITEMS.size.toString())
        for (i in 0..Tool.ITEMS.size-1)
        {
            map.put(Tool.ITEMS.get(i).id.toString(),Tool.ITEMS.get(i))
            Log.d("elmapa",map.toString())
        }
        return map
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dia_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.dia_detail.text = "Detalle de el evento:"+System.getProperty("line.separator")+it.detalle

        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
