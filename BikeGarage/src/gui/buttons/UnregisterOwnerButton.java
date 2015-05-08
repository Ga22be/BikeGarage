package gui.buttons;

import gui.BikeGarageGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UnregisterOwnerButton extends JButton implements ActionListener{
	
	public UnregisterOwnerButton(){
		super("<html><center> Avregistrera<br/>Anv�ndare </center></html>");
		addActionListener(this);
		setPreferredSize(new Dimension(BikeGarageGUI.WIDTH / 3 / 2 - 6, BikeGarageGUI.HEIGHT / 3 / 3 - BikeGarageGUI.HORIZONTAL_PADDING));
		setFont(new Font(getFont().getName(), Font.BOLD, 16));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	
}
