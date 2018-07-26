package uneatlantico.unehorario

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import objetos.Eventos
import objetos.Eventos2
import org.jetbrains.anko.db.update
import org.jetbrains.anko.doAsync

class SqliteHandler(ctx: Context?) : SQLiteOpenHelper(ctx,Tool.DB_NAME,null,Tool.DB_VERSION) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    onCreate(p0)
    }


val tols:Tool = Tool()
    companion object {
        private var instance: SqliteHandler? = null

        @Synchronized
        fun getInstance(ctx: Context): SqliteHandler {
            if (instance == null) {
                instance = SqliteHandler(ctx.getApplicationContext())
            }
            return instance!!
        }
    }


    object tipo_evento{
        var NAME = "tipo_evento"
        var ID = "_id"
        var NOMBRE = "nombre"
    }

    object evento{
        val NAME ="evento"
        val ID="_id"
        val NOMBRE = "nombre"
        val DURACION = "duracion"
        val FECHA_INICIO = "hora_inicio"
        val FECHA_FINAL = "fecha_final"
        val TIPO_EVENTO = "tipo_evento"
        val REPETICION = "repeticion"
        val DIAS = "dias"
        val ID_ALUMNO = "id_alumno"
        val ID_PROFESOR = "id_profesor"
        val DETALLES = "descripcion"
    }
    /**
    Select de eventos
     **/
    @RequiresApi(Build.VERSION_CODES.O)
    fun selectEvento(): ArrayList<Eventos> {
        var evnt: ArrayList<Eventos> = ArrayList()
        val db: SQLiteDatabase = readableDatabase
        val c = db.rawQuery("Select * from evento order by fecha_inicio", null)
        Log.d("lamierda",c.toString())
        while (c.moveToNext()){
            Log.d("mecagoendios",evnt.toString())
            var id= c.getInt(c.getColumnIndex("_id"))
            Log.d("mecagoendioss",id.toString())
            Log.d("diasemana",tols.changeFecha(c.getString(c.getColumnIndex("fecha_final"))))
            //1var
            var a: Eventos =objetos.Eventos(c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("nombre")),c.getInt(c.getColumnIndex("duracion")),tols.changeFecha(c.getString(c.getColumnIndex("fecha_inicio"))),tols.changeFecha(c.getString(c.getColumnIndex("fecha_final"))),c.getInt(c.getColumnIndex("tipo_evento")),c.getInt(c.getColumnIndex("repeticion")),c.getString(c.getColumnIndex("dias")),c.getInt(c.getColumnIndex("id_alumno")),c.getString(c.getColumnIndex("id_profesor")),c.getString(c.getColumnIndex("detalle")))
            Log.d("lol",a.toString())
            evnt.add(a)
        }

        return evnt
    }
    fun selectoneEvento(id:Int): Eventos {
        var evnt= Eventos(0,"a",0,"","",0,0,"",0,"","")
        val db: SQLiteDatabase = readableDatabase
        val c = db.rawQuery("Select * from evento where _id =? order by fecha_inicio", arrayOf(id.toString()))
        Log.d("lamierda",c.toString())
        if (c.moveToNext()){
             evnt= Eventos(c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("nombre")),c.getInt(c.getColumnIndex("duracion")),c.getString(c.getColumnIndex("fecha_inicio")),c.getString(c.getColumnIndex("fecha_final")),c.getInt(c.getColumnIndex("tipo_evento")),c.getInt(c.getColumnIndex("repeticion")),c.getString(c.getColumnIndex("dias")),c.getInt(c.getColumnIndex("id_alumno")),c.getString(c.getColumnIndex("id_profesor")),c.getString(c.getColumnIndex("detalle")))
        }
        return evnt
    }
    /**
     * update de eventos
     */
    fun updateEventos(valus:ContentValues,id: Int){
        val dad = writableDatabase
        dad.update("evento",valus,"_id="+id,null)
    }
    /**
     * Select de los Json que existen
     */
    fun selectJsonEvento():String{
        val db: SQLiteDatabase = readableDatabase
        val c = db.rawQuery("Select * from evento order by _id desc",null)
        var json =""
        if (c.moveToNext()){
            json = c.getString(c.getColumnIndex("nombre"))
        }
        return json
    }
    fun selectJsonTipoEvento():String{
        val db: SQLiteDatabase = readableDatabase
        val c = db.rawQuery("Select * from cosas order by _id desc",null)
        var json =""
        if (c.moveToNext()){
            json = c.getString(c.getColumnIndex("jsontipoevento"))
        }
        return json
    }

    /**
     * Verificacion de que existe el usuario
     */
    public fun verifyUsuario(nombreAlumno:String): Boolean{
        var bool:Boolean = false
        val readable: SQLiteDatabase = readableDatabase
        val result = readable.rawQuery("select nombre from alumno where nombre = '"+nombreAlumno+"'",null)
        if (!result.moveToNext()){
            bool = true
            var values:ContentValues = ContentValues()
            values.put("nombre",nombreAlumno)
            insert("alumno",values)
        }
        Log.d("valorbool",bool.toString())
        return bool
    }

    /**
     * obtener el id del usuario en la base de datos interna en base del nombre
     */
    fun getIDUsuario(usuario:String):Int{
        var ent:Int = 0
        val readable: SQLiteDatabase = readableDatabase
        val result = readable.rawQuery("select nombre from alumno where nombre = '"+usuario+"'",null)
        if (verifyUsuario(usuario)) {
            ent = result.getInt(result.getColumnIndex("nombre"))
        }
        return ent
    }

    /**
     * obtener el tipo de evento para los combobox que estan en los formularios
     */
    fun getTipoEvento():ArrayList<String>{
        var arr: ArrayList<String> = ArrayList()
        val db: SQLiteDatabase = readableDatabase
        val c = db.rawQuery("Select * from tipo_evento where nombre != 'clase'",null)
        while (c.moveToNext()){
            arr.add(c.getString(c.getColumnIndex("nombre")))
        }
        return arr
    }

    /**
     * Insert general para cualquier tabla
     */
    public fun insert(nombreTabla: String, values: ContentValues){
        val writable: SQLiteDatabase = writableDatabase

            try {
                writable.insert(nombreTabla,null,values)

            }catch (e: Exception){

                Log.d("esto",e.toString())
            }
    }

    /**
     * funcion que corre en la primera run del programa para verificar si existen datos en la base
     * de datos del web service
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public fun firstRun():Boolean{
        var boolean:Boolean=false
    try{

        tols.syncDatabaseTipoEvento(this)
        tols.syncDatabaseEventos(Tool.USUARIO,this)
        Tool.ITEMS=selectEvento()
        boolean=true
        //Tool.ITEMS=selectEvento()
    }catch (e:Exception){
        Log.d("FalloFirstRun",e.toString())

    }
    return boolean
    }

    /**
     * Funcion que se ejecuta cuando se crea la base de datos
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {

            try {
                db!!.execSQL("CREATE  TABLE if not exists `tipo_evento` (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`nombre` varchar(250)  NOT NULL)")
                db.execSQL("CREATE TABLE if not exists `evento` ( `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  `nombre` varchar(250) NOT NULL,  `duracion` int(11) DEFAULT NULL,`fecha_inicio` varchar(500) NOT NULL, `fecha_final` varchar(500) DEFAULT NULL,  `tipo_evento` int(11) NOT NULL, `repeticion` int(11) NOT NULL,`dias` varchar(300) DEFAULT '',`id_alumno` int(11) NOT NULL,`id_profesor` varchar(110) NOT NULL,detalle varchar(500) default '')")
                db.execSQL("create table if not exists alumno (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre varchar(250) not null)")
                db.execSQL("CREATE  TABLE if not exists `cosas` (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`version` int NOT NULL,jsonevento varchar(1500),jsontipoevento varchar(1800))")
                doAsync {
                     val booll = firstRun()
                    if( !booll){
                        var values:ContentValues= ContentValues()
                        values.put("nombre","evento Y")
                        insert("tipo_evento",values)
                    }
                }
            }catch (e: Exception){
                Log.d( "problemasTabla",e.toString())
            }


    }

    /**
     * delete general
     */
    fun delete(nombreTabla: String,condicion:String){
        val db= writableDatabase
        db.delete(nombreTabla,condicion,null)
        db.close()
    }
    val Context.database: SqliteHandler
        get() = SqliteHandler.getInstance(getApplicationContext())




}