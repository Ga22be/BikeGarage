package gui;

import gui.buttons.BackButton;

import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.DatabaseMetaData;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class RegisterOwnerUI extends JPanel{
	private BikeGarageGUI main;
	private DataBase db;
	
	private JTextField socSecField;
	private JTextField nameField; 
	private JTextField mailField; 
		
	public RegisterOwnerUI(BikeGarageGUI main, String socSecNum, DataBase db){
		this.main = main;
		this.db = db;
//		setBackground(Color.PINK);
		
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
	
	public boolean addInDatabase(BarcodePrinter printer){
		if(nameField.getText().isEmpty() || mailField.getText().isEmpty()){
			return false;
		}
		String name = nameField.getText();
		String socSecNum = socSecField.getText();
		String mail = mailField.getText();
		main.printMessage("Lade till användare med uppgifterna: Personnummer: " + socSecNum + ", Namn: " + name + ", Mail: " + mail);
		//TODO Add connector to DB
		if(db.bikeCount() + 1 <= 500){
			try {
				db.addBikeOwner(name, socSecNum, mail);
				printer.printBarcode("" + db.addBike(socSecNum));
				JOptionPane.showMessageDialog(null, "Den nya användarens PIN är: " + db.getOwner(socSecNum)[3]);
				return true;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				main.printErrorMessage("En person med detta personnummer finns redan registrerad");
			} catch (UnavailableOperationException e) {
				e.printStackTrace();
				main.printErrorMessage("Garagets totala kapacitet är uppnådd");
			}			
		} else {
			main.printErrorMessage("Garagets totala kapacitet är uppnådd");
		}
		return false;
	}

}
