package uneatlantico.unehorario.dummy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.LayoutInflater
import kotlinx.coroutines.experimental.channels.Channel
import objetos.Eventos
import okhttp3.Call
import uneatlantico.unehorario.DiaListActivity
import uneatlantico.unehorario.MainActivity
import java.util.ArrayList
import java.util.HashMap
import uneatlantico.unehorario.SqliteHandler

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
class DummyContent(context: Context)  {
    /**
     * An array of sample (dummy) items.
     */

    val cancer = SqliteHandler(context!!).readableDatabase
    val ITEMS: List<objetos.Eventos> = selectEvento()
    fun selectEvento(): ArrayList<objetos.Eventos>{
        var evnt:ArrayList<Eventos> = ArrayList()
        val db: SQLiteDatabase = cancer
        val c = db.rawQuery("Select * from evento", null)
        Log.d("lamierda",c.toString())
        while (c.moveToNext()){
            Log.d("mecagoendios",evnt.toString())
            var id= c.getInt(c.getColumnIndex("_id"))
            Log.d("mecagoendioss",id.toString())
            //1var
            var a: Eventos =objetos.Eventos(c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("nombre")),c.getInt(c.getColumnIndex("duracion")),c.getString(c.getColumnIndex("fecha_inicio")),c.getString(c.getColumnIndex("fecha_final")),c.getInt(c.getColumnIndex("tipo_evento")),c.getInt(c.getColumnIndex("repeticion")),c.getString(c.getColumnIndex("dias")),c.getInt(c.getColumnIndex("id_alumno")),c.getString(c.getColumnIndex("id_profesor")),c.getString(c.getColumnIndex("descripcion")))
            Log.d("lol",a.toString())
            evnt.add(a)
        }

        return evnt
    }

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, objetos.Eventos> = HashMap()

    private val COUNT = ITEMS.count()

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(ITEMS.get(i))
        }
    }


    private fun addItem(item: objetos.Eventos) {
        ITEM_MAP.put(item.id.toString(), item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
