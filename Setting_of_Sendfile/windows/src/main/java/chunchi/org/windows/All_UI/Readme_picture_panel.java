package chunchi.org.windows.All_UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import chunchi.org.windows.App;

public class Readme_picture_panel extends Panel{
	
	private static final long serialVersionUID = 1L;
	File current_working_space;
	
	public JLabel example1;
	public All_UI aui;
	GridBagConstraints c;
	GridBagLayout layout;
	
	
	Readme_picture_panel(All_UI aui,String image_name){
		
		this.aui=aui;
		c = new GridBagConstraints();
		layout = new GridBagLayout();
		
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		try {
			example1 = new JLabel(new ImageIcon(
				ImageIO.read(new File(current_working_space,image_name))
				.getScaledInstance(1100, 750, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			example1 = new JLabel("...");
			e.printStackTrace();
		}
		
		add_a_component(example1, 0, 0, 1, 1, c, this, layout);
    }
	
	public static void add_a_component(Component comp,int gridx, 
			int gridy, int gridwidth, int gridheight, 
			GridBagConstraints c,Container top, GridBagLayout gridbag) {
	   
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
