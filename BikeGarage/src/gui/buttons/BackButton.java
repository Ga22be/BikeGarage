package gui.buttons;

import gui.BikeGarageGUI;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BackButton extends JButton implements ActionListener{
	private BikeGarageGUI main;
	
	public BackButton(BikeGarageGUI main){
		super("Tillbaka");
		this.main = main;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout)main.getUIPane().getLayout();
		System.out.println("Tillbaka");
		main.printMessage("INFO GOES HERE");
		cl.show(main.getUIPane(), BikeGarageGUI.MENUPANE);		
	}
	
	
}
