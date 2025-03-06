package fr.isen.aliagafuentesjuanandres.isensmartcompanion.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Esta clase se encarga de gestionar las alertas de eventos mediante notificaciones.
 * Se ejecuta en segundo plano utilizando WorkManager.
 */
class AlertSettings(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Obtener el ID del evento desde los datos de entrada
        val eventId = inputData.getString("eventId") ?: return Result.failure()

        // Recuperar la preferencia del usuario para este evento
        val reminderPreferences = NotificationPreferences(applicationContext)
        val isReminderSet = reminderPreferences.getReminder(eventId)

        // Si el usuario ha activado el recordatorio para este evento, se genera una notificación
        if (isReminderSet) {
            createNotificationChannel()
            showNotification(eventId)
        }

        return Result.success()
    }

    /**
     * Crea un canal de notificaciones si el dispositivo está en Android Oreo o superior.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel",  // ID del canal
                "Notificaciones de Eventos", // Nombre del canal
                NotificationManager.IMPORTANCE_HIGH // Nivel de importancia
            ).apply {
                description = "Canal para recordatorios de eventos"
            }
            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * Muestra una notificación al usuario para recordarle sobre el evento.
     *
     * @param eventId Identificador único del evento.
     */
    private fun showNotification(eventId: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, "event_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)  // Icono de la notificación
            .setContentTitle("Recordatorio de Evento")
            .setContentText("¡El evento al que te suscribiste está por comenzar!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Se usa el hash del eventId para asegurarse de que la notificación sea única
        notificationManager.notify(eventId.hashCode(), notification)
    }
}
