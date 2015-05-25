package gui;

import gui.buttons.RegisterButton;
import gui.buttons.UserHandlerButton;

import java.awt.*;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;

public class MainMenu extends JPanel {
	private BikeGarageGUI main;
	private JPanel centerPanel;
	private DataBase db;
	private UserHandlerUI handlerUI;
	private BarcodePrinter printer;

	/**
	 * Creates the panel, making it contain centered buttons
	 * 
	 * @param main
	 *            The main GUI component
	 * @param db
	 *            The database the GUI exchanges information with
	 * @param handlerUI
	 *            The panel containing the user handling interface
	 * @param printer
	 *            The printer the GUI prints barcodes with
	 */
	public MainMenu(BikeGarageGUI main, DataBase db, UserHandlerUI handlerUI,
			BarcodePrinter printer) {
		this.main = main;
		setLayout(new GridLayout(3, 3));
		this.db = db;
		this.handlerUI = handlerUI;
		this.printer = printer;

		centerPanel = new JPanel();

		centerPanel.add(new RegisterButton(main, db, printer));
		centerPanel.add(new UserHandlerButton(main, handlerUI));

		for (int i = 0; i < 4; i++) {
			add(new JPanel());
		}
		add(centerPanel);
		for (int i = 0; i < 3; i++) {
			add(new JPanel());
		}
	}

}
