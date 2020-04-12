package com.example.swingtest;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SwingTest {
    private static final String IMAGE_FILENAME = "testimage.jpg";
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			SwingTest test = new SwingTest();
			test.createSwingWindow();
		});
	}
	
	public JFrame createSimpleSwingWindow() {
		JFrame frame = new JFrame("Swing Test");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("This is our test Swing application");
		JButton button = new JButton("Click Me!");
		
		frame.add(label);
		frame.add(button);
		
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
	
	public JFrame createSwingWindow() {
		JFrame frame = new JFrame("Test Swing Application");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("This is our test Swing application");
		JButton button = new JButton("Click Me!");
		
		ImageIcon icon = new ImageIcon(IMAGE_FILENAME);
		JLabel iconLabel = new JLabel(icon);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel pctSizeLabel = new JLabel("Percent Size: ");
		JTextField pctSizeField = new JTextField();
		JLabel rotationalLabel = new JLabel("Degree Rotation: ");
		JTextField rotationField = new JTextField();
		
		button.addActionListener(event -> 
		       adjustImage(pctSizeField, rotationField, iconLabel));
		panel.add(pctSizeLabel);
		panel.add(pctSizeField);
		panel.add(rotationalLabel);
		panel.add(rotationField);
		
		frame.add(label);
		frame.add(panel);
		frame.add(button);
		frame.add(iconLabel);
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		frame.pack();
		frame.setVisible(true);
		return frame;
		
	}

	private void adjustImage(JTextField pctField, JTextField degreeField, JLabel imageLabel) {
		Icon icon = imageLabel.getIcon();
		ImageIcon imageIcon = (ImageIcon)icon;
		Image image = imageIcon.getImage();
		
		BufferedImage bi = convertToBufferedImage(image);
		
		bi = resizeImage(bi, getIntFromTextField(pctField,100));
		bi = rotateImage(bi, getIntFromTextField(pctField,0));
		
		imageLabel.setIcon(new ImageIcon(bi));
	}
	
	private BufferedImage convertToBufferedImage(Image image) {
		if(image instanceof BufferedImage) {
			return (BufferedImage)image;
		}
		
		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),
				BufferedImage.TYPE_BYTE_BINARY);
		Graphics g = bi.getGraphics();
		g.drawImage(image, 0,0,null);
		g.dispose();
		
		return bi;
		
	}
	
	private BufferedImage resizeImage(BufferedImage bi, int percent) {
		double scale = percent/100.0;
		AffineTransform resize = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp op= new AffineTransformOp (
				resize,
				AffineTransformOp.TYPE_BICUBIC);
		return op.filter(bi, null);
	}
	
	private BufferedImage rotateImage(BufferedImage bi, int degrees) {
		double radians = Math.toRadians(degrees);
		AffineTransform rotate = AffineTransform.getRotateInstance(radians);
		AffineTransformOp op= new AffineTransformOp (
				rotate,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(bi, null);
	}
	
	private int getIntFromTextField(JTextField pctField, int defaultValue) {
		try {
			int num = Integer.parseInt(pctField.getText());
			return num;
		}catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
