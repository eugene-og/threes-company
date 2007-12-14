package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.columbia.threescompany.client.Settings;

public class SettingsFrame extends JFrame {

	private static final long serialVersionUID = -3003280397568559407L;

	private JCheckBox soundCheckBox;
	
	public SettingsFrame() {
		super("Blobs Game Settings");
		JPanel mainPane = (JPanel)this.getContentPane();
		mainPane.setLayout(new GridLayout(3, 2));
		mainPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8), 
				   BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Game Settings"), 
													  BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		soundCheckBox = new JCheckBox("Sound", Settings.getInstance().soundOn);
		mainPane.add(soundCheckBox);
		mainPane.add(new JLabel(""));
		mainPane.add(new JLabel(""));
		mainPane.add(new JLabel(""));
		JButton saveSettingsButton = new JButton("Save");
		saveSettingsButton.setPreferredSize(new Dimension(50,30));
		saveSettingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveSettings();
				dispose();
			}
		});
		mainPane.add(saveSettingsButton,BorderLayout.SOUTH);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(50,30));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		mainPane.add(cancelButton,BorderLayout.SOUTH);
		
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int xPos = (int)(rect.getWidth() - GuiConstants.PLAYER_GUI_WIDTH)/2;
		int yPos = (int)(rect.getHeight() - GuiConstants.PLAYER_GUI_HEIGHT)/2;
		
		setPreferredSize(new Dimension(GuiConstants.PLAYER_GUI_WIDTH, GuiConstants.PLAYER_GUI_HEIGHT));
		setLocation(xPos, yPos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void saveSettings() {
		Settings.getInstance().soundOn = soundCheckBox.isSelected();
	}

	public static void main(String[] args) {
		new SettingsFrame();
	}

}
