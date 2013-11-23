package database.persistence.impl;

import java.util.List;
import java.util.Map;

import database.persistence.BDHelper;

public interface CanalesGateway {

	
	
	//Marcar un canal como suscrito
	public void suscribir(Long idCanal, BDHelper helper);
	//Borrar una suscripci�n
	public void anularSuscripcion(Long idCanal, BDHelper helper);	
	//Listar los canales suscritos
	public List<Map<String, Object>> getCanalesSuscritos(BDHelper helper);
	
	//Listar la informacion del canal
	public Map<String, Object> getCanal(Long idCanal, BDHelper helper);
	//A�adir canal
	public long a�adirCanal(Map<String, Object> canal, BDHelper helper);
	public void borrarCanal(Long idCanal, BDHelper helper);
	//Actualizar la informaci�n del canal
	public void actualizarLink(Long idCanal, String link, BDHelper helper);
	public void actualizarTitulo(Long idCanal, String titulo, BDHelper helper);
	public void actualizarDescripcion(Long idCanal, String descripcion, BDHelper helper);

	
	
}
