package gui.buttons;

import gui.BikeGarageGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UserHandlerButton extends JButton implements ActionListener{
	
	public UserHandlerButton(){
		super("Hantera Användare");
		addActionListener(this);
		setPreferredSize(new Dimension(BikeGarageGUI.WIDTH / 3 - BikeGarageGUI.VERTICAL_PADDING, BikeGarageGUI.HEIGHT / 3 / 3 - BikeGarageGUI.HORIZONTAL_PADDING));
		setFont(new Font(getFont().getName(), Font.BOLD, 19));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	
}
