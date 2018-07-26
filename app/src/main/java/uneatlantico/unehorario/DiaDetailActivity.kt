package uneatlantico.unehorario

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_dia_detail.*

/**
 * An activity representing a single Dia detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [DiaListActivity].
 */
class DiaDetailActivity : AppCompatActivity() {
val handler:SqliteHandler= SqliteHandler(this)
 var Intento:Intent= Intent()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dia_detail)
        setSupportActionBar(detail_toolbar)
        Intento=Intent(this,EditEvento::class.java).apply {
            putExtra("ITEM_ID", intent.getStringExtra(DiaDetailFragment.ARG_ITEM_ID))
        }
        eliminar.setOnClickListener { view ->
            Intento= Intent(this,DiaListActivity::class.java)
            handler.delete("evento","_id="+intent.getStringExtra(DiaDetailFragment.ARG_ITEM_ID))
            Tool.ITEMS=handler.selectEvento()
            startActivity(Intento)
        }
        editar.setOnClickListener { view ->
                startActivity(Intento)
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = DiaDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(DiaDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(DiaDetailFragment.ARG_ITEM_ID))
                    Log.d("elid",intent.getStringExtra(DiaDetailFragment.ARG_ITEM_ID))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.dia_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    navigateUpTo(Intent(this, DiaListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
    public fun getHandler(handler:SqliteHandler): SqliteHandler {
        return handler
    }
}
