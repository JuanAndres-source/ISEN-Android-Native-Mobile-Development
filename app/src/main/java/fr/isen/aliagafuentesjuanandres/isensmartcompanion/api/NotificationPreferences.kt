package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import android.content.Context
import android.content.SharedPreferences

class NotificationPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)  // Se obtiene la instancia de SharedPreferences para almacenar las preferencias del usuario.

    // Guarda el estado de la notificación (activado/desactivado) para un evento específico.
    fun saveReminder(eventId: String, isReminderSet: Boolean) {
        sharedPreferences.edit().putBoolean(eventId, isReminderSet).apply()  // Guarda el valor del recordatorio en las preferencias compartidas.
    }

    // Recupera el estado del recordatorio para un evento específico.
    fun getReminder(eventId: String): Boolean {
        return sharedPreferences.getBoolean(eventId, false)  // Devuelve el valor del recordatorio, por defecto 'false' si no se ha configurado.
    }
}
