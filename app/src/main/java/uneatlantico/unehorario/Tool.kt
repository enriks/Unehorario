package uneatlantico.unehorario

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.reflect.TypeToken
import objetos.Eventos
import org.json.JSONArray
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class Tool() {

    companion object {
        val DB_NAME = "unehorario.db"
        var USUARIO = ""
        val DB_VERSION = 1
        //val instance by lazy { SqliteHandler(App.instace)}
        var ITEMS = ArrayList<Eventos>()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun changeFecha(args: String):String {
        var string = "July 25, 2017"


        val date = LocalDate.parse(args, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        string= date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM"))
       return string
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    fun changeFecha2(args: String):LocalDate {
        var date=LocalDate.now()


         date = LocalDate.parse(args, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        return date
    }
    public fun syncDatabaseEventos(nombre:String, db: SqliteHandler):ContentValues{
        var values:ContentValues= ContentValues()


                var json:JSONObject= JSONObject()
                json.put("nombre","manuel.coto")
                //val algo = "["+json("GetEventos",json).substring(json("GetEventos",json).indexOf("[")+1,json("GetEventos",json).indexOf("]"))+"}"
                val algo = json("GetEventos",json)
                val gson = Gson()
                val x = JSONArray(algo)
                for(a in 0 until  x.length()){
                    Log.d("jsonEventos", x[a].toString())
                    val objetos = gson.fromJson(x[a].toString(), objetos.Eventos2()::class.java)
                    values.put("nombre", objetos.nombre)
                    Log.d("AsyncData", objetos.nombre)
                    values.put("nombre",objetos.nombre)
                    values.put("duracion",objetos.duracion)
                    values.put("fecha_inicio",objetos.fecha_inicio.toString())
                    values.put("fecha_final",objetos.fecha_final.toString())
                    values.put("tipo_evento",objetos.tipo_evento)
                    values.put("repeticion",objetos.repeticion)
                    values.put("dias",objetos.dias)
                    values.put("id_alumno",objetos.alumno)
                    values.put("id_profesor",objetos.maestro)
                    values.put("detalle",objetos.detalle)
                    Log.d("AsyncData",objetos.nombre)
                    db.insert("evento",values)
                    values= ContentValues()
                    Log.d("objetosEventos",objetos.toString())
                }
        //val jsonA=JSONObject(algo)
                var values2:ContentValues= ContentValues()
                values2.put("version", DB_VERSION)
                values2.put("jsonevento",algo)
                db.insert("cosas",values2)






        return values
    }
    public fun syncDatabaseTipoEvento(db:SqliteHandler):ContentValues{

        var values:ContentValues= ContentValues()

            var json: JSONObject = JSONObject()
            json.put("nombre", "manuel.coto")
            //val algo = "["+json("GetTipoEventos",json).substring(json("GetTipoEventos",json).indexOf("[")+1,json("GetTipoEventos",json).indexOf("]"))+"]]"
            val algo = json("GetTipoEventos", json)
            val x = JSONArray(algo)
            val gson = Gson()
            for(a in 0 until  x.length()){
                Log.d("jsonTipoEvento", x[a].toString())
                val objetos = gson.fromJson(x[a].toString(), objetos.TipoEvento()::class.java)
                values.put("nombre", objetos.nombre)
                Log.d("AsyncData", objetos.nombre)
                db.insert("tipo_evento",values)
                values= ContentValues()
            }

            //val jsonA=JSONObject(algo)

            var values2:ContentValues= ContentValues()
            values2.put("version", DB_VERSION)
            values2.put("jsontipoevento",algo)
            db.insert("cosas",values2)


        return values
    }
    fun json(php:String,json:JSONObject):String{
        val url= URL("http://172.22.10.168/Unehorario/"+php+".php")
        val conn = connect(url)
        var text:String=""


        Log.d("jsonenviado",json.toString())
        text = enviarWS(conn,json)
        return text
    }
    fun enviarWS(conn: HttpURLConnection, query:JSONObject):String{
        val outputStream = conn.outputStream
        val outputStreamWriter = OutputStreamWriter(outputStream,"UTF-8")
        outputStreamWriter.write(query.toString())
        outputStreamWriter.flush()
        outputStreamWriter.close()
        return conn.inputStream.use { it.reader().use { reader->reader.readText() } }
    }
    fun connect(url: URL): HttpURLConnection {
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout=10000
        conn.connectTimeout=15000
        conn.requestMethod="POST"
        conn.setRequestProperty("Accept","application/json;charset=utf-8")
        conn.doInput=true
        conn.doOutput=true
        conn.connect()
        return conn
    }
}
