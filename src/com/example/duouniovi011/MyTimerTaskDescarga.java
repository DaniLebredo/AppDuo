package com.example.duouniovi011;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TimerTask;

import parser.RssItem;
import parser.RssProvider;
import parser.RssProvider.Section;
import parser.RssProviderImpl;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import database.persistence.BDHelper;
import database.persistence.DatabaseManager;
import database.persistence.PersistenceFactory;
import database.persistence.impl.CanalesGateway;
import database.persistence.impl.ItemsGateway;

public class MyTimerTaskDescarga extends TimerTask {

	@Override
	public void run() {

		// Stack de secciones: Guardo la seccion y el id de la misma
		Queue<Object[]> canalesQueue = new LinkedList<Object[]>();

		// Lleno el stack
		Map<RssProviderImpl.Section, String> mapaCanales = RssProviderImpl
				.getSecciones();
		Object[] arrayCanales = mapaCanales.entrySet().toArray();
		for (int index = 0; index < mapaCanales.size(); index++) {
			@SuppressWarnings("unchecked")
			Entry<RssProvider.Section, String> canal = (Entry<Section, String>) arrayCanales[index];
			Object[] channel = new Object[2];
			channel[0] = canal.getKey();
			channel[1] = index + 1;
			canalesQueue.add(channel);
		}

		// Abre el lector
		RssProviderImpl lector = new RssProviderImpl();
		// Abre las conexiones con la base de datos
		BDHelper base = DatabaseManager.getConnection();
		ItemsGateway ig = PersistenceFactory.getItemsGateway();
		CanalesGateway cg = PersistenceFactory.getCanalesGateway();
		// Declaro la variable que contar� el numero de nuevos suscritos
		int nuevosItemsSuscritos = 0;

		while (!canalesQueue.isEmpty()) {

			Object[] aDescargar = canalesQueue.poll();
			Integer id = (Integer) aDescargar[1];
			Long idCanal = id.longValue();
			// Lee el identificador del �ltimo item registrado del canal
			long idUltimo = ig.getMaxID(base, idCanal);

			// Declaro las variables que uso
			List<RssItem> elementos = null; // Aqu� se guardar� la lista de
											// elementos descargados
			boolean hayConexion = false; // Aqu� guardar� si la conexi�n est� o
											// no disponible
			try {
				// Des7carga los elementos del canal
				elementos = lector.getLast((Section) aDescargar[0],
						(int) idUltimo);
				hayConexion = true; // Si todo fue bien, es que hay conexi�n
			} catch (NullPointerException e) {
				// Hay un error de conexi�n
				Log.e("Servicio descarga", "error de conexi�n");
			}

			if (elementos != null && hayConexion)// Caso normal: todo fue bien
			{
				// Grabar en la base de datos
				grabarEnBaseDeDatos(base, ig, idCanal, elementos);
				// Leo a ver si est� suscrito al canal para generar la
				// notificaci�n
				Map<String, Object> infoCanal = cg.getCanal(idCanal, base);
				if (infoCanal.get("Suscrito").toString().equals("Si")) // Si
																		// est�
																		// suscrito
					nuevosItemsSuscritos += elementos.size(); // A�ade el numero
																// de items
																// descargados
			} else if (elementos == null || !hayConexion) { // Hay conexi�n pero
															// no pudo
															// descargarse nada
															// o no hay conexion
				canalesQueue.add(aDescargar);
			}

		}
		// Envio notificaciones
		if (nuevosItemsSuscritos > 0)
			crearNotificacion(base, nuevosItemsSuscritos);

	}

	private void grabarEnBaseDeDatos(BDHelper base, ItemsGateway ig,
			Long idCanal, List<RssItem> elementos) {
		// Por cada elemento
		for (RssItem item : elementos) {
			// Pon en un mapa toda la informaci�n a guardar del elemento.
			Map<String, Object> it = new HashMap<String, Object>();
			it.put("Titulo", item.getTitle());
			it.put("Link", item.getLink());
			it.put("Descripcion", item.getContent());
			it.put("Fecha", item.getDate());
			it.put("Id_canal", idCanal);
			it.put("Guardado", "No");
			it.put("Id_item", (long) item.getId());
			// Insertalo en la base de datos
			ig.a�adirItem(it, base);
		}
	}

	@SuppressWarnings("deprecation")
	private void crearNotificacion(BDHelper base, int nuevosItemsSuscritos) {
		// Si hay nuevos elementos
		String servicio = Context.NOTIFICATION_SERVICE;
		NotificationManager notificacion = (NotificationManager) base
				.getContexto().getSystemService(servicio);
		String texto = nuevosItemsSuscritos
				+ " Nuevos publicaciones a las que est�s suscrito.";
		int icono = R.drawable.ic_launcher;
		Long hora = System.currentTimeMillis();
		// TODO actualizar Notification a Notification.Builder
		Notification miNotificacion = new Notification(icono, texto, hora);

		Context contexto = base.getContexto();
		String titulo = "AppDuo";
		String descripcion = nuevosItemsSuscritos
				+ " Nuevas publicaciones a las que est�s suscrito.";
		// al pulsar sobre la notificaci�n, esta se borra autom�ticamente.
		// Poner sonido
		miNotificacion.defaults = Notification.DEFAULT_ALL;
		miNotificacion.flags = Notification.FLAG_AUTO_CANCEL;

		Intent notificacionIntent = new Intent(contexto, MainActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0,
				notificacionIntent, 0);
		miNotificacion.setLatestEventInfo(contexto, titulo, descripcion,
				contIntent);
		notificacion.notify(1, miNotificacion);
	}

}
