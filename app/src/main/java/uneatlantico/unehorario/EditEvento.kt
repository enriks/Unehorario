package uneatlantico.unehorario

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.content.ContentValues
import android.content.Intent
import android.database.SQLException
import android.support.annotation.RequiresApi
import android.text.Editable
import android.util.Log
import android.widget.RadioGroup

import kotlinx.android.synthetic.main.activity_edit_evento.*
import kotlinx.android.synthetic.main.activity_insertar_evento.*
import org.jetbrains.anko.radioGroup

/**
 * A login screen that offers login via email/password.
 */
class EditEvento : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

val handler:SqliteHandler= SqliteHandler(this)
    val tols:Tool= Tool()
    var Intento: Intent = Intent()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_evento)
        val spinnere= edit_tipo_eventoss
        // Set up the login form.
        Intento= Intent(this,DiaListActivity::class.java)
        val aa = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,handler.getTipoEvento())
        spinnere.adapter = aa
        val inte=intent.extras.get("ITEM_ID")
        // Set up the login form.
        Log.d("itemid",inte.toString())
        val evnt=handler.selectoneEvento(inte.toString().toInt())
        if(evnt.repeticion.equals(0)){
            edit_repeticion_no.isChecked=true
            edit_dias.isEnabled=false
        }else{
            edit_repeticion_si.isChecked=true
            edit_dias.isEnabled=true
        }
        edit_nombre.text= Editable.Factory.getInstance().newEditable(evnt!!.nombre)
        edit_duracion.text=Editable.Factory.getInstance().newEditable(evnt.duracion.toString())
        var fecha=tols.changeFecha2(evnt.fecha_inicio)
        edit_fecha_inicio.updateDate(fecha.year,fecha.month.value-1,fecha.dayOfMonth)
        fecha=tols.changeFecha2(evnt.fecha_final)
        edit_fecha_final.updateDate(fecha.year,fecha.month.value-1,fecha.dayOfMonth)
        edit_dias.text=Editable.Factory.getInstance().newEditable(evnt.dias)
        edit_detalle.text=Editable.Factory.getInstance().newEditable(evnt.detalle)
        var radios = findViewById(R.id.edit_RadioGroup) as RadioGroup
        radios.setOnCheckedChangeListener { group, i -> edit_dias.isEnabled=edit_repeticion_si.isChecked }
        edit_btn_click_me.setOnClickListener { handler.updateEventos(obtenerValores(), inte.toString().toInt())
            Tool.ITEMS=handler.selectEvento()
            startActivity(Intento)}
    }
fun obtenerValores():ContentValues{
    val value:ContentValues= ContentValues()
    value.put("nombre",edit_nombre.text.toString())
    value.put("duracion",edit_duracion.text.toString())
    if (edit_fecha_inicio.month<10){
        if(edit_fecha_inicio.dayOfMonth<10) {

            value.put("fecha_inicio", "" + edit_fecha_inicio.year + "-0" + (edit_fecha_inicio.month+1) + "-0" + edit_fecha_inicio.dayOfMonth)
        }else{
            value.put("fecha_inicio", "" + edit_fecha_inicio.year + "-0" + (edit_fecha_inicio.month+1) + "-" + edit_fecha_inicio.dayOfMonth)
        }
    }else{
        value.put("fecha_inicio",""+edit_fecha_inicio.year+"-"+edit_fecha_inicio.month+"-"+edit_fecha_inicio.dayOfMonth)
        if(edit_fecha_inicio.dayOfMonth<10) {

            value.put("fecha_inicio", "" + edit_fecha_inicio.year + "-" + (edit_fecha_inicio.month+1) + "-0" + edit_fecha_inicio.dayOfMonth)
        }else{
            value.put("fecha_inicio", "" + edit_fecha_inicio.year + "-" + (edit_fecha_inicio.month+1) + "-" + edit_fecha_inicio.dayOfMonth)
        }
    }
    if (edit_fecha_final.month<10){
        if(edit_fecha_final.dayOfMonth<10) {

            value.put("fecha_final", "" + edit_fecha_final.year + "-0" + (edit_fecha_final.month+1) + "-0" + edit_fecha_final.dayOfMonth)
        }else{
            value.put("fecha_final", "" + edit_fecha_final.year + "-0" + (edit_fecha_final.month+1) + "-" + edit_fecha_final.dayOfMonth)
        }
    }else{
        value.put("fecha_final",""+edit_fecha_final.year+"-"+(edit_fecha_final.month+1)+"-"+edit_fecha_final.dayOfMonth)
        if(edit_fecha_final.dayOfMonth<10) {

            value.put("fecha_final", "" + edit_fecha_final.year + "-" + (edit_fecha_final.month+1) + "-0" + edit_fecha_final.dayOfMonth)
        }else{
            value.put("fecha_final", "" + edit_fecha_final.year + "-" + (edit_fecha_final.month+1) + "-" + edit_fecha_final.dayOfMonth)
        }
    }
    value.put("tipo_evento",edit_tipo_eventoss.selectedItemId)
    value.put("repeticion",verifyChecked())
    value.put("dias",edit_dias.text.toString())
    value.put("id_alumno",handler.getIDUsuario(Tool.USUARIO))
    value.put("id_profesor","nadie")
    value.put("detalle",edit_detalle.text.toString())

    return value
}


    fun verifyChecked():Int{
        var i=0
        if(edit_repeticion_si.isChecked)
            i=1
        return i
    }


}
