package database.persistence.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import database.persistence.BDHelper;

public class CanalesGatewayImpl implements CanalesGateway {

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
	
		
	/**
	 * Método que marca un canal como suscrito por el usuario.
	 * @param idCanal Identificador único del canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void suscribir(Long idCanal, BDHelper helper) {	
		compruebaPrecondiciones(idCanal, helper);		
		
		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			ContentValues values = new ContentValues();
			values.put(BDHelper.SUSCRITO, "Si");	
			db.update(BDHelper.NOMBRE_TABLA_CANALES, values, BDHelper.ID_CANAL + " = ?", new String[]{ idCanal.toString()});	
		}finally{
			db.close();	
			r.unlock();
		}
	}

	/**
	 * Método auxiliar que comprueba las precondiciones del método: que exista el canal
	 * y que la base de datos esté en un estado correcto.
	 * @param idCanal Identificador del canal a buscar.
	 * @param helper Base de datos.
	 */
	private void compruebaPrecondiciones(Long idCanal, BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ningún helper.");		
		if( idCanal == null || idCanal < 0)		
			throw new IllegalArgumentException("Identificador del canal no valido: " + idCanal);
		if(  !existeID(idCanal, helper) )
			throw new IllegalArgumentException("El canal no existe en el sistema de persistencia: " + idCanal);
	}

	/**
	 * Método que anula la suscripción a un canal.
	 * @param idCanal Identificador único del canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void anularSuscripcion(Long idCanal, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);
		
		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			ContentValues values = new ContentValues();
			values.put(BDHelper.SUSCRITO, "No");	
			db.update(BDHelper.NOMBRE_TABLA_CANALES, values, BDHelper.ID_CANAL + " = ?", new String[]{ idCanal.toString()});	
		}finally
		{
			db.close();	
			r.unlock();
		}
		
	}

	/**
	 * Método que devuelve la información de un canal, en base a su identificador
	 * único.
	 * @param idCanal Identificador único del canal.
	 * @return Mapa clave/valor contenedor de los datos de la base de datos.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public Map<String, Object> getCanal(Long idCanal, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);
		
		Map<String, Object> resultado = new HashMap<String,Object>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		try{
			r.lock();
			db = helper.getReadableDatabase();
			cursor = db.query(BDHelper.NOMBRE_TABLA_CANALES, new String[] {BDHelper.TITULO, BDHelper.LINK, BDHelper.DESCRIPCION, BDHelper.SUSCRITO }, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() }, null , null , null);
			while(cursor.moveToNext()){
				resultado.put("Titulo", cursor.getString(0));
				resultado.put("Link",cursor.getString(1) );
				resultado.put("Descripcion", cursor.getString(2));
				resultado.put("Suscrito", cursor.getString(3));
		}
		}finally
		{
			db.close();
			cursor.close();
			r.unlock();
		}
		return resultado;
	}

	/**
	 * Método que guarda un nuevo canal en el sistema de persistencia.
	 * @param canal Mapa contenedor de todos los datos del canal.
	 * @return Identificador único del nuevo canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public long añadirCanal(Map<String, Object> canal, BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ningún helper.");		
		if( canal == null)		
			throw new IllegalArgumentException("No se han introducido datos");
		compruebaCamposObligadorios(canal);
		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Long idCanal = null;
		try{
			r.lock();
			
			db = helper.getWritableDatabase();		
			ContentValues newValues = new ContentValues(); 
			// Assign values for each row. 
			newValues.put("Titulo", (String) canal.get("Titulo")); 
			newValues.put("Link",  (String) canal.get("Link"));
			newValues.put("Descripcion",  (String) canal.get("Descripcion"));
			newValues.put("Suscrito",  (String) canal.get("Suscrito"));
			db.insert(BDHelper.NOMBRE_TABLA_CANALES, null, newValues);	
			db.close();
			
			db = helper.getReadableDatabase();			
			cursor = db.rawQuery("SELECT max(" + BDHelper.ID_CANAL + ") as maximo FROM " + BDHelper.NOMBRE_TABLA_CANALES, null);
			while(cursor.moveToNext()){
				idCanal = cursor.getLong(0);
			}		
			
		}finally
		{
			db.close();
			cursor.close();
			r.unlock();
		}
			return idCanal;
	}

	/**
	 * Método auxiliar que comprueba que estén todos los campos 
	 * obligatorios a guardar en la base de datos.
	 * @param canal Datos a guardar.
	 */
	private void compruebaCamposObligadorios(Map<String, Object> canal) {
		if (canal.containsKey("Titulo") && canal.containsKey("Link")) {
			String titulo = canal.get("Titulo").toString();
			if (titulo == "" || titulo == null)
				throw new IllegalArgumentException(
						"Debe especificarse un título para añadir un canal.");
			String link = canal.get("Link").toString();
			if (link == "" || titulo == null)
				throw new IllegalArgumentException(
						"Debe especificarse un link para añadir un canal.");
		}else
			throw new IllegalArgumentException("Faltan campos");
		
		if(canal.containsKey("Suscrito")){
			String suscrito = canal.get("Suscrito").toString();
			if(suscrito == "")
				throw new IllegalArgumentException("Se ha especificado el campo suscripción, pero está vacio.");
			if(!suscrito.equals("Si") && !suscrito.equals("No"))
				throw new IllegalArgumentException("El campo suscrito tiene un valor invalido, " + suscrito);
		}else{//Valor por defecto
			canal.put("Suscrito", "No");
		}
		
		
		
	}

	/**
	 * Método que actualiza el link de un canal.
	 * @param idCanal Identificador único del canal.
	 * @param link Nuevo link del canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void actualizarLink(Long idCanal, String link, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);
		
		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			ContentValues values = new ContentValues(); 
			values.put("Link", link); 
			db.update(BDHelper.NOMBRE_TABLA_CANALES, values, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() });	
		}finally
		{
			db.close();
			r.unlock();
		}
			
	}

	/**
	 * Método que actualiza el titulo de un canal.
	 * @param idCanal Identificador único del canal.
	 * @param titulo Nuevo titulo del canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void actualizarTitulo(Long idCanal, String titulo, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);
		
		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			ContentValues values = new ContentValues(); 
			values.put("Titulo", titulo); 
			db.update(BDHelper.NOMBRE_TABLA_CANALES, values, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() });		
		}
		finally
		{
			db.close();
			r.unlock();
		}
			
	}

	/**
	 * Método que actualiza la descripción de un canal.
	 * @param idCanal Identificador único del canal.
	 * @param descripcion Descripción nueva del canal.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void actualizarDescripcion(Long idCanal, String descripcion, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);	

		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			ContentValues values = new ContentValues(); 
			values.put("Descripcion", descripcion); 
			db.update(BDHelper.NOMBRE_TABLA_CANALES, values, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() });	
		}
		finally
		{
			db.close();
			r.unlock();
		}
	}

	/**
	 * Método que borra un canal del sistema de persistencia.
	 * @param idCanal Identificador del canal a borrar.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public void borrarCanal(Long idCanal, BDHelper helper) {
		compruebaPrecondiciones(idCanal, helper);

		SQLiteDatabase db = null;
		
		try{
			r.lock();
			db = helper.getWritableDatabase();		
			db.delete(BDHelper.NOMBRE_TABLA_CANALES, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() });	
		}finally
		{
			db.close();
			r.unlock();
		}
	}

	/**
	 * Método que lee los canales a los que el usuario se ha suscrito.
	 * @param BDHelper Base de datos.
	 * @return Lista con toda la información de los canales contenida en la base de datos.
	 * @throws IllegalArgumentException - Alguno de los parámetros no son validos.
	 * @throws SQLiteException - Error de programación o error no contemplado con SQLite.
	 */
	@Override
	public List<Map<String, Object>> getCanalesSuscritos(BDHelper helper) {
		if(helper == null)
			throw new IllegalArgumentException("Ningún helper.");	
		
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			r.lock();
			db = helper.getReadableDatabase();
			cursor = db.query(BDHelper.NOMBRE_TABLA_CANALES, null , BDHelper.SUSCRITO + " = ?", new String[] { "Si" }, null , null , null);
			while(cursor.moveToNext()){
				Map<String, Object> resultado = new HashMap<String,Object>();
				resultado.put("Titulo", cursor.getString(1));
				resultado.put("Link",cursor.getString(2) );
				resultado.put("Descripcion", cursor.getString(3));
				resultado.put("Id_canal", cursor.getLong(0));
				resultado.put("Suscrito", cursor.getString(4));
				ret.add(resultado);
			}
		}finally
		{
			db.close();
			cursor.close();
			r.unlock();
		}
		
			return ret;		
	}

	/**
	 * Método auxiliar que comprueba si existe un ID en la base de datos.
	 * @param idCanal Identificador del canal.
	 * @param helper Base de datos
	 * @return	true/false si existe o no.
	 */
	private boolean existeID(Long idCanal, BDHelper helper){		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		boolean result = false;
		try{
			r.lock();
			db = helper.getReadableDatabase();
			cursor = db.query(BDHelper.NOMBRE_TABLA_CANALES, null, BDHelper.ID_CANAL + " = ?", new String[] { idCanal.toString() }, null, null, null);
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

}
