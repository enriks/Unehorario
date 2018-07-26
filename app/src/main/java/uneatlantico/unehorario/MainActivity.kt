package uneatlantico.unehorario

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import com.firebase.ui.auth.AuthUI
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.IdpResponse
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private var m_listview: ListView? = null
    val handler = SqliteHandler(this@MainActivity)
    val tols = Tool()
    var intento:Intent=Intent()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val providers = Arrays.asList(
                AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)

        //Salir
       /* AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    fun onComplete(task: Task<Void>) {
                        // ...
                    }
                })*/
        var text = ""
        intento= Intent(this,DiaListActivity::class.java)


        doAsync{
            val url=URL("http://172.22.10.168/Unehorario/GetEventos.php")
            val conn = connect(url)

            val usuario = JSONObject()
            usuario.put("nombre","manuel.coto")
            Log.d("jsonprimero",usuario.toString())
            text = enviarWS(conn,usuario)
            Log.d("imprimir",text)


        }


    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Log.d("usuario",user.email.toString())
                    val separated=user.email.toString().split("@".toRegex())
                    Tool.USUARIO = separated[0]

                    Log.d("nombreUsuario",Tool.USUARIO)
                    handler.verifyUsuario(Tool.USUARIO)
                    Tool.ITEMS=handler.selectEvento()
                    startActivity(intento)


                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    fun enviarWS(conn:HttpURLConnection,query:JSONObject):String{
        val outputStream = conn.outputStream
        val outputStreamWriter = OutputStreamWriter(outputStream,"UTF-8")
        outputStreamWriter.write(query.toString())
        outputStreamWriter.flush()
        outputStreamWriter.close()
        return conn.inputStream.use { it.reader().use { reader->reader.readText() } }
    }
    fun connect(url: URL):HttpURLConnection{
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
