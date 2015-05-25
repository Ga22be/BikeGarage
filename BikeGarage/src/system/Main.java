package system;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JOptionPane;

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

public class Main {

	public static void main(String[] args) {
		String fileName = JOptionPane.showInputDialog(null, "Vilken sparfil vill du öppna?");
		DataBase db;
		if(fileName == null){
			
		} else if (!fileName.isEmpty()){
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
			ElectronicLock entryLock = new ElectronicLockTestDriver("Ingång");
			ElectronicLock exitLock = new ElectronicLockTestDriver("Utgång");
			manager.registerHardwareDrivers(entryLock, exitLock, pinTerminal);
			
			BikeGarageGUI garageGUI = new BikeGarageGUI(db, printer);
			
		} else {
			JOptionPane.showMessageDialog(null, "Vänligen ange ett giltigt filnamn");
			System.exit(0);
		}
		
		

	}

}
