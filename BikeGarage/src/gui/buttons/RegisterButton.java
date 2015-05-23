package gui.buttons;

import gui.BikeGarageGUI;
import gui.RegisterBikeUI;
import gui.RegisterOwnerUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import testDrivers.BarcodePrinter;
import bikeGarageDatabase.DataBase;

public class RegisterButton extends JButton implements ActionListener {
	private BikeGarageGUI main;
	private DataBase db;
	private BarcodePrinter printer;

	/**
	 * Creates button for registering new things in the database
	 * 
	 * @param main
	 *            The main GUI component
	 * @param db
	 *            The database the GUI exchanges information with
	 * @param printer
	 *            The printer the GUI prints barcodes with
	 */
	public RegisterButton(BikeGarageGUI main, DataBase db,
			BarcodePrinter printer) {
		super("Registrera");
		this.main = main;
		this.db = db;
		this.printer = printer;
		addActionListener(this);
		setPreferredSize(new Dimension(BikeGarageGUI.WIDTH / 3
				- BikeGarageGUI.VERTICAL_PADDING, BikeGarageGUI.HEIGHT / 3 / 3
				- BikeGarageGUI.HORIZONTAL_PADDING));
		setFont(new Font(getFont().getName(), Font.BOLD, 38));
	}

	/**
	 * ActionListener for this button, handling the input and guiding further in
	 * the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String socSecNum = JOptionPane
				.showInputDialog(null, "Ange perssonnummer", "Personnummer",
						JOptionPane.PLAIN_MESSAGE);
		CardLayout cl = (CardLayout) main.getUIPane().getLayout();
		if (socSecNum == null || socSecNum.isEmpty()) {
			// Do nothing
		} else if ((socSecNum.matches("[0-9]+") && socSecNum.length() == 12)) {
			if (!db.isOwner(socSecNum)) {
				// Owner registration
				switch (JOptionPane
						.showConfirmDialog(null,
								"Är du säker på att du vill registrera en ny användare?")) {
				case JOptionPane.YES_OPTION:
					System.out.println("Registrera Användare");
					// cl.show(main.getUIPane(), BikeGarageGUI.REGOWNERPANE);
					RegisterOwnerUI regOwner = new RegisterOwnerUI(main,
							socSecNum, db);
					switch (JOptionPane.showConfirmDialog(null, regOwner,
							"Indata", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null)) {
					case JOptionPane.OK_OPTION:
						main.printMessage("Indata bekräftad");
						if (!regOwner.addInDatabase(printer)) {
							main.printErrorMessage("Vänligen fyll i alla personuppgifter och försök igen.");
							return;
						} else {
							// Done
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
				String[] user = db.getOwner(socSecNum);
				RegisterBikeUI regBike = new RegisterBikeUI(main, db, user[1],
						user[0]);
				JOptionPane.showConfirmDialog(null, regBike, "Indata",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null);
				regBike.addInDatabase(printer);
			}
		} else {
			main.printErrorMessage("Felaktig inmatning. Vänligen ange ett korrekt personnummer och försök igen");
		}
	}
}
