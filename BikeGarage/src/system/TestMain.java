package system;

import bikeGarageDatabase.DataBase;
import gui.BikeGarageGUI;
import gui.MainMenu;

public class TestMain {

	public static void main(String[] args) {
		DataBase database = new DataBase();
		BikeGarageGUI menu = new BikeGarageGUI(database);

	}

}
