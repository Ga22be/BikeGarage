package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserHandlerUI extends JPanel{
	private BikeGarageGUI main;
	private Set<String[]> names;
	private String[] person;
	private int currentOwner;
	
	//Search
	private JPanel searchPanel;
	private JTextField searchField;
	private JButton searchButton;
	private JButton showAllButton;
	
	//Users
	private JPanel ownersPanel;
	private JList<String> ownersList;
	private DefaultListModel<String> ownersListModel;
	private JScrollPane ownerScroll;
	
	private JPanel ownersButtonPanel;
	private JButton unregisterOwnerButton;
	
	//Personal
	private JPanel personalPanel;
	private JTextArea userInfoArea;
	private JScrollPane userScroll;
	
	private JList<String> bikeList;
	private DefaultListModel<String> bikeListModel;
	private JScrollPane bikeScroll;
	
	private JPanel personalButtonPanel;
	private JButton refreshButton;
	private JButton unregiserBikeButton;
	
	//TEMP
	private String[] person1 = {"Lores Ipsum", "19920413-4423", "Lores.Ipsum@domain.se", "Aktiverad", "12345", "Inne", "54321", "Ute"};
	private String[] person2 = {"Ipsum Lores", "19960824-5648", "Ipsum.Lores@domain.se", "Avaktiverad", "56789", "Ute", "98765", "Inne"};
	private Object[] personer = {person1, person2};
	
	public UserHandlerUI(BikeGarageGUI main){
		this.main = main;
		setLayout(new BorderLayout());
		currentOwner = 0;
		
		setBackground(Color.GREEN); //DEBUG SETTTING
		
		//Search panel
		makeSearchPanel();
		add(searchPanel, BorderLayout.NORTH);
		
		//Users panel
		makeOwnersPanel();
		add(ownersPanel, BorderLayout.CENTER);
		
		//Personal panel
		makePersonalPanel();
		add(personalPanel, BorderLayout.EAST);
		setUserInfo(currentOwner);	
	}
	
	/**
	 * Skapar panelen innehållande sökrutan och dess knapp
	 */
	private void makeSearchPanel(){
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
		searchButton.addActionListener(new SearchListener());
		showAllButton = new JButton("Visa alla");
		showAllButton.addActionListener(new ShowAllListener());
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(showAllButton);
	}
	
	/**
	 * Skapar panelen innehållande alla användare;
	 */
	private void makeOwnersPanel(){
		ownersPanel = new JPanel();
		ownersPanel.setLayout(new BoxLayout(ownersPanel, BoxLayout.Y_AXIS));
		ownersPanel.setBorder(new EmptyBorder(0,5,0,0));
		
		// Add ownersList
		// Kan getNames() ge ett sett av vektorer om två. Namn + personnummer
		ownersListModel = new DefaultListModel<String>();
		ownersList = new JList<String>(ownersListModel);
		ownersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ownersList.setSelectedIndex(0);
		ownersList.addListSelectionListener(new bikeListListener());
		ownersList.setFont(BikeGarageGUI.LFONT);
		
		ownerScroll = new JScrollPane(ownersList);
		JLabel scrollHeader = new JLabel(String.format("%-19s %s", "Namn", "Personnummer"));
		scrollHeader.setFont(new Font(null, Font.BOLD, 18));
		scrollHeader.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		ownerScroll.setColumnHeaderView(scrollHeader);
		ownerScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ownersPanel.add(ownerScroll);
			
	}
	/**
	 * Skapar panelen innnehållande all personlig information
	 */
	private void makePersonalPanel(){
		personalPanel = new JPanel();
		personalPanel.setLayout(new BoxLayout(personalPanel, BoxLayout.Y_AXIS));
		personalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Add personalInfoBox
		personalPanel.add(new JLabel("Personinformation"));
		personalPanel.getComponent(0).setFont(BikeGarageGUI.HFONT);;
		
		userInfoArea = new JTextArea(7, 46);
		System.out.println(userInfoArea.getFont().getName());
		System.out.println(userInfoArea.getFont().getStyle());
		userInfoArea.setFont(BikeGarageGUI.TFONT);
		userScroll = new JScrollPane(userInfoArea);
		userScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		personalPanel.add(userScroll);		
		personalPanel.add(Box.createVerticalStrut(10));
		
		personalPanel.add(new JLabel("CykelID"));
		personalPanel.getComponent(3).setFont(BikeGarageGUI.HFONT);
		
		// Add bikeID list
		bikeListModel = new DefaultListModel<String>();
		bikeList = new JList<String>(bikeListModel);
		bikeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bikeList.setSelectedIndex(0);
		bikeList.addListSelectionListener(new bikeListListener());
		bikeList.setFont(BikeGarageGUI.LFONT);
		
		
		bikeScroll = new JScrollPane(bikeList);
		JLabel scrollHeader = new JLabel(String.format("%-19s %s", "ID", "Status"));
		scrollHeader.setFont(new Font(null, Font.BOLD, 18));
		scrollHeader.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		bikeScroll.setColumnHeaderView(scrollHeader);
		bikeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		personalPanel.add(bikeScroll);
		
		personalButtonPanel = new JPanel();
		refreshButton = new JButton("Uppdatera");
		refreshButton.addActionListener(new RefreshListener());
		unregiserBikeButton = new JButton("Avregistrera cykel");
		unregiserBikeButton.addActionListener(new UnregisterBikeListener());
		unregiserBikeButton.setEnabled(false);
		personalButtonPanel.add(refreshButton);
		personalButtonPanel.add(unregiserBikeButton);
		personalPanel.add(personalButtonPanel);	
	}
	
	private void setUserInfo(int i){
		person = (String[])personer[i];
		int counter = 0;
		userInfoArea.setText("");
		userInfoArea.append(String.format("%-20s %s %n", "Namn: ", person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n", "Personnummer: ", person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n %n", "Mail: ", person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n", "Tillgångstid: ", person[counter++]));
		
		bikeListModel.removeAllElements();
		while(counter+2 <= person.length){
			bikeListModel.addElement(String.format("%-20s %s", person[counter++], person[counter++]));
		}
		
	}

	public class bikeListListener implements ListSelectionListener{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			 if (e.getValueIsAdjusting() == false) {
				 
		            if (bikeList.getSelectedIndex() == -1) {
		            //No selection, disable unregister button.
		                unregiserBikeButton.setEnabled(false);
		 
		            } else {
		            //Selection, enable the unregister button.
		                unregiserBikeButton.setEnabled(true);
		            }
		        }
		}		
	}

	public class SearchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Add connection to DB
		}		
	}
	
	public class ShowAllListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Make method for adding all names in database
		}
	}
	
	public class RefreshListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Add connection to DB
			setUserInfo(++currentOwner%2);
		}
	}
	
	
	public class UnregisterBikeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = bikeList.getSelectedIndex();
			String bikeToRemove = person[4+2*index];
			String socSecNum = person[1];
			System.out.println(socSecNum + ", " + bikeToRemove);
			//TODO Add connection to DB
			//TODO Add get owner again!
//			setUserInfo(currentUser%2);
		}
	}
	
}
