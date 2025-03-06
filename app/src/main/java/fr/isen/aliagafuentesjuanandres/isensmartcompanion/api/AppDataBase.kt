package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase que representa la base de datos de la aplicación utilizando Room.
 *
 * Se define como una base de datos abstracta que extiende `RoomDatabase`,
 * lo que permite gestionar el almacenamiento de datos de manera estructurada.
 *
 * @Database: Indica que esta clase es una base de datos y especifica las entidades que maneja.
 * La versión se establece en 1, lo que significa que es la primera versión de la base de datos.
 */
@Database(entities = [Interaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona el acceso al DAO (Data Access Object) que permite
     * interactuar con la tabla de `Interaction` en la base de datos.
     */
    abstract fun interactionDao(): InteractionDao

    companion object {
        // Instancia única de la base de datos para evitar múltiples accesos simultáneos.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene una instancia de la base de datos. Si aún no ha sido creada, se inicializa
         * utilizando `Room.databaseBuilder` con el contexto de la aplicación.
         *
         * Se usa `synchronized` para asegurar que solo una instancia se cree en caso de concurrencia.
         *
         * @param context Contexto de la aplicación para acceder a los recursos.
         * @return Instancia única de `AppDatabase`.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "interactions_database" // Nombre de la base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
