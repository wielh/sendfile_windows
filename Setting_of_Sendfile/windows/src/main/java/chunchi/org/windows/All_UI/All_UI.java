package chunchi.org.windows.All_UI;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

public class All_UI extends JFrame{

	private static final long serialVersionUID = 1L;
	All_UI aUi = this;
	JTabbedPane jtp;
	
	Readme_picture_panel rmp_panel1;
	Readme_picture_panel rmp_panel2;
	Readme_picture_panel rmp_panel3;
	Readme_panel rm_panel;
	RemoteToRemote_Panel rr_panel;
	Schedule_Panel sd_panel;
	
	String database_username;

	public All_UI(String database_username) {
		
		UIManager.put("TabbedPane.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Button.font", new Font("Courier", Font.BOLD, 16));
		UIManager.put("Label.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("TextField.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("ComboBox.font", new Font("Courier", Font.BOLD, 18));		
		UIManager.put("RadioButton.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("CheckBox.font", new Font("Courier", Font.BOLD, 18));
		//=================================================================
		
		this.setTitle("Setting of daily task");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.database_username = database_username;
		this.setSize(1250, 850);
		this.setLocation(400, 150); 
		
		//hr_panel = new HostToRemote_Panel(this);
		rmp_panel1 = new Readme_picture_panel(this,"Example1.png");
		rmp_panel2 = new Readme_picture_panel(this,"Example2.png");
		rmp_panel3 = new Readme_picture_panel(this,"Example3.png");
		rm_panel = new Readme_panel(this);
		rr_panel = new RemoteToRemote_Panel(this);
		sd_panel = new Schedule_Panel(this, database_username);
		jtp = new JTabbedPane();
		jtp.addTab("remote_remote_task", rr_panel);
		jtp.addTab("future_tasks", sd_panel);
		jtp.addTab("readme", rm_panel);
		jtp.addTab("example1", rmp_panel1);
		jtp.addTab("example2", rmp_panel2);
		jtp.addTab("example3", rmp_panel3);
		jtp.setSelectedIndex(0);
		
		this.setContentPane(jtp);
		this.setVisible(true);	
	}
	
	public static boolean check_whether_a_task(File f) {
		if(f.isDirectory()) {
			ArrayList<String> total_child_name = new ArrayList<String>();
			for(File child:f.listFiles()) {
				total_child_name.add(child.getName());
				//System.out.println(child.getName());
			}			
			
			return total_child_name.contains("script.bat")
					&& total_child_name.contains("log.txt");		
		} else {
			return false;
		}
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

	

