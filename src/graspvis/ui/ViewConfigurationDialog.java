package graspvis.ui;

import graspvis.util.Session;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.Dimension;

public class ViewConfigurationDialog extends JDialog {
	public enum View{OVERVIEW, ELEMENT}
	public enum ViewKeys{SECTION1, SECTION2, SECTION3, SECTION4};
	
	private JComboBox cbSection1;
	private JComboBox cbSection2;
	private JComboBox cbSection3;
	private JComboBox cbSection4;
	private JButton btnDefault;
	private JButton btnOk;
	private JLabel lblViewConfiguration;
	private JLabel lblSection4;
	private JLabel lblSection3;
	private JLabel lblSection2;
	private JLabel lblSection;
	private JPanel mainPanel;
	
	private ButtonListener listener;
	
	private Map<Object, Object> views;
	
	public ViewConfigurationDialog(Frame owner, boolean modality) {
		super(owner, modality);
		setSize(new Dimension(256, 228));
		this.views = new HashMap<Object, Object>();
		listener = new ButtonListener();
		
		setTitle("View Configuration");
		
		mainPanel = new JPanel();
		
		lblSection = new JLabel("Section 1");
		
		cbSection1 = new JComboBox();
		cbSection1.setModel(new DefaultComboBoxModel(View.values()));
		
		lblSection2 = new JLabel("Section 2");
		
		cbSection2 = new JComboBox();
		cbSection2.setModel(new DefaultComboBoxModel(View.values()));
		
		lblSection3 = new JLabel("Section 3");
		
		cbSection3 = new JComboBox();
		cbSection3.setModel(new DefaultComboBoxModel(View.values()));
		
		lblSection4 = new JLabel("Section 4");
		
		cbSection4 = new JComboBox();
		cbSection4.setModel(new DefaultComboBoxModel(View.values()));
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(listener);
		
		btnDefault = new JButton("Default");
		btnDefault.addActionListener(listener);
		
		lblViewConfiguration = new JLabel("View Conviguration");
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblViewConfiguration, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_mainPanel.createSequentialGroup()
								.addComponent(lblSection)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbSection1, 0, 176, Short.MAX_VALUE))
							.addGroup(gl_mainPanel.createSequentialGroup()
								.addComponent(lblSection2)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbSection2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGroup(gl_mainPanel.createSequentialGroup()
								.addComponent(lblSection3)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbSection3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGroup(gl_mainPanel.createSequentialGroup()
								.addComponent(lblSection4)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbSection4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 73, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnDefault)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOk)))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblViewConfiguration)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSection)
						.addComponent(cbSection1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSection2)
						.addComponent(cbSection2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSection3)
						.addComponent(cbSection3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSection4)
						.addComponent(cbSection4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDefault)
						.addComponent(btnOk))
					.addContainerGap(111, Short.MAX_VALUE))
		);
		mainPanel.setLayout(gl_mainPanel);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		getContentPane().setLayout(groupLayout);
		this.pack();
	}
	
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(btnOk)) {
				views.put(ViewKeys.SECTION1, cbSection1.getSelectedItem());
				views.put(ViewKeys.SECTION2, cbSection2.getSelectedItem());
				views.put(ViewKeys.SECTION3, cbSection3.getSelectedItem());
				views.put(ViewKeys.SECTION4, cbSection4.getSelectedItem());
				Session.getSession().put(Session.Key.VIEW_CONFIGURATION, views);
			}
			if (e.getSource().equals(btnDefault)) {
				views.put(ViewKeys.SECTION1, View.OVERVIEW);
				views.put(ViewKeys.SECTION2, View.ELEMENT);
				views.put(ViewKeys.SECTION3, View.OVERVIEW);
				views.put(ViewKeys.SECTION4, View.OVERVIEW);
				Session.getSession().put(Session.Key.VIEW_CONFIGURATION, views);
			}
			ViewConfigurationDialog.this.dispose();
		}
		
	}
}
