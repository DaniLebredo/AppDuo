package database.persistence.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import database.persistence.BDHelper;

public class ItemsGatewayImpl implements ItemsGateway {

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
	
	
	/**
	 * M�todo encargado de cargar un Item de la base de datos, en funci�n
	 * de su identificador �nico.
	 * @param idItem Identificador �nico del item en la base de datos.
	 * @return Mapa con los valores del item.
	 */
	@Override
	public Map<String, Object> getItem(Long idItem, BDHelper helper) {
		compruebaPrecondiciones(idItem, helper);	
		
		Cursor cursor = null;
		Map<String, Object> resultado = new HashMap<String,Object>();

		try{
		
		r.lock();	
		cursor = helper.getReadableDatabase().query(BDHelper.NOMBRE_TABLA_ITEMS, null , BDHelper.ID_ITEM + "= ?", new String[]{ idItem.toString() }, null, null, null);
		
		while(cursor.moveToNext()){
			resultado.put("Titulo", cursor.getString(0));
			resultado.put("Link",cursor.getString(1) );
			resultado.put("Descripcion", cursor.getString(2));
			resultado.put("Fecha", cursor.getString(3));
			resultado.put("Id_canal", cursor.getLong(4));
			resultado.put("Guardado", cursor.getString(5));
			resultado.put("Id_item", cursor.getLong(6));
		}
		
		}finally
		{
			cursor.close();
			r.unlock();
		}
		
		return resultado;
	}

	/**
	 * M�todo que lista todos los items de un determinado canal.
	 * @param idCanal Identificador �nico del cana.
	 * @return Listado con los valores de cada item, contenidos en un mapa clave/valor.
	 */
	@Override
	public List<Map<String, Object>> getItemsDelCanal(Long idCanal, BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");	
		if( idCanal == null || idCanal < 0)		
			throw new IllegalArgumentException("Identificador del canal no valido: " + idCanal);
		
		Cursor cursor = null;		
		List<Map<String, Object>> resultado = new ArrayList<Map<String,Object>>();
		
		try{
		
		r.lock();
		cursor = helper.getReadableDatabase().query(BDHelper.NOMBRE_TABLA_ITEMS, new String[]{BDHelper.TITULO, BDHelper.LINK, BDHelper.DESCRIPCION, BDHelper.FECHA, BDHelper.GUARDADO, BDHelper.ID_ITEM}, BDHelper.ID_CANAL + "= ?", new String[]{ idCanal.toString() }, null, null, "Id_item DESC");
		while(cursor.moveToNext()){
			Map<String,Object> mapa = new HashMap<String,Object>();
			mapa.put("Titulo", cursor.getString(0));
			mapa.put("Link",cursor.getString(1) );
			mapa.put("Descripcion",cursor.getString(2) );
			mapa.put("Fecha",cursor.getString(3) );
			mapa.put("Guardado",cursor.getString(4) );
			mapa.put("Id_item",cursor.getLong(5) );	
			resultado.add(mapa);
		}
		}finally{
			cursor.close();
			r.unlock();
		}
		return resultado;	
	}

	/**
	 * M�todo que recupera los items que el usuario ha escogido guardar.
	 * @return Listado con los items, contenidos en un mapa por item.
	 */
	@Override
	public List<Map<String, Object>> getItemsGuardados(BDHelper helper, Long idCanal) {
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");	
		
		Cursor cursor = null;			
		List<Map<String, Object>> resultado = new ArrayList<Map<String,Object>>();
				
		try{
			r.lock();		
			cursor = helper.getWritableDatabase().query(BDHelper.NOMBRE_TABLA_ITEMS, null, BDHelper.ID_CANAL + "= ? and " +BDHelper.GUARDADO + "= ?", new String[]{ idCanal.toString(), "Si" }, null, null,  "Id_item DESC");
		while(cursor.moveToNext()){
			Map<String,Object> mapa = new HashMap<String,Object>();
				
			mapa.put("Titulo", cursor.getString(0));
			mapa.put("Link",cursor.getString(1) );
			mapa.put("Descripcion",cursor.getString(2) );
			mapa.put("Fecha",cursor.getString(3) );
			mapa.put("Id_canal",cursor.getLong(4) );
			mapa.put("Guardado",cursor.getString(5) );
			mapa.put("Id_item",cursor.getLong(6) );
				
			resultado.add(mapa);
		}
		}finally{
			cursor.close();
			r.unlock();
		}
		return resultado;
		
	}

	/**
	 * M�todo que marca como guardado un item en la base de datos, identificado por si id.
	 * @idItem Identificador �nico del item.
	 */
	@Override
	public void guardar(Long idItem, BDHelper helper) {
		compruebaPrecondiciones(idItem, helper);
		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();			
			final ContentValues values = new ContentValues();
			values.put("Guardado", "Si");	
			db.update(BDHelper.NOMBRE_TABLA_ITEMS, values, BDHelper.ID_ITEM + " = ?", new String[]{ idItem.toString()});
		}finally{
			db.close();
			r.unlock();
		}
	
	}

	/**
	 * M�todo que desmarca un elemento como guardado.
	 * @param idItem Identificador �nico del item.
	 */
	@Override
	public void noGuardar(Long idItem, BDHelper helper) {
		compruebaPrecondiciones(idItem, helper);
		
		SQLiteDatabase db = null;		
		try{
			r.lock();
			db = helper.getWritableDatabase();			
			final ContentValues values = new ContentValues();
			values.put("Guardado", "No");	
			db.update(BDHelper.NOMBRE_TABLA_ITEMS, values, BDHelper.ID_ITEM + " = ?", new String[]{ idItem.toString()});
		}
		finally
		{		
			db.close();
			r.unlock();
		}
	}

	/**
	 * M�todo encargado de limpiar los items de la base de datos anterior 
	 * a una fecha determina. Solo elimina los que no han sido marcados para conservarlos.
	 * @param fechaBase Fecha l�mite para borrar. Todos los anteriores se borrar�n.
	 */
	@Override
	public void limpiarPersistencia(Date fechaBase, BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");	
		
		SQLiteDatabase db = null;
		
		try{	
			r.lock();
			
			db = helper.getWritableDatabase();				
			String a�o = fechaBase.getYear() + 1900 + "-";
			String mes = fechaBase.getMonth() + 1 + "-";
			String dia = fechaBase.getDay() + 10 + "";
			db.delete(BDHelper.NOMBRE_TABLA_ITEMS, "guardado = 'No' AND fecha < ? ", new String[] { a�o+mes+dia });		
		}finally{
			db.close();
			r.unlock();
		}
	
	}

	/**
	 * M�todo que a�ade un item a la base de datos.
	 * @param Informaci�n a guardar.
	 * @return identificador �nico del nuevo canal.
	 */
	@Override
	public Long a�adirItem(Map<String, Object> item, BDHelper helper) {
		compruebaCamposObligadorios(item);	
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");	
		
		SQLiteDatabase db = null;
		Long idItem  = null;
		
		try{
			r.lock();
			db	= helper.getWritableDatabase();	
		
			final ContentValues values = new ContentValues();
			values.put("titulo", (String) item.get("Titulo"));
			values.put("link", (String) item.get("Link"));
			values.put("descripcion", (String) item.get("Descripcion"));
			values.put("fecha", (String) item.get("Fecha"));
			values.put("id_canal", (Long)item.get("Id_canal") );
			values.put("guardado", (String) item.get("Guardado"));
			values.put("id_item", (Long)item.get("Id_item") );
		
			db.insert(BDHelper.NOMBRE_TABLA_ITEMS, null, values);		
		}finally{
			db.close();
			r.unlock();			
		idItem =  getMaxID(helper, (Long)item.get("Id_canal"));		
	}
		return idItem;
		
	}

	/**
	 * M�todo que borra un item de la base de datos, identificado por su n�mero.
	 */
	@Override
	public void borrarItem(Long idItem, BDHelper helper) {			
		compruebaPrecondiciones(idItem, helper);			
		
		SQLiteDatabase db = null;
		try{
			r.lock();
			db = helper.getWritableDatabase();
			db.delete(BDHelper.NOMBRE_TABLA_ITEMS, "Id_item = ? ", new String[] {idItem.toString() });
		}finally
		{
			db.close();
			r.unlock();
		}
		
	}

	/**
	 * M�todo auxiliar que comprueba si existe un ID en la base de datos.
	 * @param idItem Identificador del item.
	 * @param helper Base de datos
	 * @return	true/false si existe o no.
	 */
	private boolean existeID(Long idItem, BDHelper helper){		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		boolean result = false;
		try{
			r.lock();
			db = helper.getReadableDatabase();
			cursor = db.query(BDHelper.NOMBRE_TABLA_ITEMS, null, BDHelper.ID_ITEM + " = ?", new String[] { idItem.toString() }, null, null, null);
			result = cursor.moveToNext();	
		}
		finally
		{
			db.close();
			cursor.close();
			r.unlock();
		}
		return result;
	}

	

	/**
	 * M�todo auxiliar que comprueba que est�n todos los campos 
	 * obligatorios a guardar en la base de datos.
	 * @param item Datos a guardar.
	 * Lanza excepciones en funci�n de la correcci�n de todos los campos introducidos.
	 */
	private void compruebaCamposObligadorios(Map<String, Object> item) {
		if (item.containsKey("Id_item")){
			String idItem = item.get("Id_item").toString();
			if (idItem == "" || idItem == null)
				throw new IllegalArgumentException("Error en el identificador del item.");
			@SuppressWarnings("unused")
			Long id_item = Long.parseLong(idItem);
		}else
			throw new IllegalArgumentException("Falta el campo id_item.");
		
		
		
		if (item.containsKey("Titulo") && item.containsKey("Link")) {
			String titulo = item.get("Titulo").toString();
			if (titulo == "" || titulo == null)
				throw new IllegalArgumentException("Debe especificarse un t�tulo para a�adir un item.");
			String link = item.get("Link").toString();
			if (link == "" || titulo == null)
				throw new IllegalArgumentException("Debe especificarse un link para a�adir un item.");
		}else
			throw new IllegalArgumentException("Faltan campos");
		
		if(item.containsKey("Guardado")){
			String guardado = item.get("Guardado").toString();
			if(guardado == "")
				throw new IllegalArgumentException("Se ha especificado el campo guardado, pero est� vacio.");
			if(!guardado.equals("Si") && !guardado.equals("No"))
				throw new IllegalArgumentException("El campo guardado tiene un valor invalido, " + guardado);
		}else{//Valor por defecto
			item.put("Guardado", "No");
		}
		
		
		
	}
	
	
	/**
	 * M�todo auxiliar que comprueba las precondiciones del m�todo: que exista un item
	 * y que la base de datos est� en un estado correcto.
	 * @param idItem Identificador del item a buscar.
	 * @param helper Base de datos.
	 */
	private void compruebaPrecondiciones(Long idItem, BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");		
		if( idItem == null || idItem < 0)		
			throw new IllegalArgumentException("Identificador del item no valido: " + idItem);
		if(  !existeID(idItem, helper) )
			throw new IllegalArgumentException("El item no existe en el sistema de persistencia: " + idItem);
	}

	/**
	 * M�todo encargado de recoger el m�ximo identificador de los items.
	 * Devuelve -1 en caso de error.
	 */
	@Override
	public Long getMaxID(BDHelper helper, Long idCanal) {
		if(helper == null)
			throw new IllegalArgumentException("Ning�n helper.");	
		
		Cursor cursor = null;
		Long idItem = null;
		try{
			r.lock();
			cursor = helper.getReadableDatabase().query(BDHelper.NOMBRE_TABLA_ITEMS, new String [] {"MAX(Id_item)"}, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() }, null, null, null);
		while(cursor.moveToNext())
			idItem = cursor.getLong(0);		
		}finally{
			cursor.close();		
			r.unlock();
		}
		return idItem;
	}

	
}
