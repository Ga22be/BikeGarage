package gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ShutDownButton extends JButton implements ActionListener{
	
	public ShutDownButton(){
		super("Avsluta");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);		
	}
	
	
}
