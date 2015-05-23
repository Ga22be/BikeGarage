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

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class UserHandlerUI extends JPanel {
	private BikeGarageGUI main;
	private List<String[]> owners;
	private String[] person;
	private DataBase db;
	private int currentOwner;
	private BarcodePrinter printer;

	// Search
	private JPanel searchPanel;
	private JTextField searchField;
	private JButton searchButton;
	private JButton showAllButton;

	// Owners
	private JPanel ownersPanel;
	private JList<String> ownersList;
	private DefaultListModel<String> ownersListModel;
	private JScrollPane ownerScroll;

	private JPanel ownersButtonPanel;
	private JButton unregisterOwnerButton;

	// Personal
	private JPanel personalPanel;
	private JTextArea userInfoArea;
	private JScrollPane userScroll;

	private JList<String> bikeList;
	private DefaultListModel<String> bikeListModel;
	private JScrollPane bikeScroll;

	private JPanel personalButtonPanel;
	private JButton refreshButton;
	private JButton printBarcodeButton;
	private JButton unregiserBikeButton;

	public UserHandlerUI(BikeGarageGUI main, DataBase db, BarcodePrinter printer) {
		this.main = main;
		this.printer = printer;
		setLayout(new BorderLayout());
		currentOwner = 0;
		owners = new ArrayList<String[]>();
		this.db = db;

		setBackground(Color.GREEN); // DEBUG SETTTING

		// Search panel
		makeSearchPanel();
		add(searchPanel, BorderLayout.NORTH);

		// Personal panel
		makePersonalPanel();
		add(personalPanel, BorderLayout.EAST);

		// Users panel
		makeOwnersPanel();
		add(ownersPanel, BorderLayout.CENTER);
		setOwners();
	}

	/**
	 * Creates panel containing the search field, search button and showAll
	 * button
	 */
	private void makeSearchPanel() {
		searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchField = new JTextField("Ange sökterm...");
		searchField.setColumns(20);
		searchField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
	 * Creates panel showing list of users and button for user removal
	 */
	private void makeOwnersPanel() {
		ownersPanel = new JPanel();
		ownersPanel.setLayout(new BoxLayout(ownersPanel, BoxLayout.Y_AXIS));
		ownersPanel.setBorder(new EmptyBorder(0, 5, 0, 0));

		// Add ownersList
		ownersListModel = new DefaultListModel<String>();
		ownersList = new JList<String>(ownersListModel);
		ownersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ownersList.setSelectedIndex(0);
		ownersList.addListSelectionListener(new ownerListListener());
		ownersList.setFont(BikeGarageGUI.LFONT);
		ownersList.setVisibleRowCount(18);

		ownerScroll = new JScrollPane(ownersList);
		JLabel scrollHeader = new JLabel("Namn");
		scrollHeader.setFont(new Font(null, Font.BOLD, 18));
		scrollHeader.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		ownerScroll.setColumnHeaderView(scrollHeader);
		ownerScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ownersPanel.add(ownerScroll);

		ownersButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		unregisterOwnerButton = new JButton(
				"<html><center>Ta bort<br/>Cykelägare</center></html>");
		unregisterOwnerButton.addActionListener(new UnregisterOwnerListener());
		unregisterOwnerButton.setEnabled(false);
		ownersButtonPanel.add(unregisterOwnerButton);
		ownersPanel.add(ownersButtonPanel);

	}

	/**
	 * Creates panel containing information about selected user and some buttons
	 * to manipulate this user.
	 */
	private void makePersonalPanel() {
		personalPanel = new JPanel();
		personalPanel.setLayout(new BoxLayout(personalPanel, BoxLayout.Y_AXIS));
		personalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Add personalInfoBox
		personalPanel.add(new JLabel("Personinformation"));
		personalPanel.getComponent(0).setFont(BikeGarageGUI.HFONT);
		;

		userInfoArea = new JTextArea(8, 48);
		userInfoArea.setFont(BikeGarageGUI.TFONT);
		userInfoArea.setEditable(false);
		userScroll = new JScrollPane(userInfoArea);
		userScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		personalPanel.add(userScroll);
		personalPanel.add(Box.createVerticalStrut(16));

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
		JLabel scrollHeader = new JLabel(String.format("%-19s %s", "ID",
				"Status"));
		scrollHeader.setFont(new Font(null, Font.BOLD, 18));
		scrollHeader.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		bikeScroll.setColumnHeaderView(scrollHeader);
		bikeScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		personalPanel.add(bikeScroll);

		// Add buttons
		personalButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		refreshButton = new JButton("Uppdatera");
		refreshButton.addActionListener(new RefreshListener());
		refreshButton.setEnabled(false);
		printBarcodeButton = new JButton("Skriv ut streckkod");
		printBarcodeButton.addActionListener(new PrintBarcodeListener());
		printBarcodeButton.setEnabled(false);
		unregiserBikeButton = new JButton("Avregistrera cykel");
		unregiserBikeButton.addActionListener(new UnregisterBikeListener());
		unregiserBikeButton.setEnabled(false);
		personalButtonPanel.add(refreshButton);
		personalButtonPanel.add(printBarcodeButton);
		personalButtonPanel.add(unregiserBikeButton);
		personalPanel.add(personalButtonPanel);
	}

	/**
	 * Shows all users in the database in the list
	 */
	public void setOwners() {
		setOwners(null);
	}

	/**
	 * Adds a "-" separator in a 12-digit social security number
	 * 
	 * @param socSecNum
	 *            The number to split
	 * @return The number with separator
	 */
	private String addSeparator(String socSecNum) {
		return socSecNum.substring(0, 8) + "-"
				+ socSecNum.substring(8, socSecNum.length());
	}

	/**
	 * Shows users in the owners list depending on the arguments
	 * 
	 * @param id
	 *            If only one person is to be shown, what's their social
	 *            security number or a bike they are registered with. If all
	 *            users are supposed to be showed, set as null.
	 * @return true if an owner was successfully added in the database,
	 *         otherwise false.
	 */
	private boolean setOwners(String id) {
		clearUserInfo();
		ownersListModel.removeAllElements();
		owners.clear();
		boolean result = false;
		if (id == null) {
			Set<String> names = db.getNames();
			if (!names.isEmpty()) {
				Iterator<String> itr = names.iterator();
				while (itr.hasNext()) {
					String[] temp = itr.next().split("-");
					owners.add(temp);
					ownersListModel.addElement(temp[0]);
					result = true;
				}
				ownersList.setSelectedIndex(-1);
			}
		} else {
			try {
				String[] person = db.getOwner(id);
				owners.add(person);
				ownersListModel.addElement(person[0]);
				ownersList.setSelectedIndex(0);
				result = true;
			} catch (NoSuchElementException e) {
				if (id.length() == 12) {
					main.printErrorMessage("Det finns ingen användare associerad med det angivna personnummret.");
				} else {
					main.printErrorMessage("Det finns ingen användare associerad med det angivna cykelIDt.");
				}
			}
		}
		return result;
	}

	/**
	 * Sets the information in the inforamtion fields
	 * 
	 * @param i
	 *            The index of the owner in the internal list to be shown
	 */
	private void setUserInfo(int i) {
		person = db.getOwner(owners.get(i)[1]);
		int counter = 0;
		userInfoArea.setText("");
		userInfoArea.append(String.format("%-20s %s %n", "Namn: ",
				person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n", "Personnummer: ",
				addSeparator(person[counter++])));
		userInfoArea.append(String.format("%-20s %s %n", "Mail: ",
				person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n %n", "Pin: ",
				person[counter++]));
		userInfoArea.append(String.format("%-20s %s %n", "Tillgångstid: ",
				person[counter++]));

		bikeListModel.removeAllElements();
		while (counter + 2 <= person.length) {
			bikeListModel.addElement(String.format("%-20s %s",
					person[counter++], person[counter++]));
		}

	}

	/**
	 * Clears the information fields
	 */
	private void clearUserInfo() {
		userInfoArea.setText("");
		bikeListModel.removeAllElements();
	}

	/**
	 * Listener for the bikeList
	 * 
	 * @author Gabbe
	 * 
	 */
	private class bikeListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				if (bikeList.getSelectedIndex() == -1) {
					// No selection, disable unregister and print button.
					unregiserBikeButton.setEnabled(false);
					printBarcodeButton.setEnabled(false);
				} else {
					// Selection, enable the unregister and print button.
					unregiserBikeButton.setEnabled(true);
					printBarcodeButton.setEnabled(true);
				}
			}
		}
	}

	/**
	 * Listener for the search button
	 * 
	 * @author Gabbe
	 *
	 */
	private class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String query = searchField.getText();
			if (!query.isEmpty()
					&& (query.matches("[0-9]+") && (query.length() == 5 || query
							.length() == 12))) {
				if (query.length() == 5) {
					main.printMessage("Sökte efter cykelID: " + query);
					if (setOwners(query)) {
						setUserInfo(currentOwner);
					}
				} else {
					main.printMessage("Sökte efter personnummer: " + query);
					if (setOwners(query)) {
						setUserInfo(currentOwner);
					}
				}
			} else {
				main.printErrorMessage("Felaktig inmatning. Vänligen ange antingen ett cykelID eller ett personnummer och försök igen");
			}

		}
	}

	/**
	 * Listener for the showAll button
	 * 
	 * @author Gabbe
	 *
	 */
	private class ShowAllListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setOwners();
		}
	}

	/**
	 * Listener for the refreshButton
	 * 
	 * @author Gabbe
	 *
	 */
	private class RefreshListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setUserInfo(currentOwner);
		}
	}

	/**
	 * Listener for the printBarcode button
	 * 
	 * @author Gabbe
	 *
	 */
	private class PrintBarcodeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = bikeList.getSelectedIndex();
			String bike = person[5 + 2 * index];
			main.printMessage("Skrev ut streckkod för cykel: " + bike);
			printer.printBarcode(bike);
		}
	}

	/**
	 * Listener for the unregisterBike button
	 * 
	 * @author Gabbe
	 *
	 */
	private class UnregisterBikeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(null,
					"Är du säker på att du vill avregistrera cykeln?") == JOptionPane.OK_OPTION) {
				int index = bikeList.getSelectedIndex();
				String bikeToRemove = person[5 + 2 * index];
				String socSecNum = person[1];
				main.printMessage("Avregistrerade cykelID: " + bikeToRemove);
				// System.out.println(socSecNum + ", " + bikeToRemove);
				try {
					db.removeBike(socSecNum, bikeToRemove);
				} catch (NoSuchElementException e1) {
					// If nonexisting
					main.printErrorMessage("No such what?");
					// e1.printStackTrace();
				} catch (UnavailableOperationException e1) {
					// Om cykel i garage
					main.printErrorMessage("Vänligen checka ut cykel ur garaget innan den kan avregistreras.");
					// e1.printStackTrace();
				}
				try {
					person = db.getOwner(socSecNum);
					setUserInfo(currentOwner);
				} catch (NoSuchElementException e2) {
					setOwners();
				}
			}
		}
	}

	/**
	 * Listener for the ownersList
	 * 
	 * @author Gabbe
	 *
	 */
	private class ownerListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				if (ownersList.getSelectedIndex() == -1) {
					// No selection, empty user information.
					unregisterOwnerButton.setEnabled(false);
					refreshButton.setEnabled(false);
				} else {
					// Selection, show info for selected user.
					unregisterOwnerButton.setEnabled(true);
					refreshButton.setEnabled(true);
					currentOwner = ownersList.getSelectedIndex();
					setUserInfo(currentOwner);
				}
			}
		}
	}

	/**
	 * Listener for the unregisterOwner button
	 * 
	 * @author Gabbe
	 *
	 */
	private class UnregisterOwnerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(null,
					"Är du säker på att du vill avregistrera denna användare?") == JOptionPane.OK_OPTION) {
				String socSecNum = owners.get(ownersList.getSelectedIndex())[1];
				main.printMessage("Tog bort: " + socSecNum);
				try {
					db.removeBikeOwner(socSecNum);
				} catch (NoSuchElementException e1) {
					// Nonexisting user
					main.printErrorMessage("No such what?");
					// e1.printStackTrace();
				} catch (UnavailableOperationException e1) {
					// If not all bikes checked out
					main.printErrorMessage("Vänligen checka ut alla cykelägarens cyklar innan den kan avregistreras.");
					// e1.printStackTrace();
				}
				setOwners();
			}
		}
	}

}
