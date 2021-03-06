package gui;

import java.util.NoSuchElementException;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class RegisterBikeUI extends JPanel {
	private BikeGarageGUI main;
	private DataBase db;
	private String socSecNum;

	private JPanel namePanel;
	private JTextField nameField;
	private JPanel amountPanel;
	private JTextField bikeAmount;

	/**
	 * Creates panel showing interface for adding a new bike in the database
	 * 
	 * @param main
	 *            The main GUI component
	 * @param db
	 *            The database the GUI exchanges information with
	 * @param socSecNum
	 *            The social security number to use when adding bike to a
	 *            specific owner
	 * @param name
	 *            The name of the person to add the bike to
	 */
	public RegisterBikeUI(BikeGarageGUI main, DataBase db, String socSecNum,
			String name) {
		this.main = main;
		this.socSecNum = socSecNum;
		this.db = db;

		namePanel = new JPanel();
		namePanel.add(new JLabel("Namn: "));
		nameField = new JTextField(name, 24);
		nameField.setEditable(false);
		namePanel.add(nameField);

		bikeAmount = new JTextField("1", 3);
		amountPanel = new JPanel();
		amountPanel.add(new JLabel("L�gg till:"));
		amountPanel.add(bikeAmount);
		amountPanel.add(new JLabel("nya cyklar"));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(namePanel);
		add(Box.createVerticalStrut(10));
		add(amountPanel);
	}

	/**
	 * Method for adding the obtained amount of bikes to the previously
	 * specified owner
	 * 
	 * @param printer
	 *            The printer used to print barcodes
	 * @return true if bike added, otherwise false
	 */
	public boolean addInDatabase(BarcodePrinter printer) {
		String amount = bikeAmount.getText();
		int parsedAmount = Integer.parseInt(amount);
		if (!amount.isEmpty() && !amount.equals("0")
				&& amount.matches("[0-9]+") && amount.length() >= 1
				&& amount.length() <= 3) {
			int bikesInGarage = db.bikeCount();
			if (parsedAmount + bikesInGarage <= 500) {
				try {
					for (int i = 0; i < parsedAmount; i++) {
						db.addBike(socSecNum);
						String[] owner = db.getOwner(socSecNum);
						printer.printBarcode(owner[owner.length - 2]);
					}
				} catch (NoSuchElementException e) {
					// Icke-existerande personnummer
					main.printErrorMessage("No such what?");
					// e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// Felaktigt personnummer
					main.printErrorMessage("Illegal argument exception");
					// e.printStackTrace();
				} catch (UnavailableOperationException e) {
					main.printErrorMessage("Garagets totala kapacitet �r uppn�dd");
					// e.printStackTrace();
				}
				return true;
			} else {
				main.printErrorMessage("Garagets totala kapacitet �r uppn�dd. Det finns redan: "
						+ db.bikeCount() + " cyklar.");
			}
		} else {
			main.printErrorMessage("V�nligen fyll i ett tal mellan 1 och 499 och f�rs�k igen.");
		}
		return false;
	}

}
