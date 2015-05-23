package gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ShutDownButton extends JButton implements ActionListener{
	
	/**
	 * Creates button to shutdown the system with
	 */
	public ShutDownButton(){
		super("Avsluta");
		addActionListener(this);
	}

	/**
	 * ActionListener for this button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);		
	}
	
	
}
