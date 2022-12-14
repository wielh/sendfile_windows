package chunchi.org.windows.All_UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.json.simple.JSONObject;

import chunchi.org.windows.Config;
import chunchi.org.windows.BackEnd.Utils;

public class Schedule_Panel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	public JLabel next_n_days_label;
	public JTextField next_n_days_textfield;
	public JButton confirm;
	
	public JTable jt;
	public Object[] header_of_jt 
		= {"task_name","source","target","time_left"};
	public JScrollPane js;
	
	GridBagConstraints c = new GridBagConstraints();
	GridBagLayout layout = new GridBagLayout();
	All_UI aui;
	
	public Schedule_Panel(All_UI aui,String database_username) {
		
		this.setLayout(layout);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aui = aui;
	    this.setBounds(aui.getBounds());
	    this.setVisible(true);		
		
		UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Table.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Label.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("TextField.font", new Font("Courier", Font.BOLD, 18));
		
		next_n_days_label = new JLabel("Input a number n(1~60) to show "
				+ "next n_day schedules:",JLabel.CENTER);
		next_n_days_label.setPreferredSize(new Dimension(600,50));
		add_a_component(next_n_days_label, 0, 0, 8, 1, c,this, layout);
		next_n_days_textfield = new JTextField("7");
		next_n_days_textfield.setPreferredSize(new Dimension(50,50));
		add_a_component(next_n_days_textfield, 8, 0, 1, 1, c,this, layout);
		confirm = new JButton("confirm");
		confirm.setPreferredSize(new Dimension(160,50));
		confirm.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Connection conn = Utils.connect_to_database();
				Statement stmt;
				ResultSet result;
				
				int next_n_days = 7;
				try {
					next_n_days = Integer.parseInt(next_n_days_textfield.getText());
				} catch (Exception e2) {
					Utils.set_and_show_error("The input is not a number.",true);
					return;
				}			
				
				if(next_n_days>60) {
					next_n_days=60;
				}
				
				if(next_n_days<1) {
					next_n_days=1;
				}
				
				ArrayList<String> execute_timing_list = new ArrayList<String>();
				ArrayList<String> period_list = new ArrayList<String>();		
				ArrayList<Integer> date_list = new ArrayList<Integer>();
				ArrayList<String> weekday_list = new ArrayList<String>();
				ArrayList<Integer> hour_list = new ArrayList<Integer>();
				ArrayList<Integer> minute_list = new ArrayList<Integer>();
		
				ArrayList<String> task_name_list = new ArrayList<String>();
				ArrayList<String> target_list = new ArrayList<String>();
				ArrayList<String> source_list = new ArrayList<String>();
				
				LinkedHashMap<Integer, JSONObject> all_tasks_in_future
					= new LinkedHashMap<Integer, JSONObject>();
						
				try {					
					//step1 collect data
					stmt = conn.createStatement();
					String stmt_string = "use "+ Config.DB_database_name+";";
					stmt.executeUpdate(stmt_string);
					stmt_string = String.format
							("select * from tasks where username='%s'",database_username);	
					result = stmt.executeQuery(stmt_string);
							
				    while (result.next()) {
				    	task_name_list.add(result.getString("task_name"));
				    	execute_timing_list.add(result.getString("execute_timing"));
				    	if(result.getString("execute_timing").equals("periodical")) {
				    		period_list.add(result.getString("period"));
							if(result.getString("period").equals("monthly")) {
								weekday_list.add("*");
								date_list.add(result.getInt("day_of_month"));
							} else if(result.getString("period").equals("weekly")){
								weekday_list.add(result.getString("day_of_week"));
								date_list.add(-1);
							} else {
								weekday_list.add("*");
								date_list.add(-1);
							}					
							hour_list.add(result.getInt("hour"));
							minute_list.add(result.getInt("minute"));
				    	} else {
				    		period_list.add("*");
				    		weekday_list.add("*");
				    		date_list.add(-1);
				    		hour_list.add(-1);
							minute_list.add(-1);
				    	}
				    							
						if(result.getString("source_connection_way").equals("localhost")) {
							source_list.add("localhost"+result.getString("source_folder"));
						} else {
							source_list.add(result.getString("source_ip")+"...");
						}	
						
						if(result.getString("target_connection_way").equals("localhost")) {
							target_list.add("localhost"+result.getString("target_folder"));
						} else {
							target_list.add(result.getString("target_ip")+"...");
						}		
						
					}	    	    
				} catch (SQLException e1) {
					e1.printStackTrace();
					return;
				}
				
				try {
					result.close();
					stmt.close();
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					return;
				}
				
				//step2 add new rows 
				@SuppressWarnings("removal")
				Integer index = new Integer(0);
				try {
				   for(int i=0;i<task_name_list.size();i++) {			    	
				    	ArrayList<Long> time_difference_list = new ArrayList<Long>();
				    	
				    	if(!execute_timing_list.get(i).equals("periodical")) {
				    		time_difference_list.add((long) -1);
				    	} else {
					    	if(period_list.get(i).equals("monthly")) {
					    		time_difference_list = Utils.get_next_starttime_monthly
					    			(minute_list.get(i), hour_list.get(i),date_list.get(i), next_n_days);
					    	} else if(period_list.get(i).equals("weekly")) {
					    		time_difference_list = Utils.get_next_starttime_weekly
						    		(minute_list.get(i), hour_list.get(i), weekday_list.get(i), next_n_days);
					    	} else {
					    		time_difference_list = Utils.get_next_starttime_daily
							    	(minute_list.get(i), hour_list.get(i), next_n_days);
					    	}
				    	}
				    	
				    	for(int j=0;j<time_difference_list.size();j++) {
				    		JSONObject object = new JSONObject();
				    		object.put("task_name", task_name_list.get(i));		    			    		
				    		object.put("source", source_list.get(i));
				    		object.put("target", target_list.get(i));
				    		object.put("time_difference", time_difference_list.get(j));
				    		all_tasks_in_future.put(index,object);
				    		index += 1;
				    	}
				    }	
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			 	    							
				DefaultTableModel dft =(DefaultTableModel) jt.getModel();
				dft.setRowCount(0);
				String[][] all_data = new String[all_tasks_in_future.size()][4];
				for(int i=0;i<all_tasks_in_future.size();i++) {
					all_data[i][0] = (String) all_tasks_in_future.get(i).get("task_name");
					all_data[i][1] = (String) all_tasks_in_future.get(i).get("source");
					all_data[i][2] = (String) all_tasks_in_future.get(i).get("target");
					all_data[i][3] = String.valueOf(
							all_tasks_in_future.get(i).get("time_difference"));
							
					dft.addRow(new String[] {all_data[i][0],all_data[i][1]
							,all_data[i][2],Utils.Millisecond_to_String(
							(long)all_tasks_in_future.get(i).get("time_difference"))});
				}
				dft.fireTableDataChanged();		
			}
		});
		add_a_component(confirm, 9, 0, 2, 1, c,this, layout);
		
		//jt = new JTable(default_data,this.header_of_jt);
		DefaultTableModel dft = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		dft.setColumnIdentifiers(header_of_jt);
		jt = new JTable(dft);
		jt.getTableHeader().setFont(new Font("Courier", Font.BOLD, 18));
		jt.setRowHeight(50);
		jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {	
			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {		
				Connection conn = Utils.connect_to_database();
				Statement stmt;
				ResultSet result;
				String stmt_string = String.format("select * from tasks"
						+ " where username='%s' and task_name='%s';",
						database_username, jt.getValueAt(jt.getSelectedRow(), 0).toString());		
				try {
					stmt = conn.createStatement();
					result = stmt.executeQuery(stmt_string);
					if (result.next()) {
					    JSONObject parameters = new JSONObject();
		 				parameters.put("task_name", result.getString("task_name"));
		 				parameters.put("move_or_copy", result.getString("move_or_copy"));			
	 					parameters.put("timeout", String.valueOf(result.getFloat("timeout")));	
	 					parameters.put("execute_timing", result.getString("execute_timing"));	
	 					parameters.put("remain_time", jt.getValueAt(jt.getSelectedRow(), 3).toString());
	 					
		 				parameters.put("source_connection_way", result.getString("source_connection_way"));
		 				if(result.getString("source_connection_way").equals("temporary cifs connection")) {
		 					parameters.put("source_connection_username", result.getString("source_connection_username"));
			 				parameters.put("source_ip", result.getString("source_ip"));
			 				parameters.put("source_subfolder",result.getString("source_subfolder"));
		 				} else if(result.getString("source_connection_way").equals("temporary nfs connection")) {
		 					parameters.put("source_ip", result.getString("source_ip"));
		 					parameters.put("source_subfolder",result.getString("source_subfolder"));
		 				}
		 				parameters.put("source_folder",result.getString("source_folder"));
		 				
		 				parameters.put("target_connection_way", result.getString("target_connection_way"));
		 				if(result.getString("target_connection_way").equals("temporary cifs connection")) {
		 					parameters.put("target_connection_username", result.getString("target_connection_username"));
			 				parameters.put("target_ip", result.getString("target_ip"));
		 				} else if(result.getString("source_connection_way").equals("temporary nfs connection")) {
		 					parameters.put("target_ip", result.getString("target_ip"));
		 				}
		 				parameters.put("target_folder",result.getString("target_folder"));
		 				
					    new Detail_Schedule_UI(parameters);
				    } else {
				    	return;
				    }
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
										   			
			}
		});

		TableColumnModel jt_model = jt.getColumnModel();
		jt_model.getColumn(0).setPreferredWidth(200);
		jt_model.getColumn(1).setPreferredWidth(350);
		jt_model.getColumn(2).setPreferredWidth(350);
		jt_model.getColumn(3).setPreferredWidth(200);
	
		js = new JScrollPane(jt);
		js.setPreferredSize(new Dimension(1050,700));
		add_a_component(js, 0, 1, 11, 7, c, this, layout);									
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
	
	public void set_and_show_error(String info) {
		 JOptionPane.showConfirmDialog(null, info, 
				 "error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}	
	
	public void set_and_show_info(String info) {
		 JOptionPane.showConfirmDialog(null, info, 
				 "information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
}
