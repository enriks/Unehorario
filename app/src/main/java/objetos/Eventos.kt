package objetos

import java.sql.Date

data class Eventos(
        val id:Int,
        val nombre:String,
        val duracion:Int,
        val fecha_inicio:String,
        val fecha_final:String,
        val tipo_evento:Int,
        val repeticion:Int,
        val dias:String,
        val alumno:Int,
        val maestro:String,
        val detalle:String

)
data class Eventos2(
        val id:Int=0,
        val nombre:String="",
        val duracion:Int=0,
        val fecha_inicio:String="",
        val fecha_final:String="",
        val tipo_evento:Int=0,
        val repeticion:Int=0,
        val dias:String="",
        val alumno:Int=0,
        val maestro:String="",
        val detalle:String=""

)