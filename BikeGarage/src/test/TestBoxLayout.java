package test;

import java.awt.BorderLayout;

import javax.swing.*;

public class TestBoxLayout {
	public static void main(String[] args){
		JFrame frame = new JFrame();
		JPanel outPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("text1"));
		panel.add(new JLabel("text2"));
		panel.add(new JLabel("text3"));
		outPanel.add(panel, BorderLayout.SOUTH);
		frame.add(outPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
