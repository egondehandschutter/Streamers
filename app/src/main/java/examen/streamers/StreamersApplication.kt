package examen.streamers

import android.app.Application
import examen.streamers.data.AppContainer
import examen.streamers.data.DefaultAppContainer

class StreamersApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}