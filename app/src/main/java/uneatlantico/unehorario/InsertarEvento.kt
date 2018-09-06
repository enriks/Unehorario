package uneatlantico.unehorario

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.activity_edit_evento.*

import kotlinx.android.synthetic.main.activity_insertar_evento.*
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.sp
import org.jetbrains.anko.spinner
import kotlin.coroutines.experimental.CoroutineContext

/**
 * A login screen that offers login via email/password.
 */
class InsertarEvento : AppCompatActivity() {



    var handler: SqliteHandler = SqliteHandler(this)
    lateinit var btn_click_me:Button
    lateinit var intetno:Intent
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_evento)
        val spinnere= this.tipo_eventoss
        // Set up the login form.
        val aa = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,handler.getTipoEvento())
        spinnere.adapter = aa
        intetno = Intent(this,DiaListActivity::class.java)
        btn_click_me = findViewById(R.id.btn_click_me) as Button
        btn_click_me.setOnClickListener { estacosa(spinnere) }
        var radios = findViewById(R.id.RadioGroup) as RadioGroup
        radios.setOnCheckedChangeListener { group, i -> dias.isEnabled=repeticion_si.isChecked }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun estacosa(spinnere:Spinner){
        val value:ContentValues= ContentValues()
        value.put("nombre",nombre.text.toString())
        value.put("duracion",duracion.text.toString())
        val ass1=fecha_inicio.month + 1
        val ass2=fecha_final.month + 1
        if (ass1<10){
            if(fecha_inicio.dayOfMonth<10) {

                value.put("fecha_inicio", "" + fecha_inicio.year + "-0" + (ass1) + "-0" + fecha_inicio.dayOfMonth)
            }else{
                value.put("fecha_inicio", "" + fecha_inicio.year + "-" + (ass1) + "-" + fecha_inicio.dayOfMonth)
            }
        }else{
            value.put("fecha_inicio",""+fecha_inicio.year+"-"+ass1+"-"+fecha_inicio.dayOfMonth)
            if(fecha_inicio.dayOfMonth<10) {

                value.put("fecha_inicio", "" + fecha_inicio.year + "-" + (ass1) + "-0" + fecha_inicio.dayOfMonth)
            }else{
                value.put("fecha_inicio", "" + fecha_inicio.year + "-" + (ass1) + "-" + fecha_inicio.dayOfMonth)
            }
        }
        if (ass2 < 10){
            if(fecha_final.dayOfMonth<10) {

                value.put("fecha_final", "" + fecha_final.year + "-0" + (ass2) + "-0" + fecha_final.dayOfMonth)
            }else{
                value.put("fecha_final", "" + fecha_final.year + "-" + (ass2) + "-" + fecha_final.dayOfMonth)
            }
        }else{
            value.put("fecha_final",""+fecha_final.year+"-"+(ass2)+"-"+fecha_final.dayOfMonth)
            if(fecha_final.dayOfMonth<10) {

                value.put("fecha_final", "" + fecha_final.year + "-" + (ass2) + "-0" + fecha_final.dayOfMonth)
            }else{
                value.put("fecha_final", "" + fecha_final.year + "-" + (ass2) + "-" + fecha_final.dayOfMonth)
            }
        }
        value.put("tipo_evento",spinnere.selectedItemId)
        value.put("repeticion",verifyChecked())
        value.put("dias",dias.text.toString())
        value.put("id_alumno",handler.getIDUsuario(Tool.USUARIO))
        value.put("id_profesor","nadie")
        value.put("detalle",detalle.text.toString())
        handler.insert("evento",value)
        Tool.ITEMS=handler.selectEvento()
        startActivity(intetno)

    }
fun verifyChecked():Int{
    var i=0
    if(repeticion_si.isChecked)
        i=1
    return i
}




}



