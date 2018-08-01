package uneatlantico.unehorario

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView

import uneatlantico.unehorario.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_dia_list.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.dia_list_content.view.*
import kotlinx.android.synthetic.main.dia_list.*
import kotlinx.android.synthetic.main.nav_header_main2.*
import objetos.Eventos
import org.jetbrains.anko.contentView
import org.jetbrains.anko.image
import android.graphics.BitmapFactory
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.widget.ImageView
import org.jetbrains.anko.doAsync
import java.net.URL


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [DiaDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class DiaListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("click","esta aqui")
        when (item.itemId) {
            R.id.nav_add -> {
                // Handle the camera action
                Log.d("click","clico")
            }


            R.id.nav_sync->{
                Log.d("click","clico 2")
            }
            R.id.nav_manage -> {

                Log.d("click","clico 3")

            }
        }

       // drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var ITEMS=Tool.ITEMS
    var cont:Context=this
    lateinit var nada: SqliteHandler

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("items",ITEMS.toString())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dia_list)
        setSupportActionBar(toolbar)
        var algo=findViewById(R.id.nav_view) as NavigationView
        var nad2a =algo.getHeaderView(0)
        algo.setNavigationItemSelectedListener (this)
        var usuario:TextView= nad2a.findViewById(R.id.nombre_usuario)
        var correo:TextView= nad2a.findViewById(R.id.correo_usuario)
        var imagn:ImageView= nad2a.findViewById(R.id.imageView)

        doAsync {
            val newurl = URL(Tool.IMAGEN)

           imagn.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()))
        }

       usuario.setText(Tool.NOMBRE)
       correo.setText(Tool.CORREO)
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
        val toggle = ActionBarDrawerToggle(
                this, this.drawer_layout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupRecyclerView(dia_list)
       // recreate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
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
