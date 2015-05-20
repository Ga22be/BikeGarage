package system;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
		String fileName = "testFile";
		
		DataBase db;
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					fileName));
			db = (DataBase) in.readObject();
		} catch (Exception e) {
			db = new DataBase(fileName);
		}
		
		IdentificationService manager = new IdentificationService(db);
		BarcodePrinter printer = new BarcodePrinterTestDriver();
		BarcodeReader entryReader = new BarcodeReaderEntryTestDriver();
		entryReader.register(manager);
		BarcodeReader exitReader = new BarcodeReaderExitTestDriver();
		exitReader.register(manager);
		PinCodeTerminal pinTerminal = new PinCodeTerminalTestDriver();
		pinTerminal.register(manager);
		ElectronicLock entryLock = new ElectronicLockTestDriver("Ing�ng");
		ElectronicLock exitLock = new ElectronicLockTestDriver("Utg�ng");
		manager.registerHardwareDrivers(entryLock, exitLock, pinTerminal);
		
		BikeGarageGUI garageGUI = new BikeGarageGUI(db, printer);

	}

}
