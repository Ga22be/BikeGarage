package system;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import bikeGarageDatabase.DataBase;
import testDrivers.ElectronicLock;
import testDrivers.PinCodeTerminal;

public class IdentificationService {
	private DataBase db;

	private ElectronicLock entryLock;
	private ElectronicLock exitLock;
	private PinCodeTerminal terminal;

	private StringBuilder pin;
	private long startTime;

	public IdentificationService(DataBase db) {
		this.db = db;
		pin = new StringBuilder();
	}

	/*
	 * Register hardware so that IdentificationService knows which drivers to
	 * access.
	 */
	public void registerHardwareDrivers(ElectronicLock entryLock,
			ElectronicLock exitLock, PinCodeTerminal terminal) {
		this.entryLock = entryLock;
		this.exitLock = exitLock;
		this.terminal = terminal;
	}

	/*
	 * Will be called when a user has used the bar code reader at the entry
	 * door. Bicycle ID should be a string of 5 characters, where every
	 * character can be '0', '1',... '9'.
	 */
	public void entryBarcode(String bicycleID) {
		if (db.checkIn(Integer.parseInt(bicycleID))) {
			entryLock.open(10);
			terminal.lightLED(PinCodeTerminal.GREEN_LED, 10);
		} else {
			terminal.lightLED(PinCodeTerminal.RED_LED, 3);
		}
	}

	/*
	 * Will be called when a user has used the bar code reader at the exit door.
	 * Bicycle ID should be a string of 5 characters, where every character can
	 * be '0', '1',... '9'.
	 */
	public void exitBarcode(String bicycleID) {
		if (db.checkOut(Integer.parseInt(bicycleID))) {
			exitLock.open(10);
		}
	}

	/*
	 * Will be called when a user has pressed a key at the keypad at the entry
	 * door. The following characters could be pressed: '0', '1',... '9', '*',
	 * '#'. A '#' restarts the session and '*' elongates the timeout
	 */
	public void entryCharacter(char c) {
		long tempTime = System.currentTimeMillis();
		System.out.println(db.bikeCount());
		// Reset, new PIN
		if (c == '#') {
			pin = new StringBuilder();
			startTime = 0;
			return;
		} else if (c == '*') {
			// Prolong input time
			startTime = tempTime;
			return;
		} else {
			if (startTime == 0 || tempTime - startTime < 3000) {
//				pin.append(c);
				pin.append("" + c);
			} else {
				pin = new StringBuilder();
				pin.append("" + c);
			}
			startTime = tempTime;
			if (pin.length() == 4) {
				if (db.checkIn(Integer.parseInt(pin.toString()))) {
					terminal.lightLED(PinCodeTerminal.GREEN_LED, 10);
					entryLock.open(10);
					startTime = 0;
				} else {
					terminal.lightLED(PinCodeTerminal.RED_LED, 3);
					startTime = 0;
				}
				pin = new StringBuilder();
				return;
			}
		}
		System.out.println(pin.toString());
	}
}
