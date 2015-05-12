package gui.buttons;

import gui.BikeGarageGUI;
import gui.RegisterOwnerUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class RegisterButton extends JButton implements ActionListener{
	private BikeGarageGUI main;
	
	private String testNum = "1337"; // TEMP TO TEST
	
	public RegisterButton(BikeGarageGUI main){
		super("Registrera");
		this.main = main;
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
		} else if(!socSecNum.equals(testNum)){
			switch (JOptionPane.showConfirmDialog(null, "Är du säker på att du vill registrera en ny användare?")) {
			case JOptionPane.YES_OPTION:
				System.out.println("Registrera Användare");
//				cl.show(main.getUIPane(), BikeGarageGUI.REGOWNERPANE);
				RegisterOwnerUI regOwner = new RegisterOwnerUI(main, socSecNum);
				switch(JOptionPane.showConfirmDialog(null, regOwner, "Indata", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null)){
				case JOptionPane.OK_OPTION:
					System.out.println("Indata bekräftad");
					regOwner.addInDatabase();
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
			System.out.println("Registrera Cykel");
			cl.show(main.getUIPane(), BikeGarageGUI.REGBIKEPANE);				
		}
	}
	
	
}
