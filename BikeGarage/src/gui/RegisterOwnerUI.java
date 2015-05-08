package gui;

import gui.buttons.BackButton;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class RegisterOwnerUI extends JPanel{
	private BikeGarageGUI main;
	
	private JTextField socSecField;
	private JTextField nameField; 
	private JTextField mailField; 
		
	public RegisterOwnerUI(BikeGarageGUI main, String socSecNum){
		this.main = main;
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
	
	//TODO Add connector to DB
	public boolean addInDatabase(){
		if(nameField.getText().isEmpty() || mailField.getText().isEmpty()){
			throw new IllegalArgumentException("Fyll i både namn och mejl");
		}
		System.out.println("Personnummer: " + socSecField.getText() + ", Namn: " + nameField.getText() + ", Mail: " + mailField.getText());
		return true;
	}

}
