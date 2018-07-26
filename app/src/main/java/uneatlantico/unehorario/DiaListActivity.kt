package uneatlantico.unehorario

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import uneatlantico.unehorario.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_dia_list.*
import kotlinx.android.synthetic.main.dia_list_content.view.*
import kotlinx.android.synthetic.main.dia_list.*
import objetos.Eventos
import org.jetbrains.anko.contentView

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [DiaDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class DiaListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var ITEMS=Tool.ITEMS
    var cont:Context=this
    lateinit var nada: SqliteHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("items",ITEMS.toString())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dia_list)

        setSupportActionBar(toolbar)
        toolbar.title = title
        cont=applicationContext
        nada=SqliteHandler(cont)
        Log.d("contexto",cont.toString())
        fab.setOnClickListener { view ->
            var a:Intent= Intent()
            a= Intent(this,InsertarEvento::class.java)
            startActivity(a)
        }

        if (dia_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(dia_list)
       // recreate()
    }




    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, ITEMS, twoPane)
       // recreate()
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: DiaListActivity,
                                        private val values: List<objetos.Eventos>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Eventos
                if (twoPane) {
                    val fragment = DiaDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(DiaDetailFragment.ARG_ITEM_ID, item.id.toString())
                            Log.d("elid2",item.id.toString())
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.dia_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, DiaDetailActivity::class.java).apply {
                        putExtra(DiaDetailFragment.ARG_ITEM_ID, item.id.toString())
                    }
                    Log.d("elod2",DiaDetailFragment.ARG_ITEM_ID)
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.dia_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id.toString()
            holder.contentView.text = item.nombre+" ("+ item.fecha_inicio+" - "+item.fecha_final+")"

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
