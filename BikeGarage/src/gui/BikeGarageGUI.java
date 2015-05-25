package gui;

import gui.buttons.BackButton;
import gui.buttons.ShutDownButton;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;

public class BikeGarageGUI extends JFrame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int HORIZONTAL_PADDING = 12;
	public static final int VERTICAL_PADDING = 20;

	public static final String MENUPANE = "menu";
	public static final String REGOWNERPANE = "registerOwner";
	public static final String REGBIKEPANE = "registerBike";
	public static final String HANDLERPANE = "handler";
	public static final String UNREGOWNERPANE = "unregOwner";
	public static final String UNREGBIKEPANE = "unregBike";

	public static final Font HFONT = new Font(null, Font.BOLD, 22);
	public static final Font TFONT = new Font("Monospaced", Font.BOLD, 16);
	public static final Font LFONT = new Font(null, Font.BOLD, 16);

	private DataBase db;
	private MainMenu menu;
	private RegisterOwnerUI regOwnerUI;
	private RegisterBikeUI regBikeUI;
	private UserHandlerUI uHandler;
	private UnregisterOwner unregOwner;
	private UnregisterBike unregBike;

	private BarcodePrinter printer;

	private JPanel uiPane;
	private JPanel bottomPane;
	private JTextField infoBox;

	/**
	 * Constructor creating and showing the GUI
	 * 
	 * @param db
	 *            The database the GUI will exchange information with
	 * @param printer
	 *            The printer the different GUI components will print barcodes
	 *            with
	 */
	public BikeGarageGUI(DataBase db, BarcodePrinter printer) {
		super("BikeGarage");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLayout(new BorderLayout());
		this.db = db;
		this.printer = printer;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		uHandler = new UserHandlerUI(this, db, printer);
		menu = new MainMenu(this, db, uHandler, printer);
		unregOwner = new UnregisterOwner(this);
		unregBike = new UnregisterBike(this);

		uiPane = new JPanel(new CardLayout());
		uiPane.add(menu, MENUPANE);
		uiPane.add(uHandler, HANDLERPANE);
		uiPane.add(unregOwner, UNREGOWNERPANE);
		uiPane.add(unregBike, UNREGBIKEPANE);

		bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());

		infoBox = new JTextField("INFO GOES HERE");
		infoBox.setEditable(false);

		bottomPane.add(infoBox, BorderLayout.CENTER);
		bottomPane.add(new BackButton(this), BorderLayout.WEST);
		bottomPane.add(new ShutDownButton(), BorderLayout.EAST);

		add(uiPane, BorderLayout.CENTER);
		add(bottomPane, BorderLayout.SOUTH);

		((CardLayout) uiPane.getLayout()).show(uiPane, MENUPANE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/**
	 * Returns the panel containing the cardlayout allowing switches between
	 * main-panels
	 * 
	 * @return Returns the panel containing the cardlayout
	 */
	public JPanel getUIPane() {
		return uiPane;
	}

	private void printMessage(String mes, Color col) {
		infoBox.setForeground(col);
		infoBox.setText(mes);
	}

	/**
	 * Prints message at the bottom of the frame
	 * 
	 * @param mes
	 *            The message to be printed
	 */
	public void printMessage(String mes) {
		printMessage(mes, Color.BLACK);
	}

	/**
	 * Prints error message at the bottom of the frame
	 * 
	 * @param mes
	 *            The error message to be printed
	 */
	public void printErrorMessage(String mes) {
		printMessage(mes, Color.RED);
	}

}
