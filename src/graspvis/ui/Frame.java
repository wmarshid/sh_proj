package graspvis.ui;

import graspvis.controller.FrameController;
import graspvis.exception.SystemException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import processing.core.PApplet;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Frame extends JFrame{
	private FrameController controller;
	
	// The swing components
	private JPanel section1Panel;
	private PApplet section1View;
	private PApplet section2View;
	private PApplet section3View;
	private PApplet section4View;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JPanel northBorder;
	private JPanel southBorder;
	private JPanel westBorder;
	private JPanel eastBorder;
	private JPanel panel;
	private JPanel section2Panel;
	private JPanel section3Panel;
	private JPanel section4Panel;
	private JMenu mnView;
	private JMenuItem mntmViewConviguration;
	
	public Frame(FrameController theController) {
		controller = theController;
		initGUI();
	}
	
	private void initGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ButtonListener buttonListener = new ButtonListener();
		
		northBorder = new JPanel();
		getContentPane().add(northBorder, BorderLayout.NORTH);
		
		southBorder = new JPanel();
		getContentPane().add(southBorder, BorderLayout.SOUTH);
		
		westBorder = new JPanel();
		getContentPane().add(westBorder, BorderLayout.WEST);
		
		eastBorder = new JPanel();
		getContentPane().add(eastBorder, BorderLayout.EAST);
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 2, 0, 0));
		
		section1Panel = new JPanel();
		section1Panel.setLayout(new BorderLayout(0, 0));
		panel.add(section1Panel);
		
		section2Panel = new JPanel();
		section2Panel.setPreferredSize(new Dimension(100, 100));
		section2Panel.setLayout(new BorderLayout(0, 0));
		panel.add(section2Panel);
		
		section3Panel = new JPanel();
		panel.add(section3Panel);
		section3Panel.setLayout(new BorderLayout(0, 0));
		
		section4Panel = new JPanel();
		panel.add(section4Panel);
		section4Panel.setLayout(new BorderLayout(0, 0));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(buttonListener);
		mnFile.add(mntmOpen);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		mntmViewConviguration = new JMenuItem("View Conviguration");
		mntmViewConviguration.addActionListener(buttonListener);
		mnView.add(mntmViewConviguration);
	}

	public void showApplet() throws SystemException {
		if (section1View == null) {
			throw new SystemException("Constellation view is null");
		}
		if (section2View == null) {
			throw new SystemException("Element view is null");
		}
		
		section1Panel.removeAll();
		section1Panel.add(section1View, BorderLayout.CENTER);
		
		section2Panel.removeAll();
		section2Panel.add(section2View, BorderLayout.CENTER);
		
		section3Panel.removeAll();
		section3Panel.add(section3View, BorderLayout.CENTER);
		
		section4Panel.removeAll();
		section4Panel.add(section4View, BorderLayout.CENTER);
		
		if(section1View != null)
			section1View.init();
		if(section2View != null)
			section2View.init();
		if(section3View != null)
			section3View.init();
		if(section4View != null)
			section4View.init();
		setVisible(true);
	}
	
	/**
	 * @return the view
	 */
	public PApplet getSection1View() {
		return section1View;
	}

	/**
	 * @param view the view to set
	 */
	public void setSection1View(PApplet view) {
		this.section1View = view;
	}
	
	/**
	 * @return the elementView
	 */
	public PApplet getSection2View() {
		return section2View;
	}

	/**
	 * @param elementView the elementView to set
	 */
	public void setSection2View(PApplet elementView) {
		this.section2View = elementView;
	}

	/**
	 * @return the section3View
	 */
	public PApplet getSection3View() {
		return section3View;
	}

	/**
	 * @param section3View the section3View to set
	 */
	public void setSection3View(PApplet section3View) {
		this.section3View = section3View;
	}

	/**
	 * @return the section4View
	 */
	public PApplet getSection4View() {
		return section4View;
	}

	/**
	 * @param section4View the section4View to set
	 */
	public void setSection4View(PApplet section4View) {
		this.section4View = section4View;
	}

	public void setViews(PApplet view, int section) {
		switch (section) {
		case 1:
			section1View = view;
			break;
		case 2:
			section2View = view;
			break;
		case 3:
			section3View = view;
			break;
		case 4:
			section4View = view;
			break;
		default:
			section1View = view;
			break;
		}
	}
	
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mntmOpen)) {
				controller.openFile();
			}
			if (e.getSource().equals(mntmViewConviguration)) {
				controller.configureViews();
			}
		}
		
	}
}
