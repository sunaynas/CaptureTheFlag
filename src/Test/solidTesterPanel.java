package Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import Assets.ImgConsts;
import Server.Consts;

public class solidTesterPanel extends JPanel implements MouseListener {

	public solidTesterPanel() {
//		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
		setDoubleBuffered(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		System.out.println("Calling paint component");
		ImgConsts.initImgs(getClass());
		Image i = ImgConsts.TL_LWALL_Img.getScaledInstance(Consts.TILE_SIZE_PX, Consts.TILE_SIZE_PX,
				Image.SCALE_DEFAULT);
		// ignore+50 its for weird paint offset

		int mx = 0, my = 50;
		g.setColor(Color.RED);
		g.drawImage(i, mx, my, null);
		g.drawRect(mx + 220, my + 205 - 30, 400 - 220, 260 - 205);
		g.setColor(Color.CYAN);
		g.drawRect(mx + 185, my + 205 - 30, 220 - 185, 430 - 205);
//		// CANNOT GO ON
//		g.drawOval(160, 275 + 50, 365 - 160, 430 - 320 + 20);
//		g.drawRect(175, 90 + 50, 350 - 175, 350 - 90);
//		g.drawOval(175, 40 + 50, 350 - 176, 170 - 70);
//		// CAN GO ON
//		g.setColor(Color.cyan);
//		g.drawRect(300, 135 - 25 + 50, 355 - 300 - 10, 430 - 135);
//		g.drawOval(175 + 20, 40 + 20 + 50, 350 - 176 - 35, 170 - 70 - 35);
//		g.drawRect(286, 100 + 50, 334 - 280, 152 - 120);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MouseClicked at X:" + e.getX() + " Y:" + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
