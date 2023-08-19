package Test;

import java.awt.Dimension;

import javax.swing.JFrame;

import Server.Consts;

public class solidTester extends JFrame {
	public solidTester(String windowName) {
		super(windowName);
		setPreferredSize(new Dimension(Consts.TILE_SIZE_PX + 100, Consts.TILE_SIZE_PX + 100));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		solidTester fr = new solidTester("solidTester");
		solidTesterPanel tsp = new solidTesterPanel();
		fr.add(tsp);
		fr.addMouseListener(tsp);
		fr.setVisible(true);
	}
}
