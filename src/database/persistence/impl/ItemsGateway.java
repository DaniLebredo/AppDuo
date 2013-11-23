package database.persistence.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import database.persistence.BDHelper;

public interface ItemsGateway {
	
	//Listar los items
	public Map<String, Object> getItem(Long idItem, BDHelper helper); //Busqueda de uno específico
	public List<Map<String, Object>> getItemsDelCanal(Long idCanal, BDHelper helper); //Busqueda de todos por canal
	public List<Map<String, Object>> getItemsGuardados(BDHelper helper, Long idCanal); //Dame los que he guardado
	//Marcar como guardado y sin guardar
	public void guardar(Long idItem, BDHelper helper);
	public void noGuardar(Long idItem, BDHelper helper);
	//Borrar los elementos antiguos de la base, que existan a partir de una fecha
	//Se borran todos los anteriores a esa fecha, salvo los marcados
	public void limpiarPersistencia(Date fechaBase, BDHelper helper);
	//Añadir item
	public Long añadirItem(Map<String, Object> item, BDHelper helper);
	//Borrar item
	public void borrarItem(Long idItem, BDHelper helper);
	
	//Selecciona el máximo id
	public Long getMaxID(BDHelper helper, Long idCanal);

}
