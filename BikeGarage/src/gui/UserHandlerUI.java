package gui;

import java.awt.BorderLayout;

import javax.swing.*;

public class UserHandlerUI extends JPanel {
	private BikeGarageGUI main;
	private JPanel searchPanel;
	private JTextField searchBox;
	private JPanel resutlBoxPanel;
	
	public UserHandlerUI(BikeGarageGUI main){
		this.main = main;
		setLayout(new BorderLayout());
		
		searchPanel = new JPanel();
		
		
	}
}
