package test;

import java.awt.GridLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestLayout {
    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        JButton button = new JButton("why");
        JPanel panel = new JPanel();
        JTextField field= new JTextField("Something");
        
        Container c = frame.getContentPane();
        c.setLayout(new GridLayout(3,3));//Devides the container in 3 rows and 1 column
        for(int i = 0; i < 4; i++){
        	c.add(new JPanel());
        }
        c.add(panel);	//Add in first row
        c.add(button);	//Add in second row
        c.add(panel);	//Add in first row
        
        for(int i = 0; i < 3; i++){
        	c.add(new JPanel());
        }
        frame.setSize(500, 300);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        }
}
