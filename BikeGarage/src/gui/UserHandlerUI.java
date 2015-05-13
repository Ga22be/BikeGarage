package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserHandlerUI extends JPanel{
	private BikeGarageGUI main;
	private List<String[]> owners;
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
	private String[] person1 = {"Ipsum Lores", "199608245648", "Ipsum.Lores@domain.se", "Avaktiverad", "56789", "Ute", "98765", "Inne"};
	private String[] person2 = {"Lores Ipsum", "199204134423", "Lores.Ipsum@domain.se", "Aktiverad", "12345", "Inne", "54321", "Ute"};
	private String[] person3 = {"", "", "", "", "", "", "", ""};
	private Object[] personer = {person1, person2, person3};
	

	public UserHandlerUI(BikeGarageGUI main){
		this.main = main;
		setLayout(new BorderLayout());
		currentOwner = 0;
		owners = new ArrayList<String[]>();
		
		
		setBackground(Color.GREEN); //DEBUG SETTTING
		
		//Search panel
		makeSearchPanel();
		add(searchPanel, BorderLayout.NORTH);
		
		//Users panel
		makeOwnersPanel();
		add(ownersPanel, BorderLayout.CENTER);
		setOwners();
		
		//Personal panel
		makePersonalPanel();
		add(personalPanel, BorderLayout.EAST);
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
		ownersList.addListSelectionListener(new ownerListListener());
		ownersList.setFont(BikeGarageGUI.LFONT);
		
		ownerScroll = new JScrollPane(ownersList);
		JLabel scrollHeader = new JLabel(String.format("%-20s %s", "Namn", "Personnummer"));
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
		
		userInfoArea = new JTextArea(7, 45);
		System.out.println(userInfoArea.getFont().getName());
		System.out.println(userInfoArea.getFont().getStyle());
		userInfoArea.setFont(BikeGarageGUI.TFONT);
		userInfoArea.setEditable(false);
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
		userInfoArea.append(String.format("%-20s %s %n", "Personnummer: ", addSeparator(person[counter++])));
		userInfoArea.append(String.format("%-20s %s %n %n %n", "Mail: ", person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n", "Tillgångstid: ", person[counter++]));
		
		bikeListModel.removeAllElements();
		while(counter+2 <= person.length){
			bikeListModel.addElement(String.format("%-20s %s", person[counter++], person[counter++]));
		}
		
	}
	
	private void setOwners(){
		//TODO Run getNames() in DB
		Set<String> namesTEMP = new TreeSet<String>(); //Returned from DB
		namesTEMP.add("Lores Ipsum-199204134423");
		namesTEMP.add("Ipsum Lores-199608245648");
		
		List<String> names = new LinkedList<String>();
		names.addAll(namesTEMP);
		
		
		ownersListModel.removeAllElements();
		Iterator<String> itr = names.iterator();
		while(itr.hasNext()){
			String[] temp = itr.next().split("-");
			for(int i = 0; i < 2; i++){
				System.out.print(temp[i] + ", ");
			}
			System.out.println();
			owners.add(temp);
			ownersListModel.addElement(String.format("%-20s %s", temp[0], temp[1]));
		}
		
	}

	private class bikeListListener implements ListSelectionListener{
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

	private class SearchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Add connection to DB
		}		
	}
	
	private class ShowAllListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Make method for adding all names in database
		}
	}
	
	private class RefreshListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO Add connection to DB
			setUserInfo(++currentOwner%2);
		}
	}
	
	
	private class UnregisterBikeListener implements ActionListener{
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
	
	private class ownerListListener implements ListSelectionListener{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			 if (e.getValueIsAdjusting() == false) {
				 
		            if (ownersList.getSelectedIndex() == -1) {
		            //No selection, empty user information.
		                setUserInfo(2);		 
		            } else {
		            //Selection, show info for selected user.
		            	System.out.println(owners.get(ownersList.getSelectedIndex())[1]);
		            	setUserInfo(ownersList.getSelectedIndex());
		            }
		        }
		}		
	}
	
	private String addSeparator(String socSecNum){
		return socSecNum.substring(0, 8) + "-" + socSecNum.substring(8, socSecNum.length());
	}
	
}
