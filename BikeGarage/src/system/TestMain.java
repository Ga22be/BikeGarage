package system;

import testDrivers.BarcodePrinter;
import testDrivers.BarcodePrinterTestDriver;
import testDrivers.BarcodeReader;
import testDrivers.BarcodeReaderEntryTestDriver;
import testDrivers.BarcodeReaderExitTestDriver;
import testDrivers.ElectronicLock;
import testDrivers.ElectronicLockTestDriver;
import testDrivers.PinCodeTerminal;
import testDrivers.PinCodeTerminalTestDriver;
import bikeGarageDatabase.DataBase;
import gui.BikeGarageGUI;
import gui.MainMenu;

public class TestMain {

	public static void main(String[] args) {
		
		BarcodePrinter printer = new BarcodePrinterTestDriver();
//		BarcodeReader entryReader = new BarcodeReaderEntryTestDriver();
//		BarcodeReader exitReader = new BarcodeReaderExitTestDriver();
//		PinCodeTerminal pinTerminal = new PinCodeTerminalTestDriver();
//		ElectronicLock entryLock = new ElectronicLockTestDriver("Ingång");
//		ElectronicLock exitLock = new ElectronicLockTestDriver("Utgång");
		
		DataBase database = new DataBase();
		BikeGarageGUI menu = new BikeGarageGUI(database, printer);

	}

}
