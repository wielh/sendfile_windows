package chunchi.org.windows.All_UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import chunchi.org.windows.Config;
import chunchi.org.windows.BackEnd.Utils;

public class Import_UI extends JFrame{

	private static final long serialVersionUID = 1L;

	String database_username;
	All_UI aui;
	
	JTabbedPane jtp;
	all_panel all_panel;
	source_panel source_panel;
	target_panel target_panel;
	RemoteToRemote_Panel rrp;

	public Import_UI(RemoteToRemote_Panel rrp,String database_username) {	
		this.database_username = database_username;
		this.all_panel = new all_panel(this);
		this.source_panel = new source_panel(this);
		this.target_panel = new target_panel(this);
		this.rrp = rrp;
		this.jtp = new JTabbedPane();
		jtp.addTab("both_side setting", all_panel);
		jtp.addTab("source setting", source_panel);
		jtp.addTab("target setting", target_panel);
		jtp.setSelectedIndex(0);
		this.setBounds(new Rectangle(200,100,1550,850));
		this.setContentPane(jtp);
		this.setVisible(true);
	}
	
	public static void add_a_component(Component comp,int gridx, 
			int gridy, int gridwidth, int gridheight, 
			Container top, GridBagLayout gridbag) {
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

class all_panel extends JPanel{

	private static final long serialVersionUID = 1L;
	public Import_UI iui;
	public JTable jt;
	public JScrollPane js;
	public JButton confirm;
	GridBagLayout layout;
		
	public all_panel(Import_UI iui) {
		
		layout = new GridBagLayout();
		this.iui = iui;
		this.setLayout(layout);
	    this.setBounds(iui.getBounds()); 
	    this.setVisible(true);
	    
	    UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Table.font", new Font("Courier", Font.BOLD, 18));
		
		try {
			Connection conn = Utils.connect_to_database();
			Statement stmt = conn.createStatement();
			String stmt_string = "use "+ Config.DB_database_name+";";
			stmt.executeUpdate(stmt_string);
			stmt_string = String.format("select * from tasks "
			+ "where username='%s';",iui.database_username);				
			ResultSet result = stmt.executeQuery(stmt_string);
			
			ArrayList<String> task_list = new ArrayList<String>();
			
			ArrayList<String> source_connection_way_list = new ArrayList<String>();
			ArrayList<String> source_ip_list = new ArrayList<String>();
			ArrayList<String> source_connection_username_list = new ArrayList<String>();
			ArrayList<String> source_folder_list = new ArrayList<String>();
			ArrayList<String> source_subfolder_list = new ArrayList<String>();
			
			ArrayList<String> target_connection_way_list = new ArrayList<String>();
			ArrayList<String> target_ip_list = new ArrayList<String>();
			ArrayList<String> target_connection_username_list = new ArrayList<String>();
			ArrayList<String> target_folder_list = new ArrayList<String>();
			
			while (result.next()) {						
				task_list.add(result.getString("task_name"));			
				if(result.getString("source_connection_way")==null) {
					source_connection_way_list.add("");
				} else {
					source_connection_way_list.add(result.getString("source_connection_way"));
				}
				
				if(result.getString("source_ip")==null) {
					source_ip_list.add("");
				} else {
					source_ip_list.add(result.getString("source_ip"));
				}

				if(result.getString("source_connection_username")==null) {
					source_connection_username_list.add("");
				} else {
					source_connection_username_list.add(result.getString("source_connection_username"));
				}
				
				if(result.getString("source_folder")==null) {
					source_folder_list.add("");
				} else {
					source_folder_list.add(result.getString("source_folder"));
				}
				
				if(result.getString("source_subfolder")==null) {
					source_subfolder_list.add("");
				} else {
					source_subfolder_list.add(result.getString("source_subfolder"));
				}
				
				if(result.getString("target_connection_way")==null) {
					target_connection_way_list.add("");
				} else {
					target_connection_way_list.add(result.getString("target_connection_way"));
				}
				
				if(result.getString("target_ip")==null) {
					target_ip_list.add("");
				} else {
					target_ip_list.add(result.getString("target_ip"));
				}

				if(result.getString("target_connection_username")==null) {
					target_connection_username_list.add("");
				} else {
					target_connection_username_list.add(result.getString("target_connection_username"));
				}
				
				if(result.getString("target_folder")==null) {
					target_folder_list.add("");
				} else {
					target_folder_list.add(result.getString("target_folder"));
				}
			}		
			
			String[][] all_data = new String[source_connection_way_list.size()][9];
			for(int i=0;i<source_connection_way_list.size();i++) {
				//all_data[i][0] = task_list.get(i);
				all_data[i][0] = source_connection_way_list.get(i);
				all_data[i][1] = source_ip_list.get(i);
				all_data[i][2] = source_connection_username_list.get(i);
				all_data[i][3] = source_folder_list.get(i);
				all_data[i][4] = source_subfolder_list.get(i);
				all_data[i][5] = target_connection_way_list.get(i);
				all_data[i][6] = target_ip_list.get(i);
				all_data[i][7] = target_connection_username_list.get(i);
				all_data[i][8] = target_folder_list.get(i);
			}
					
			String[] header = {"source_connection_way",
				"source_ip","source_connection_username","source_folder",
				"folder_to_be_copied","target_connection_way",
				"target_ip","target_connection_username","target_folder"};		
			jt = new JTable(all_data,header);
			jt.getTableHeader().setFont(new Font("Courier", Font.BOLD, 18));
			jt.setRowHeight(50);
			jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			TableColumnModel jt_model = jt.getColumnModel();
			jt_model.getColumn(0).setPreferredWidth(150);
			jt_model.getColumn(1).setPreferredWidth(150);
			jt_model.getColumn(2).setPreferredWidth(150);
			jt_model.getColumn(3).setPreferredWidth(150);
			jt_model.getColumn(4).setPreferredWidth(150);
			jt_model.getColumn(5).setPreferredWidth(150);
			jt_model.getColumn(6).setPreferredWidth(150);
			jt_model.getColumn(7).setPreferredWidth(150);
			jt_model.getColumn(8).setPreferredWidth(150);
			js = new JScrollPane(jt);
			js.setPreferredSize(new Dimension(1450,700));
			add_a_component(js, 0, 0, 10, 10, this, layout);
			
			jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {			
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (jt.getSelectedRow() > -1) {				
						try {
							String task_name = task_list.get(jt.getSelectedRow());
							String stmt_string = String.format("select * from tasks "
									+ "where username='%s' and task_name='%s';"
									,iui.database_username, task_name);						
							ResultSet result;
							result = stmt.executeQuery(stmt_string);
							if(result.next()) {
								iui.rrp.edit_mv_or_cp_combobox.setSelectedItem(result.getString("move_or_copy"));
								iui.rrp.period_combobox.setSelectedItem(result.getString("period"));
								iui.rrp.date_textfield.setText(result.getString("??????"));
								iui.rrp.weekday_JComboBox.setSelectedItem(result.getString("??????"));
								iui.rrp.start_hour_textfield.setText(String.valueOf(result.getInt("??????")));
								iui.rrp.start_minute_textfield.setText(String.valueOf(result.getInt("??????")));
								iui.rrp.execute_hour_textfield.setText(String.valueOf(result.getFloat("timeout")));
								iui.rrp.edit_mv_or_cp_combobox.setSelectedItem(result.getString("move_or_copy"));
		 	 				 	 							
								iui.rrp.source_connection_way_combobox.setSelectedItem(result.getString("source_connection_way"));
								iui.rrp.source_ip_textfield.setText(result.getString("source_ip"));
								iui.rrp.source_connection_username_textfield.setText(result.getString("source_connection_username"));
								iui.rrp.source_folder_textfield.setText(result.getString("source_folder"));
								iui.rrp.source_subfolder_textfield.setText(result.getString("source_subfolder"));
								String s = result.getString("encrypted_source_connection_password");
								if(s!=null) {
									iui.rrp.source_connection_password_textfield.setText(Utils.decrypted_by_AES(s));
								}
								
								iui.rrp.dest_connection_way_combobox.setSelectedItem(result.getString("target_connection_way"));
		 	 					iui.rrp.dest_ip_textfield.setText(result.getString("target_ip"));	
		 	 					iui.rrp.dest_connection_username_textfield.setText(result.getString("target_connection_username"));
		 	 					iui.rrp.dest_folder_textfield.setText(result.getString("target_folder"));																		
		 	 					s = result.getString("encrypted_target_connection_password");
		 	 					if(s!=null) {
		 	 						iui.rrp.dest_connection_password_textfield.setText(Utils.decrypted_by_AES(s));				
		 	 					}	
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}							
			        }
					
				}
			});	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void add_a_component(Component comp,int gridx, 
			int gridy, int gridwidth, int gridheight, 
			Container top, GridBagLayout gridbag) {
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


class source_panel extends JPanel{

	private static final long serialVersionUID = 1L;
	public Import_UI iui;
	public JTable jt;
	public JScrollPane js;
	public JButton confirm;
	GridBagLayout layout;
		
	public source_panel(Import_UI iui) {
		
		layout = new GridBagLayout();
		this.iui = iui;
		this.setLayout(layout);
	    this.setBounds(iui.getBounds()); 
	    this.setVisible(true);
	    
	    UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Table.font", new Font("Courier", Font.BOLD, 18));
		
		try {
			Connection conn = Utils.connect_to_database();
			Statement stmt = conn.createStatement();
			String stmt_string = "use "+ Config.DB_database_name+";";
			stmt.executeUpdate(stmt_string);
			stmt_string = String.format("select distinct source_connection_way,"
			+ "source_ip,source_connection_username,source_folder,source_subfolder "
			+ "from tasks where username='%s';"
			,iui.database_username);		
			
			ResultSet result = stmt.executeQuery(stmt_string);	
							
			ArrayList<String> source_connection_way_list = new ArrayList<String>();
			ArrayList<String> source_ip_list = new ArrayList<String>();
			ArrayList<String> source_connection_username_list = new ArrayList<String>();
			ArrayList<String> source_folder_list = new ArrayList<String>();
			ArrayList<String> source_subfolder_list = new ArrayList<String>();
			
			while (result.next()) {					
				if(result.getString("source_connection_way")==null) {
					source_connection_way_list.add("");
				} else {
					source_connection_way_list.add(result.getString("source_connection_way"));
				}
				
				if(result.getString("source_ip")==null) {
					source_ip_list.add("");
				} else {
					source_ip_list.add(result.getString("source_ip"));
				}

				if(result.getString("source_connection_username")==null) {
					source_connection_username_list.add("");
				} else {
					source_connection_username_list.add(result.getString("source_connection_username"));
				}
				
				if(result.getString("source_folder")==null) {
					source_folder_list.add("");
				} else {
					source_folder_list.add(result.getString("source_folder"));
				}
				
				if(result.getString("source_subfolder")==null) {
					source_subfolder_list.add("");
				} else {
					source_subfolder_list.add(result.getString("source_subfolder"));
				}
			}		
			
			String[][] all_data = new String[source_connection_way_list.size()][5];
			for(int i=0;i<source_connection_way_list.size();i++) {
				all_data[i][0] = source_connection_way_list.get(i);
				all_data[i][1] = source_ip_list.get(i);
				all_data[i][2] = source_connection_username_list.get(i);
				all_data[i][3] = source_folder_list.get(i);
				all_data[i][4] = source_subfolder_list.get(i);
			}
					
			String[] header = {"connection_way","source_ip","connection_username","source_folder","folder_to_be_copied"};		
			jt = new JTable(all_data,header);
			jt.getTableHeader().setFont(new Font("Courier", Font.BOLD, 18));
			jt.setRowHeight(50);
			jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			TableColumnModel jt_model = jt.getColumnModel();
			jt_model.getColumn(0).setPreferredWidth(200);
			jt_model.getColumn(1).setPreferredWidth(250);
			jt_model.getColumn(2).setPreferredWidth(350);
			jt_model.getColumn(3).setPreferredWidth(350);
			jt_model.getColumn(4).setPreferredWidth(350);
			js = new JScrollPane(jt);
			js.setPreferredSize(new Dimension(1450,700));
			add_a_component(js, 0, 0, 10, 10, this, layout);
					
			jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {			
					if (jt.getSelectedRow() > -1) {
						iui.rrp.source_connection_way_combobox.setSelectedItem
						((String) jt.getValueAt(jt.getSelectedRow(), 0));
						iui.rrp.source_ip_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 1));
						iui.rrp.source_connection_username_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 2));
						iui.rrp.source_folder_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 3));
						iui.rrp.source_subfolder_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 4));
			        }
				}				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void add_a_component(Component comp,int gridx, 
			int gridy, int gridwidth, int gridheight, 
			Container top, GridBagLayout gridbag) {
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

class target_panel extends JPanel{

	private static final long serialVersionUID = 1L;
	public Import_UI iui;
	public JTable jt;
	public JScrollPane js;
	public JButton confirm;
	GridBagLayout layout;
		
	public target_panel(Import_UI iui) {
		
		layout = new GridBagLayout();
		this.iui = iui;
		this.setLayout(layout);
	    this.setBounds(iui.getBounds()); 
	    this.setVisible(true);
	    
	    UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Table.font", new Font("Courier", Font.BOLD, 18));
		
		try {
			Connection conn = Utils.connect_to_database();
			Statement stmt = conn.createStatement();
			String stmt_string = "use "+ Config.DB_database_name+";";
			stmt.executeUpdate(stmt_string);
			stmt_string = String.format("select distinct target_connection_way,"
			+ "target_ip,target_connection_username,target_folder "
			+ "from tasks where username='%s';"
			,iui.database_username);		
			
			ResultSet result = stmt.executeQuery(stmt_string);
			ArrayList<String> target_connection_way_list = new ArrayList<String>();
			ArrayList<String> target_ip_list = new ArrayList<String>();
			ArrayList<String> target_connection_username_list = new ArrayList<String>();
			ArrayList<String> target_folder_list = new ArrayList<String>();
			
			while (result.next()) {					
				if(result.getString("target_connection_way")==null) {
					target_connection_way_list.add("");
				} else {
					target_connection_way_list.add(result.getString("target_connection_way"));
				}
				
				if(result.getString("target_ip")==null) {
					target_ip_list.add("");
				} else {
					target_ip_list.add(result.getString("target_ip"));
				}

				if(result.getString("target_connection_username")==null) {
					target_connection_username_list.add("");
				} else {
					target_connection_username_list.add(result.getString("target_connection_username"));
				}
				
				if(result.getString("target_folder")==null) {
					target_folder_list.add("");
				} else {
					target_folder_list.add(result.getString("target_folder"));
				}
			}		
					
			String[][] all_data = new String[target_connection_way_list.size()][4];
			for(int i=0;i<target_connection_way_list.size();i++) {
				all_data[i][0] = target_connection_way_list.get(i);
				all_data[i][1] = target_ip_list.get(i);
				all_data[i][2] = target_connection_username_list.get(i);
				all_data[i][3] = target_folder_list.get(i);
			}
					
			String[] header = {"connection_way","target_ip","connection username","target_folder"};		
			jt = new JTable(all_data,header);
			jt.getTableHeader().setFont(new Font("Courier", Font.BOLD, 18));
			jt.setRowHeight(50);
			jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			TableColumnModel jt_model = jt.getColumnModel();
			jt_model.getColumn(0).setPreferredWidth(200);
			jt_model.getColumn(1).setPreferredWidth(250);
			jt_model.getColumn(2).setPreferredWidth(475);
			jt_model.getColumn(3).setPreferredWidth(475);
			js = new JScrollPane(jt);
			js.setPreferredSize(new Dimension(1450,700));
			jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (jt.getSelectedRow() > -1) {
						iui.rrp.dest_connection_way_combobox.setSelectedItem
						((String) jt.getValueAt(jt.getSelectedRow(), 0));
						iui.rrp.dest_ip_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 1));
						iui.rrp.dest_connection_username_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 2));
						iui.rrp.dest_folder_textfield.setText(
						(String) jt.getValueAt(jt.getSelectedRow(), 3));
			        }
				}			
			});
			
			add_a_component(js, 0, 0, 10, 10, this, layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void add_a_component(Component comp,int gridx, 
			int gridy, int gridwidth, int gridheight, 
			Container top, GridBagLayout gridbag) {
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
