package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones de acceso a la base de datos
 * para la entidad `Interaction`, utilizando Room.
 *
 * `@Dao` indica que esta interfaz es un Data Access Object (DAO),
 * encargado de gestionar las consultas y modificaciones en la base de datos.
 */
@Dao
interface InteractionDao {

    /**
     * Inserta una nueva interacción en la base de datos.
     *
     * @param interaction Objeto `Interaction` que se guardará en la base de datos.
     */
    @Insert
    suspend fun insertInteraction(interaction: Interaction)

    /**
     * Obtiene todas las interacciones almacenadas en la base de datos.
     *
     * @return Un `Flow` que emite la lista de interacciones cada vez que cambia.
     */
    @Query("SELECT * FROM interaction")
    fun getAllInteractions(): Flow<List<Interaction>> // Retorna un flujo de datos en tiempo real.

    /**
     * Elimina una interacción específica de la base de datos.
     *
     * @param interaction Objeto `Interaction` que se desea eliminar.
     */
    @Delete
    suspend fun deleteInteraction(interaction: Interaction)

    /**
     * Elimina todas las interacciones de la base de datos.
     */
    @Query("DELETE FROM interaction")
    suspend fun deleteAllInteractions()
}
