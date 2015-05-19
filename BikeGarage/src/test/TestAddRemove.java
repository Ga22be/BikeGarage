package test;

import java.util.NoSuchElementException;

import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class TestAddRemove {
	
	public static void main(String[] args){
		DataBase db = new DataBase();
		try {
			db.addBikeOwner("Name", "199510247712", "Mail");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			db.removeBikeOwner("199510247712");
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			db.addBikeOwner("Name", "199510247712", "Mail");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
