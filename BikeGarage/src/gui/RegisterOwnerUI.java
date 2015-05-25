package gui;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class RegisterOwnerUI extends JPanel {
	private BikeGarageGUI main;
	private DataBase db;

	private JTextField socSecField;
	private JTextField nameField;
	private JTextField mailField;

	/**
	 * Creates panel showing interface for adding a new owner in the database
	 * 
	 * @param main
	 *            The main GUI component
	 * @param socSecNum
	 *            The previously specified social security number of the new
	 *            owner
	 * @param db
	 *            The database the GUI exchanges information with
	 */
	public RegisterOwnerUI(BikeGarageGUI main, String socSecNum, DataBase db) {
		this.main = main;
		this.db = db;

		socSecField = new JTextField(socSecNum);
		socSecField.setEditable(false);
		nameField = new JTextField(5);
		mailField = new JTextField(5);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Personnummer"));
		add(socSecField);
		add(Box.createVerticalStrut(10));
		add(new JLabel("Namn"));
		add(nameField);
		add(Box.createVerticalStrut(10));
		add(new JLabel("Mail"));
		add(mailField);
	}

	/**
	 * Method for adding the obtained personal information to a new owner in the
	 * database
	 * 
	 * @param printer
	 *            The printer used to print new barcodes
	 * @return true if succesfull add in the database, otherwise false
	 */
	public boolean addInDatabase(BarcodePrinter printer) {
		if (nameField.getText().isEmpty() || mailField.getText().isEmpty()) {
			return false;
		}
		String name = nameField.getText();
		String socSecNum = socSecField.getText();
		String mail = mailField.getText();
		main.printMessage("Lade till användare med uppgifterna: Personnummer: "
				+ socSecNum + ", Namn: " + name + ", Mail: " + mail);
		if (db.bikeCount() + 1 <= 500) {
			try {
				db.addBikeOwner(name, socSecNum, mail);
				printer.printBarcode("" + db.addBike(socSecNum));
				JOptionPane.showMessageDialog(null,
						"Den nya användarens PIN är: "
								+ db.getOwner(socSecNum)[3]);
				return true;
			} catch (IllegalArgumentException e) {
				main.printErrorMessage("En person med detta personnummer finns redan registrerad");
			} catch (UnavailableOperationException e) {
				main.printErrorMessage("Garagets totala kapacitet är uppnådd");
			}
		} else {
			main.printErrorMessage("Garagets totala kapacitet är uppnådd");
		}
		return true;
	}

}
