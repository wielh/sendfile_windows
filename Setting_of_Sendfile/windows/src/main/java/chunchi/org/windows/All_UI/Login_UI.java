package chunchi.org.windows.All_UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Login_UI extends JFrame{

	private static final long serialVersionUID = 1L;
	public JLabel database_username_label;
	public JTextField database_username_textfield;
	public JButton login_database;

	GridBagLayout layout;
	JPanel panel;
	Login_UI self;
	
	public Login_UI() {
		
		self = this;
		layout = new GridBagLayout();
		panel = new JPanel(layout);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(850, 130);
	    this.setLocation(450, 400);  
	    this.setTitle("login");
  
	    panel.setBounds(this.getBounds());
	    panel.setVisible(true);		
		UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Label.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("TextField.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("ComboBox.font", new Font("Courier", Font.BOLD, 18));		
		UIManager.put("RadioButton.font", new Font("Courier", Font.BOLD, 18));
		
		int current_row = 0;
		database_username_label = new JLabel("database username",JLabel.CENTER);
	    database_username_label.setPreferredSize(new Dimension(250, 50));
	    add_a_component(database_username_label, 0, current_row, 5, 1, panel, layout);
	    database_username_textfield = new JTextField();
	    database_username_textfield.setPreferredSize(new Dimension(400, 50));
	    add_a_component(database_username_textfield, 5, current_row, 7, 1, panel, layout);
    
	    login_database = new JButton("login");
	    login_database.setPreferredSize(new Dimension(150, 50));
	    login_database.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {				
				new All_UI(database_username_textfield.getText());
				self.dispose();										
			}		
		});
	    add_a_component(login_database, 12, current_row, 3, 1, panel, layout);
	    current_row+=1;
	    
		this.add(panel);
		this.setBounds(panel.getBounds());
		this.setVisible(true);
	}
	
	public static void add_a_component(Component comp,int gridx, int gridy, int gridwidth, 
			int gridheight, Container top, GridBagLayout gridbag) {
	    GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = 5;
        c.weighty = 5;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(comp, c);
        comp.setVisible(true);
        top.add(comp); 
	}
	
}
