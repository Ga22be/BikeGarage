package gui;

import gui.buttons.BackButton;
import gui.buttons.ShutDownButton;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;

public class BikeGarageGUI extends JFrame{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int VERTICAL_PADDING = 6;
	public static final int HORIZONTAL_PADDING = 12;
	
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
	
	public BikeGarageGUI(DataBase db, BarcodePrinter printer){
		super("BikeGarage");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);		
		setLayout(new BorderLayout());
		this.db = db;
		this.printer = printer;
		
		try {
//		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		
		uHandler = new UserHandlerUI(this, db, printer);
		menu = new MainMenu(this, db, uHandler, printer);
//		regOwnerUI = new RegisterOwnerUI(this);
//		regBikeUI = new RegisterBikeUI(this, db);
		unregOwner = new UnregisterOwner(this);
		unregBike = new UnregisterBike(this);
		
		uiPane = new JPanel(new CardLayout());
		uiPane.add(menu, MENUPANE);
//		uiPane.add(regOwnerUI, REGOWNERPANE);
//		uiPane.add(regBikeUI, REGBIKEPANE);
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
		
		((CardLayout)uiPane.getLayout()).show(uiPane, MENUPANE);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public JPanel getUIPane(){
		return uiPane;
	}

	private void printMessage(String mes, Color col){
		infoBox.setForeground(col);
		infoBox.setText(mes);
	}
	
	public void printMessage(String mes){
		printMessage(mes, Color.BLACK);
	}
	
	public void printErrorMessage(String mes){
		printMessage(mes, Color.RED);
	}

}
