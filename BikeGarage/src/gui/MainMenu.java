package gui;

import gui.buttons.RegisterButton;
import gui.buttons.UnregisterBikeButton;
import gui.buttons.UnregisterOwnerButton;
import gui.buttons.UserHandlerButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class MainMenu extends JPanel {
	private BikeGarageGUI main;
	private JPanel centerPanel;
	
	public MainMenu(BikeGarageGUI main){
		this.main = main;
		setLayout(new GridLayout(3,3));
		
		centerPanel = new JPanel();
//		centerPanel.setBackground(Color.RED); // DEBUG SETTING
		
		centerPanel.add(new RegisterButton(main));
		centerPanel.add(new UserHandlerButton(main));
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
