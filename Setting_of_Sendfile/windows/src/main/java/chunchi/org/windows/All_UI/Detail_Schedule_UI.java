package chunchi.org.windows.All_UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.json.simple.JSONObject;

public class Detail_Schedule_UI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	JSONObject obj;
	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	JPanel top = new JPanel();
	
	JLabel task_key;
	JLabel task_value;
	JLabel move_copy_key;
	JLabel move_copy_value;
	JLabel execute_timing_key;
	JLabel execute_timing_value;
	JLabel timeout_key;
	JLabel timeout_value;
	JLabel remain_time_key;
	JLabel remain_time_value;
	
	JLabel source_connection_way_key;
	JLabel source_connection_way_value;
	JLabel source_ip_key;
	JLabel source_ip_value;
	JLabel source_connection_username_key;
	JLabel source_connection_username_value;
	JLabel source_mount_disk_key;
	JLabel source_mount_disk_value;
	JLabel source_folder_key;
	JLabel source_folder_value;
	JLabel source_subfolder_key;
	JLabel source_subfolder_value;
	
	JLabel target_connection_way_key;
	JLabel target_connection_way_value;
	JLabel target_ip_key;
	JLabel target_ip_value;
	JLabel target_connection_username_key;
	JLabel target_connection_username_value;
	JLabel target_mount_disk_key;
	JLabel target_mount_disk_value;
	JLabel target_folder_key;
	JLabel target_folder_value;

	public Detail_Schedule_UI(JSONObject obj) {
		
		this.obj = obj;
		top.setLayout(layout);
		this.setTitle("Details of tasks");
		this.setSize(1300, 700);
		this.setLocation(200, 150);
		this.setAlwaysOnTop(true);
		
		System.out.println("start show details");
		UIManager.put("Label.font", new Font("Courier", Font.BOLD, 18));
		
		int current_row = 0;
		task_key = new JLabel("task_name:");
		task_key.setPreferredSize(new Dimension(300,50));
		add_a_component(task_key, 0, current_row, 1, 1, c, top, layout);
		task_value = new JLabel((String) obj.get("task_name"));
		task_value.setPreferredSize(new Dimension(900,50));
		add_a_component(task_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		execute_timing_key = new JLabel("execute_timing:");
		execute_timing_key.setPreferredSize(new Dimension(300,50));
		add_a_component(execute_timing_key, 0, current_row, 1, 1, c, top, layout);
		execute_timing_value = new JLabel((String) obj.get("execute_timing"));
		execute_timing_value.setPreferredSize(new Dimension(900,50));
		add_a_component(execute_timing_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		move_copy_key = new JLabel("move or copy:");
		move_copy_key.setPreferredSize(new Dimension(300,50));
		add_a_component(move_copy_key, 0, current_row, 1, 1, c, top, layout);
		move_copy_value = new JLabel((String) obj.get("move_or_copy"));
		move_copy_value.setPreferredSize(new Dimension(900,50));
		add_a_component(move_copy_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		timeout_key = new JLabel("timeout:");
		timeout_key.setPreferredSize(new Dimension(300,50));
		add_a_component(timeout_key, 0, current_row, 1, 1, c, top, layout);
		timeout_value = new JLabel((String) obj.get("timeout")+"h");
		timeout_value.setPreferredSize(new Dimension(900,50));
		add_a_component(timeout_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_connection_way_key = new JLabel("source_connection_way:");
		source_connection_way_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_connection_way_key, 0, current_row, 1, 1, c, top, layout);
		source_connection_way_value = new JLabel((String) obj.get("source_connection_way"));
		source_connection_way_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_connection_way_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_ip_key = new JLabel("source_ip:");
		source_ip_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_ip_key, 0, current_row, 1, 1, c, top, layout);
		source_ip_value = new JLabel((String) obj.get("source_ip"));
		source_ip_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_ip_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_connection_username_key = new JLabel("source_connection_username:");
		source_connection_username_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_connection_username_key, 0, current_row, 1, 1, c, top, layout);
		source_connection_username_value = new JLabel((String) obj.get("source_connection_username"));
		source_connection_username_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_connection_username_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_mount_disk_key = new JLabel("source_mount_disk:");
		source_mount_disk_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_mount_disk_key, 0, current_row, 1, 1, c, top, layout);
		source_mount_disk_value = new JLabel((String) obj.get("source_mount_disk"));
		source_mount_disk_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_mount_disk_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_folder_key = new JLabel("source_folder:");
		source_folder_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_folder_key, 0, current_row, 1, 1, c, top, layout);
		source_folder_value = new JLabel((String) obj.get("source_folder"));
		source_folder_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_folder_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		source_subfolder_key = new JLabel("source_subfolder:");
		source_subfolder_key.setPreferredSize(new Dimension(300,50));
		add_a_component(source_subfolder_key, 0, current_row, 1, 1, c, top, layout);
		source_subfolder_value = new JLabel((String) obj.get("source_subfolder"));
		source_subfolder_value.setPreferredSize(new Dimension(900,50));
		add_a_component(source_subfolder_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		target_connection_way_key = new JLabel("target_connection_way:");
		target_connection_way_key.setPreferredSize(new Dimension(300,50));
		add_a_component(target_connection_way_key, 0, current_row, 1, 1, c, top, layout);
		target_connection_way_value = new JLabel((String) obj.get("target_connection_way"));
		target_connection_way_value.setPreferredSize(new Dimension(900,50));
		add_a_component(target_connection_way_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		target_ip_key = new JLabel("target_ip:");
		target_ip_key.setPreferredSize(new Dimension(300,50));
		add_a_component(target_ip_key, 0, current_row, 1, 1, c, top, layout);
		target_ip_value = new JLabel((String) obj.get("target_ip"));
		target_ip_value.setPreferredSize(new Dimension(900,50));
		add_a_component(target_ip_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		target_connection_username_key = new JLabel("target_connection_username:");
		target_connection_username_key.setPreferredSize(new Dimension(300,50));
		add_a_component(target_connection_username_key, 0, current_row, 1, 1, c, top, layout);
		target_connection_username_value = new JLabel((String) obj.get("target_connection_username"));
		target_connection_username_value.setPreferredSize(new Dimension(900,50));
		add_a_component(target_connection_username_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		target_mount_disk_key = new JLabel("target_mount_disk:");
		target_mount_disk_key.setPreferredSize(new Dimension(300,50));
		add_a_component(target_mount_disk_key, 0, current_row, 1, 1, c, top, layout);
		target_mount_disk_value = new JLabel((String) obj.get("target_mount_disk"));
		target_mount_disk_value.setPreferredSize(new Dimension(900,50));
		add_a_component(target_mount_disk_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		target_folder_key = new JLabel("target_folder:");
		target_folder_key.setPreferredSize(new Dimension(300,50));
		add_a_component(target_folder_key, 0, current_row, 1, 1, c, top, layout);
		target_folder_value = new JLabel((String) obj.get("target_folder"));
		target_folder_value.setPreferredSize(new Dimension(900,50));
		add_a_component(target_folder_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;
		
		remain_time_key = new JLabel("remain_time:");
		remain_time_key.setPreferredSize(new Dimension(300,50));
		add_a_component(remain_time_key, 0, current_row, 1, 1, c, top, layout);
		remain_time_value = new JLabel((String) obj.get("remain_time"));
		remain_time_value.setPreferredSize(new Dimension(900,50));
		add_a_component(remain_time_value, 1, current_row, 3, 1, c, top, layout);
		current_row+=1;	
		
		top.setVisible(true);
		top.setBounds(this.getBounds());
		this.add(top);
		this.setVisible(true);
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
