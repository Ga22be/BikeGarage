package gui;

import gui.buttons.RegisterButton;
import gui.buttons.UnregisterBikeButton;
import gui.buttons.UnregisterOwnerButton;
import gui.buttons.UserHandlerButton;

import java.awt.*;

import javax.swing.*;

import bikeGarageDatabase.DataBase;

public class MainMenu extends JPanel {
	private BikeGarageGUI main;
	private JPanel centerPanel;
	private DataBase db;
	private UserHandlerUI handlerUI;
	
	public MainMenu(BikeGarageGUI main, DataBase db, UserHandlerUI handlerUI){
		this.main = main;
		setLayout(new GridLayout(3,3));
		this.db = db;
		this.handlerUI = handlerUI;
		
		centerPanel = new JPanel();
//		centerPanel.setBackground(Color.RED); // DEBUG SETTING
		
		centerPanel.add(new RegisterButton(main, db));
		centerPanel.add(new UserHandlerButton(main, handlerUI));
		centerPanel.add(new UnregisterOwnerButton());
		centerPanel.add(new UnregisterBikeButton());
		
		for(int i = 0; i < 4; i++){
			add(new JPanel());
		}
		add(centerPanel);
		for(int i = 0; i < 3; i++){
			add(new JPanel());
		}
	}

}
