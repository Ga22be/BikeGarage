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
	
	//Search
	private JPanel searchPanel;
	private JTextField searchField;
	private JButton searchButton;
	
	//Result
	private JPanel resultPanel;
	private JTextArea userInfoArea;
	private JScrollPane userScroll;
//	private 
	
	public UserHandlerUI(BikeGarageGUI main){
		this.main = main;
		setLayout(new BorderLayout());
		
		setBackground(Color.GREEN); //DEBUG SETTTING
		
		//Search panel
		makeSearchPanel();
		add(searchPanel, BorderLayout.NORTH);
		
		//ResultPanel
		makeResultPanel();
		add(resultPanel, BorderLayout.WEST);
		
	}
	
	public void makeSearchPanel(){
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
	}
	
	public void makeResultPanel(){
		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.add(new JLabel("Personinformation"));
		userInfoArea = new JTextArea(0, 60);
		userScroll = new JScrollPane(userInfoArea);
		userScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultPanel.add(userScroll);
		resultPanel.add(Box.createVerticalStrut(10));
		
		
		
		
	}
}
