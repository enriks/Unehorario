package uneatlantico.unehorario

import android.app.Application

class App : Application(){
    companion object {
        lateinit var instace: App
    }

    override fun onCreate() {
        super.onCreate()
        instace = this
    }
}