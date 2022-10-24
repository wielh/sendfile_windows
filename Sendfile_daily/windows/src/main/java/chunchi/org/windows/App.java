package chunchi.org.windows;

import java.sql.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONObject;
import chunchi.org.windows.UI.Sending_file_UI;

/**
 * Hello world!
 *
 */
public class App {
	
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
    	
      	if(args.length < 2) {
      		System.out.println("The argument must be as follow:");
      		System.out.println("java -jar <jar> db_user task_name");
      		System.out.println("You can add --force_umount and --withoutUI");
      	} else {   		
      		//get parameters
      		String database_username = args[0];;	
      		String task_name = args[1];   		
      		boolean force_umount = false;
      		boolean withUI = true;
      				
      		if(args.length>3) {
      			for(int i=3;i<args.length;i++) {
      				if(args[i].equals("--force_umount")) {
      					force_umount = true;
      				} else if(args[i].equals("--withoutUI")) {
      					withUI = false;
      				} 
      			}
			}
      		   		     					
      		ArrayList<String> infoList = new ArrayList<String>();
        	String start_time = "Start time:"+
        	new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
        	infoList.add(start_time);
        	
        	String workspace_path = "";
        	try {
    			workspace_path = new File(App.class.getProtectionDomain()
    					.getCodeSource().getLocation().toURI()).getParentFile().getAbsolutePath();
    		} catch (URISyntaxException e1) {
    			e1.printStackTrace();
    			return;
    		}
        	
        	JSONObject parameters = new JSONObject();
        	Connection conn;
    		Statement stmt;
        	ResultSet rs;
        	String stmt_string = "";
        	try {       		
        		conn = Utils.connect_to_database();
        		stmt = conn.createStatement();
        		System.out.println("Connect to database successfully.");        		
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to connect to database");
				return;
			}
        	 	     	
        	try { 	
        		stmt_string = String.format(
        				"select * from tasks where username='%s' and task_name = '%s';"
        				,database_username, task_name);		
        		rs = stmt.executeQuery(stmt_string);
        		parameters.put("force_umount", force_umount);
    	        if (rs.next()) {
    	        	parameters.put("database_username", database_username);
    	        	parameters.put("task_name", task_name);
    	        	parameters.put("timeout", rs.getFloat("timeout"));
    	        	parameters.put("move_or_copy", rs.getString("move_or_copy"));
    	        	   	        	
    	        	parameters.put("source_connection_way",rs.getString("source_connection_way"));       	
    	        	if(rs.getString("source_connection_way").equals("temporary cifs connection")) {
    	        		parameters.put("source_ip", rs.getString("source_ip"));
    	        		//TODO
    	        		parameters.put("source_mount_disk", rs.getString("source_mount_disk"));
        	        	parameters.put("source_connection_username", rs.getString("source_connection_username"));
        	        	parameters.put("source_connection_password",
        	        			Utils.decrypted_by_AES(rs.getString("encrypted_source_connection_password")));
        	        	parameters.put("source_subfolder", rs.getString("source_subfolder"));
    	        	} else if(rs.getString("source_connection_way").equals("temporary nfs connection")) {
    	        		//TODO
    	        		parameters.put("source_mount_disk", rs.getString("source_mount_disk"));
    	        		parameters.put("source_ip", rs.getString("source_ip"));
    	        		parameters.put("source_subfolder", rs.getString("source_subfolder"));
    	        	}
    	        	parameters.put("source_folder",rs.getString("source_folder"));
    	        	  	        	
    	        	parameters.put("target_connection_way",rs.getString("target_connection_way"));
    	        	if(rs.getString("target_connection_way").equals("temporary cifs connection")) {
    	        		//TODO
    	        		parameters.put("target_mount_disk", rs.getString("target_mount_disk"));
    	        		parameters.put("target_ip", rs.getString("target_ip"));
        	        	parameters.put("target_connection_username", rs.getString("target_connection_username"));
        	        	parameters.put("target_connection_password", 
        	        			Utils.decrypted_by_AES(rs.getString("encrypted_target_connection_password")));
    	        	} else if(rs.getString("target_connection_way").equals("temporary nfs connection")) {
    	        		//TODO
    	        		parameters.put("target_mount_disk", rs.getString("target_mount_disk"));
    	        		parameters.put("target_ip", rs.getString("target_ip"));
    	        	}
    	        	parameters.put("target_folder", rs.getString("target_folder"));
    	   	
    	        	System.out.println("Get all information to upload data.");
				} else {
					System.out.println("...");
				}
        	} catch (Exception e) {
        		e.printStackTrace();
    			StringWriter sw = new StringWriter();
    			e.printStackTrace(new PrintWriter(sw));
    			infoList.add(sw.toString());
    			return;
    		}
        	   
        	try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
    			e.printStackTrace(new PrintWriter(sw));
    			infoList.add(sw.toString());
			}
        	      	
        	if(withUI) {
        		try {       	   
    				new Sending_file_UI(workspace_path, parameters ,infoList);         
            	} catch (Exception e) {
        			StringWriter sw = new StringWriter();
        			e.printStackTrace(new PrintWriter(sw));
        			infoList.add(sw.toString());
        			Utils.write_log(workspace_path,parameters,infoList);
        		} 
        	} else {
        		try {
        			System.out.println("copy data to another computer");
                 	infoList.add("copy data to another computer");
                 	Single_upload_task task = new Single_upload_task(workspace_path, parameters, infoList);
            		task.start();     
            	} catch (Exception e) {
        			StringWriter sw = new StringWriter();
        			e.printStackTrace(new PrintWriter(sw));
        			infoList.add(sw.toString());
        			Utils.write_log(workspace_path,parameters,infoList);
        		}  
        	} 	     	
      	}
    	
    }
  
}



