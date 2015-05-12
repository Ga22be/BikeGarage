package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class UserHandlerUI extends JPanel {
	private BikeGarageGUI main;
	private JPanel searchPanel;
	private JTextField searchField;
	private JButton searchButton;
	private JPanel resutlBoxPanel;
	
	public UserHandlerUI(BikeGarageGUI main){
		this.main = main;
		setLayout(new BorderLayout());
		
		setBackground(Color.GREEN); //DEBUG SETTTING
		
		//Search panel
		makeSearchPanel();
		add(searchPanel, BorderLayout.NORTH);
		
		//ResultPanel
		
		
	}
	
	public void makeSearchPanel(){
//		JPanel searchPanel = new JPanel();
		searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchField = new JTextField("Ange sökterm...");
		searchField.setColumns(20);
		searchField.addMouseListener(new MouseAdapter(){		
			@Override
			public void mouseClicked(MouseEvent e){
				searchField.setText("");
			}
		});
		searchButton = new JButton("Sök");
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
//		return searchPanel;
	}
	
	public void makeResultPanel(){
		JTextPane resultField = new JTextPane();
		
	}
}
