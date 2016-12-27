package com.example.fiveyuanstore.inputcells;

import javax.swing.*;
import java.awt.*;

public class ImageSize extends JFrame {

	private ImageIcon img;
	private JLabel showImg;

	private final static int WIDTH = 147;
	private final static int HEIGHT = 136;

	public ImageSize() {

		img = new ImageIcon("1.png");
		img.setImage(img.getImage().getScaledInstance(ImageSize.WIDTH, ImageSize.HEIGHT, Image.SCALE_DEFAULT));

		showImg = new JLabel();
		showImg.setIcon(img);

		this.add(showImg, BorderLayout.CENTER);
		this.setBounds(300, 200, 400, 300);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String args[])
	 {
	  new ImageSize();
	 }
}
