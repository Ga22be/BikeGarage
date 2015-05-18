package gui;

import gui.buttons.BackButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.NoSuchElementException;

import javax.swing.*;

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

	public RegisterBikeUI(BikeGarageGUI main, DataBase db, String socSecNum,
			String name) {
		this.main = main;
		this.socSecNum = socSecNum;

		namePanel = new JPanel();
		namePanel.add(new JLabel("Namn: "));
		nameField = new JTextField(name, 24);
		nameField.setEditable(false);
		namePanel.add(nameField);

		bikeAmount = new JTextField("1", 3);
		amountPanel = new JPanel();
		amountPanel.add(new JLabel("Lägg till:"));
		amountPanel.add(bikeAmount);
		amountPanel.add(new JLabel("nya cyklar"));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(namePanel);
		add(Box.createVerticalStrut(10));
		add(amountPanel);
	}

	public boolean addInDatabase() {
		String amount = bikeAmount.getText();
		if (!amount.isEmpty() && amount.equals("0") && amount.matches("[0-9]+")
				&& amount.length() >= 1 && amount.length() < 3) {
			try {
				for (int i = 0; i < Integer.parseInt(amount); i++) {
					db.addBike(socSecNum);
				}
			} catch (NoSuchElementException e) {
				// Icke-existerande personnummer
				main.printErrorMessage("No such what?");
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// Felaktigt personnummer
				main.printErrorMessage("Illegal argument exception");
				e.printStackTrace();
			} catch (UnavailableOperationException e) {
				main.printErrorMessage("Garagets totala kapacitet är uppnådd");
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
