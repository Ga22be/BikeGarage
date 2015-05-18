package gui.buttons;

import gui.BikeGarageGUI;
import gui.RegisterBikeUI;
import gui.RegisterOwnerUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import bikeGarageDatabase.DataBase;
import bikeGarageDatabase.UnavailableOperationException;

public class RegisterButton extends JButton implements ActionListener{
	private BikeGarageGUI main;
	private DataBase db;
	
	private String testNum = "1337"; // TEMP TO TEST
	
	public RegisterButton(BikeGarageGUI main, DataBase db){
		super("Registrera");
		this.main = main;
		this.db = db;
		addActionListener(this); 
		setPreferredSize(new Dimension(BikeGarageGUI.WIDTH / 3 - BikeGarageGUI.VERTICAL_PADDING, BikeGarageGUI.HEIGHT / 3 / 3 - BikeGarageGUI.HORIZONTAL_PADDING));
		setFont(new Font(getFont().getName(), Font.BOLD, 38));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String socSecNum = JOptionPane.showInputDialog(null, "Ange perssonnummer", "Personnummer", JOptionPane.PLAIN_MESSAGE);
		CardLayout cl = (CardLayout)main.getUIPane().getLayout();
		if(socSecNum == null || socSecNum.isEmpty()){
			// Do nothing
		} else if((socSecNum.matches("[0-9]+") && socSecNum.length() == 12)){		
			//TODO Find method for check
			 if(!socSecNum.equals(testNum)){
				// Owner registration
				switch (JOptionPane.showConfirmDialog(null, "Är du säker på att du vill registrera en ny användare?")) {
				case JOptionPane.YES_OPTION:
					System.out.println("Registrera Användare");
//				cl.show(main.getUIPane(), BikeGarageGUI.REGOWNERPANE);
					RegisterOwnerUI regOwner = new RegisterOwnerUI(main, socSecNum, db);
					switch(JOptionPane.showConfirmDialog(null, regOwner, "Indata", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null)){
					case JOptionPane.OK_OPTION:
						main.printMessage("Indata bekräftad");
						if(!regOwner.addInDatabase()){
							main.printErrorMessage("Vänligen fyll i alla personuppgifter och försök igen!");
							return;
						} else {
							//Done
							//TODO Add print of new bike
						}
						break;
					case JOptionPane.CANCEL_OPTION:
						break;
					}
					break;
				case JOptionPane.NO_OPTION:
					actionPerformed(e);
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
				}
			} else {
				//TODO Add connection to DB
				RegisterBikeUI regBike = new RegisterBikeUI(main, db, "199510247712", "Gabriel Sjöberg");
				JOptionPane.showConfirmDialog(null, regBike, "Indata", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
//				String[] user = db.getOwner(socSecNum);
//				RegisterBikeUI regBike = new RegisterBikeUI(main, db, user[1], user[0]);
//				try {
//					db.addBike(socSecNum);
//				} catch (NoSuchElementException e1) {
//					//BLI AV MED
//					main.printErrorMessage("No such what?");
//					e1.printStackTrace();
//				} catch (IllegalArgumentException e1) {
//					//VILL BLI AV MED
//					main.printErrorMessage("Illegal argument exception");
//					e1.printStackTrace();
//				} catch (UnavailableOperationException e1) {
//					main.printErrorMessage("Garagets totala kapacitet är uppnådd");
//					e1.printStackTrace();
//				}
//			cl.show(main.getUIPane(), BikeGarageGUI.REGBIKEPANE);				
			}
		} else {
			main.printErrorMessage("Felaktig inmatning. Vänligen ange ett korrekt personnummer och försök igen");
		}
	}	
}
