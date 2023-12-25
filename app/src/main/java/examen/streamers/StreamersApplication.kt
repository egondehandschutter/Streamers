package examen.streamers

import android.app.Application
import examen.streamers.data.AppContainer
import examen.streamers.data.DefaultAppContainer

class StreamersApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}