package gui.buttons;

import gui.BikeGarageGUI;
import gui.UserHandlerUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UserHandlerButton extends JButton implements ActionListener{
	private BikeGarageGUI main;
	private UserHandlerUI handlerUI;
	
	/**
	 * Creates button linking further in the GUI
	 * @param main The main GUI component
	 * @param handlerUI The panel containing the user handling interface
	 */
	public UserHandlerButton(BikeGarageGUI main, UserHandlerUI handlerUI){
		super("Hantera Användare");
		this.main = main;
		this.handlerUI = handlerUI;
		addActionListener(this);
		setPreferredSize(new Dimension(BikeGarageGUI.WIDTH / 3 - BikeGarageGUI.VERTICAL_PADDING, BikeGarageGUI.HEIGHT / 3 / 3 - BikeGarageGUI.HORIZONTAL_PADDING));
		setFont(new Font(getFont().getName(), Font.BOLD, 19));
	}
	
	/**
	 * ActionListener for this button 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout)main.getUIPane().getLayout();
		handlerUI.setOwners();
		cl.show(main.getUIPane(), BikeGarageGUI.HANDLERPANE);
		
	}
	
	
}
