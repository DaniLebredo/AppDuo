package database.persistence;

public class DatabaseManager {
	
	private static BDHelper bdConnection;
	
	
	private DatabaseManager(BDHelper bd){
		DatabaseManager.bdConnection = bd;
	}
	
	public static void createConnection(BDHelper bd){
		DatabaseManager.bdConnection = bd;
	}
	
	
	public static BDHelper getConnection(){
		return bdConnection;
	}
	
	
	

}
