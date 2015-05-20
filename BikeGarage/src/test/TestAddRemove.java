package test;

import java.util.NoSuchElementException;

import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class TestAddRemove {
	
	public static void main(String[] args){
		DataBase db = new DataBase("testAddRemove");
		
		try {
			db.addBikeOwner("Name", "199510247712", "Mail");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			e.printStackTrace();
		}
		
		try {
			db.addBike("199510247712");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			e.printStackTrace();
		}
		
		System.out.println(db.bikeCount());
		
		
		
		try {
			db.removeBikeOwner("199510247712");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			e.printStackTrace();
		}
		
		try {
			db.addBikeOwner("Name", "199510247712", "Mail");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnavailableOperationException e) {
			e.printStackTrace();
		}
		
	}

}
