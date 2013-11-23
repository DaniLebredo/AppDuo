package database.persistence;

import database.persistence.impl.CanalesGateway;
import database.persistence.impl.CanalesGatewayImpl;
import database.persistence.impl.ItemsGateway;
import database.persistence.impl.ItemsGatewayImpl;


public class PersistenceFactory {
	
	public static ItemsGateway getItemsGateway(){
		return new ItemsGatewayImpl();
	}
	
	public static CanalesGateway getCanalesGateway(){
		return new CanalesGatewayImpl();
	}

}
