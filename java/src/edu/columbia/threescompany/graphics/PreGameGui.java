package edu.columbia.threescompany.graphics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PreGameGui extends JFrame {

	private static final long serialVersionUID = -4557239773540965286L;

	public PreGameGui() {
		JPanel _mainpane = (JPanel)this.getContentPane();
		_mainpane.setLayout(new GridLayout(4, 1));
		
		_mainpane.add(new JLabel("Blobs"));
		
		JButton hotSeat = new JButton("Hotseat");
		hotSeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new HotSeatGui();
				dispose();
			}
		});
		_mainpane.add(hotSeat);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setLocation(xPos, yPos);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new PreGameGui();
	}

}
