package chunchi.org.windows.BackEnd;

import java.io.*;
import java.sql.*;
import java.sql.Timestamp;

import org.json.simple.JSONObject;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.crypto.spec.PBEKeySpec;

import chunchi.org.windows.Config;

public class Utils {
	
	public static SecretKey get_AESkey_from_Config() {
		try {	
			byte[] salt= {1,2,3,4,5};
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecretKey tmp = factory.generateSecret(
					new PBEKeySpec(Config.AES_keyString.toCharArray(), salt , 65536, 256));
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");	
			return secret;
		} catch (Exception e) {
			return null;
		}
	}		
	
	public static String encrypted_by_AES(String raw) {		
		try {
			SecretKey key = get_AESkey_from_Config();
			byte[] all_data_from_file = Base64.getEncoder().encode(raw.getBytes());	
			Cipher desCipher = Cipher.getInstance("AES");
			desCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted_data = desCipher.doFinal(all_data_from_file);
			String encrypted_string= Base64.getEncoder().encodeToString(encrypted_data);
			return encrypted_string;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}	
	}

	public static String decrypted_by_AES(String encrypted) {
		try {
			SecretKey key = get_AESkey_from_Config();
			byte[] decrypted_byte = Base64.getDecoder().decode(encrypted);
			Cipher desCipher = Cipher.getInstance("AES");
			desCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted_byte2 = desCipher.doFinal(decrypted_byte);
			byte[] decrypted_byte3 = Base64.getDecoder().decode(decrypted_byte2);
			return new String(decrypted_byte3);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
			
	//=====================================================================
	
	public static String handle_null_string(String str) {
		if(str==null) {
			return "";
		} else {
			return str;
		}
	}
	
	public static String add_zero(int n) {
		if(n>=0 && n<10) {
			return "0"+String.valueOf(n);
		} else {
			return String.valueOf(n);
		}
	}
	
	//=====================================================================
	public static String Millisecond_to_String(long Millisecond) {
		if(Millisecond>0) {
			long current_Millisecond = Millisecond;
			long days = current_Millisecond/(1000*60*60*24);
			current_Millisecond = current_Millisecond- days*1000*60*60*24;
			long hours = current_Millisecond/(1000*60*60);
			current_Millisecond = current_Millisecond-hours*1000*60*60;
			long minutes = current_Millisecond/(1000*60);
			current_Millisecond = current_Millisecond-minutes*1000*60;
			long seconds = current_Millisecond/1000;
			
			return String.format("%dD,%dH,%dM,%dS", days,hours,minutes,seconds);
		} else {
			return "executed before";
		}		
	}
	
	//=================================================================
	public static ArrayList<Long> get_next_starttime_monthly
	(int minute, int hour, int day_of_month, int next_n_days) {
	
		Calendar today = Calendar.getInstance();
		Calendar first_next_time = Calendar.getInstance();
		
		if(day_of_month > today.get(Calendar.DAY_OF_MONTH)||
		   (day_of_month == today.get(Calendar.MONTH) 
		   && hour > today.get(Calendar.HOUR_OF_DAY)) ||
		   (day_of_month == today.get(Calendar.MONTH) 
		   && hour == today.get(Calendar.HOUR_OF_DAY))
		   && minute > today.get(Calendar.MINUTE)) {
			first_next_time.set(Calendar.MONTH, today.get(Calendar.MONTH));
			first_next_time.set(Calendar.DAY_OF_MONTH, day_of_month);
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		} else {
			first_next_time.set(Calendar.MONTH, today.get(Calendar.MONTH)+1);
			first_next_time.set(Calendar.DAY_OF_MONTH, day_of_month);
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		}
		
		ArrayList<Long> next_times = new ArrayList<Long>();
		long today_long = Calendar.getInstance().getTimeInMillis();
		long first_next_time_long = first_next_time.getTimeInMillis();
			
		Calendar a = (Calendar) first_next_time.clone();
		int day_difference = (int) ((first_next_time_long-today_long)/1000/60/60/24);
		while(day_difference < next_n_days) {
			next_times.add(a.getTimeInMillis()-today_long);
			a.set(Calendar.MONTH, a.get(Calendar.MONTH)+1);	
			day_difference = (int) ((a.getTimeInMillis()-today_long)/1000/60/60/24);
		}
		return next_times;
	}
	
	public static ArrayList<Long> get_next_starttime_weekly
		(int minute, int hour, String weekday, int next_n_days) {
		
		int weekday_int;
		switch (weekday) {
		case "一":
			weekday_int = Calendar.MONDAY;
			break;
		case "二":
			weekday_int = Calendar.TUESDAY;
			break;
		case "三":
			weekday_int = Calendar.WEDNESDAY;
			break;
		case "四":
			weekday_int = Calendar.THURSDAY;
			break;
		case "五":
			weekday_int = Calendar.FRIDAY;
			break;
		case "六":
			weekday_int = Calendar.SATURDAY;
			break;
		default:
			weekday_int = Calendar.SUNDAY;
		}
		
		Calendar today = Calendar.getInstance();
		Calendar first_next_time = Calendar.getInstance();
		
		if( weekday_int > today.get(Calendar.DAY_OF_WEEK)||
		   (weekday_int == today.get(Calendar.DAY_OF_WEEK) 
		   && hour > today.get(Calendar.HOUR_OF_DAY)) ||
		   (weekday_int == today.get(Calendar.DAY_OF_WEEK) 
		   && hour == today.get(Calendar.HOUR_OF_DAY))
		   && minute > today.get(Calendar.MINUTE)) {
			first_next_time.set(Calendar.WEEK_OF_YEAR, today.get(Calendar.WEEK_OF_YEAR));
			first_next_time.set(Calendar.DAY_OF_WEEK, weekday_int);
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		} else {
			first_next_time.set(Calendar.WEEK_OF_YEAR, today.get(Calendar.WEEK_OF_YEAR)+1);
			first_next_time.set(Calendar.DAY_OF_WEEK, weekday_int);
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		}
		
		long today_long = Calendar.getInstance().getTimeInMillis();
		long first_next_time_long = first_next_time.getTimeInMillis();
		int day_difference = (int) ((first_next_time_long-today_long)/1000/60/60/24);
	
		ArrayList<Long> next_times = new ArrayList<Long>();
		for(int i=0;7*i+day_difference<next_n_days;i++) {
			Calendar a = (Calendar) first_next_time.clone();
			a.set(Calendar.WEEK_OF_YEAR, first_next_time.get(Calendar.WEEK_OF_YEAR)+i);
			next_times.add(a.getTimeInMillis()-today_long);
		}
	
		return next_times;
	}
	
	public static ArrayList<Long> get_next_starttime_daily
		(int minute, int hour, int next_n_days) {
		
		Calendar first_next_time = Calendar.getInstance();
		if(hour > Calendar.getInstance().get(Calendar.HOUR)||
		  (hour == Calendar.getInstance().get(Calendar.HOUR) 
		   && minute > Calendar.getInstance().get(Calendar.MINUTE))) {
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		} else {
			first_next_time.set(Calendar.DAY_OF_MONTH,
				  Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1);
			first_next_time.set(Calendar.HOUR_OF_DAY, hour);
			first_next_time.set(Calendar.MINUTE, minute);
			first_next_time.set(Calendar.SECOND, 0);
		}
		
    	ArrayList<Long> next_times = new ArrayList<Long>();
    	for(int i=0;i<next_n_days;i++) {
    		Calendar a = (Calendar) first_next_time.clone();
    		a.set(Calendar.DAY_OF_MONTH, first_next_time.get(Calendar.DAY_OF_MONTH)+i);
    		next_times.add(a.getTimeInMillis()-Calendar.getInstance().getTimeInMillis());
    	}
	
    	return next_times;
	}
	//========================================================
	public static String hash_string(String plaintext) {
		MessageDigest abc;
		try {
			abc = MessageDigest.getInstance("SHA-256");
			String salted_String = plaintext + "@#$%" +plaintext;
			abc.update(salted_String.getBytes());
			return Base64.getEncoder().encodeToString(abc.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	public static void delete_folder(File f) {
		if(f.isDirectory()) {
			for(File g:f.listFiles()) {
				delete_folder(g);
			}
			f.delete();
		} else if(f.isFile()){
			f.delete();
		}
	}
	
	public static String check_format(JSONObject parameters) {	
		
		if(parameters.get("execute_timing").equals("periodical")) {
			try {
				String period_string = (String) parameters.get("period");
				if(period_string.equals("monthly")) {
					int k = Integer.parseInt((String) parameters.get("day_of_month"));	
					if(k<0 || k>=32) {
						return "The input of week block must be in 1~31";
					}			
				} 
			} catch (Exception e) {
				return "The input of month block must be a integer.";
			}
			
			try {
				int minute = Integer.parseInt((String) parameters.get("minute"));
				if(minute<0 || minute>=60) {
					return "The input of minute must be in 0~59";
				} 
			} catch (Exception e) {
				return "The input of minute block must be a integer.";
			}
					
			try {
				int hour = Integer.parseInt((String) parameters.get("hour"));
				if(hour<0 || hour>=24) {
					return "The input of hour must be in 0~23";
				} 
			} catch (Exception e) {
				return "The input of hour block must be a integer.";
			}
			
			try {
				int minute = Integer.parseInt((String) parameters.get("minute"));
				if(minute<0 || minute>=60) {
					return "The input of minute must be in 0~59";
				} 
			} catch (Exception e) {
				return "The input of minute block must be a integer.";
			}
			
			try {
				float timeout = Float.valueOf((String) parameters.get("timeout"));
				String period_string = (String) parameters.get("period");
				
				if(period_string.equals("monthly")) {				
					if(timeout<0 || timeout>=28*24) {
						return "The value of timeout excess.";
					}			
				} else if(period_string.equals("weekly")) {
					if(timeout<0 || timeout>=7*24) {
						return "The value of timeout excess.";
					}
				} else {
					if(timeout<0 || timeout>=24) {
						return "The value of timeout excess.";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "The input of timeout is not a number.";
			}
		} 
					
		String source_connection_way = (String) parameters.get("source_connection_way");
		String source_ip;
		String source_mount_disk;
		String source_connection_username;
		String source_connection_password;
		String source_folder;
		String source_subfolder;
		if(source_connection_way.equals("temporary cifs connection")) {	
			source_ip = (String) parameters.get("source_ip");
			if(source_ip==null||source_ip.length()==0) {
				return "Please input source_ip";
			}		
			source_connection_username = (String) parameters.get("source_connection_username");
			if(source_connection_username == null||source_connection_username.length()==0) {
				return "Please input source_connection_username";
			}		
			source_connection_password = (String) parameters.get("encrypted_source_connection_password");
			if(source_connection_password==null||source_connection_password.length()==0) {
				return "Please input source_connection_password";
			}
			source_mount_disk = (String) parameters.get("source_mount_disk");
			String format = "[a-zA-Z]:";
			if(source_mount_disk==null || source_mount_disk.length()==0) {
				return "Please input source_mount_disk";
			} else if(!source_mount_disk.matches(format)) {
				return "The format of source_mount_disk is incorrect, format: \"[a-zA-Z]:\"";
			} else if (source_mount_disk.substring(0,1).equals("c")||
					   source_mount_disk.substring(0,1).equals("C")) {
				return "C disk is the default system disk for windows";
			}
			
			source_folder = (String) parameters.get("source_folder");
			if(source_folder.length()==0) {
				return "Please input source_folder";
			}
			source_subfolder= (String) parameters.get("source_subfolder");
			if(source_subfolder.length()==0) {
				return "Please input source_subfolder";
			};
		} else if(source_connection_way.equals("temporary nfs connection")){
			source_ip = (String) parameters.get("source_ip");
			if(source_ip.length()==0) {
				return "Please input source_ip";
			}		
			source_folder = (String) parameters.get("source_folder");
			if(source_folder.length()==0) {
				return "Please input source_folder";
			}
			source_subfolder= (String) parameters.get("source_subfolder");
			if(source_subfolder.length()==0) {
				return "Please input source_subfolder";
			};
			
			source_mount_disk = (String) parameters.get("source_mount_disk");
			String format = "[a-zA-Z]:";
			if (source_mount_disk==null||source_mount_disk.length()==0){
				return "Please input source_mount_disk";
			} else if(!source_mount_disk.matches(format)) {
				return "The format of source_mount_disk is incorrect, format: \"[a-zA-Z]:\"";
			} else if (source_mount_disk.substring(0,1).equals("c")||
					   source_mount_disk.substring(0,1).equals("C")) {
				return "C disk is the default system disk for windows";
			}
		}
		
		String target_connection_way = (String) parameters.get("target_connection_way");
		String target_ip;
		String target_connection_username;
		String target_connection_password;
		String target_mount_disk;
		String target_folder;

		if(target_connection_way.equals("temporary cifs connection")) {	
			target_ip = (String) parameters.get("target_ip");
			if(target_ip==null||target_ip.length()==0) {
				return "Please input target_ip";
			}		
			target_connection_username = (String) parameters.get("target_connection_username");
			if(target_connection_username.length()==0) {
				return "Please input target_connection_username";
			}		
			target_connection_password = (String) parameters.get("encrypted_target_connection_password");
			if(target_connection_password==null||target_connection_password.length()==0) {
				return "Please input target_connection_password";
			}
			target_folder = (String) parameters.get("target_folder");
			if(target_folder==null||target_folder.length()==0) {
				return "Please input target_folder";
			}
			
			target_mount_disk = (String) parameters.get("target_mount_disk");
			String format = "[a-zA-Z]:";
			if(target_mount_disk==null||target_mount_disk.length()==0) {
				return "Please input target_mount_disk";
			} else if(!target_mount_disk.matches(format)) {
				return "The format of source_mount_disk is incorrect, format: \"[a-zA-Z]:\"";
			} else if (target_mount_disk.substring(0,1).equals("c")||
					   target_mount_disk.substring(0,1).equals("C")) {
				return "C disk is the default system disk for windows";
			}
		} else if(target_connection_way.equals("temporary nfs connection")){
			target_ip = (String) parameters.get("target_ip");
			if(target_ip==null||target_ip.length()==0) {
				return "Please input target_ip";
			}		
			target_folder = (String) parameters.get("target_folder");
			if(target_folder==null||target_folder.length()==0) {
				return "Please input target_folder";
			}
			
			target_mount_disk = (String) parameters.get("target_mount_disk");
			String format = "[a-zA-Z]:";
			if(!target_mount_disk.matches(format)) {
				return "The format of source_mount_disk is incorrect, format: \"[a-zA-Z]:\"";
			} else if (target_mount_disk.substring(0,1).equals("c")||
					   target_mount_disk.substring(0,1).equals("C")) {
				return "C disk is the default system disk for windows";
			}
		} 
		
		return "";
	}
		
	public static Connection connect_to_database() {
		Connection conn = null;
		try {
			Class.forName(Config.JDBC_DRIVER);
			String DB_URL = Config.DB_URL_Part1
						   +Config.DB_ip + Config.DB_database_name;
			conn = DriverManager.getConnection(DB_URL,
					Config.root, Config.root_password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
		return conn;
	}
	
	public static String write_to_tasks(JSONObject parameters){
			
		Statement stmt = null;
		Connection conn = connect_to_database();
		if(conn!=null) {
			System.out.println("connect success");
		} else {
			System.out.println("Unable to connect to database");
			return "Unable to connect to database";
		}
			
		try {
			stmt = conn.createStatement();
			String stmt_string = "USE "+ Config.DB_database_name +" ;";
			stmt.executeUpdate(stmt_string);
			stmt_string =  String.format("select count(*) from tasks "
					+ "where username = '%s' and task_name= '%s'",
					(String)parameters.get("database_username"),
					(String)parameters.get("task_name"));
			
			ResultSet rs = stmt.executeQuery(stmt_string);		
			if(rs.next() && rs.getInt(1) > 0) {
				rs.close();
				stmt.close();
				conn.close();
				System.out.println("The tasks is already exists.");
				return "The tasks is already exists.";
			}	
			
			String source_connection_way_string = (String) parameters.get("source_connection_way");
			String target_connection_way_string = (String) parameters.get("target_connection_way");
			String source_folder = (String)parameters.get("source_folder");
			String target_folder = (String)parameters.get("target_folder");
			String current_time = new Timestamp(System.currentTimeMillis()).toString();
			
			stmt_string = String.format("INSERT INTO tasks("
			+ "username,task_name,createdatetime,"
			+ "execute_timing,timeout,move_or_copy,"
			+ "source_connection_way, source_folder,"
			+ "target_connection_way, target_folder) values "
			+ "('%s','%s','%s','%s',%f,'%s','%s','%s','%s','%s')", 
			(String)parameters.get("database_username"),
			(String)parameters.get("task_name"),
			current_time,
			(String)parameters.get("execute_timing"),
			Float.parseFloat((String)parameters.get("timeout")),
			(String)parameters.get("move_or_copy"),
			source_connection_way_string,
			source_folder.replace("\\", "\\\\"),
			target_connection_way_string,
			target_folder.replace("\\", "\\\\"));	
			stmt.executeUpdate(stmt_string);
			
			if(parameters.get("execute_timing").equals("periodical")) {			
				String period_string = (String)parameters.get("period");
				stmt_string = String.format("UPDATE tasks "
						+ "SET period='%s',hour=%d,minute=%d "
						+ "where username='%s' and task_name='%s'", 
						period_string,
						Integer.parseInt((String)parameters.get("hour")),
						Integer.parseInt((String)parameters.get("minute")),
						(String)parameters.get("database_username"),
				    	(String)parameters.get("task_name"));
				stmt.executeUpdate(stmt_string);
				if(period_string.equals("monthly")) {
					stmt_string = String.format("UPDATE tasks "
							+ "SET day_of_month='%s' where username='%s' and task_name='%s'", 
							(String)parameters.get("day_of_month"),
							(String)parameters.get("database_username"),
					    	(String)parameters.get("task_name"));
					stmt.executeUpdate(stmt_string);
				} else if(period_string.equals("")) {
					stmt_string = String.format("UPDATE tasks "
							+ "SET day_of_week='%s' where username='%s' and task_name='%s'", 
							(String)parameters.get("day_of_week"),
							(String)parameters.get("database_username"),
					    	(String)parameters.get("task_name"));
					stmt.executeUpdate(stmt_string);
				} 
			} 
									
			String source_subfolder;
			if(source_connection_way_string.equals("temporary cifs connection")) {
				source_subfolder = (String)parameters.get("source_subfolder");
				stmt_string = String.format("UPDATE tasks "
						+ "SET source_ip='%s', source_connection_username='%s',"
						+ "encrypted_source_connection_password='%s',"
						+ "source_mount_disk='%s',source_subfolder='%s' "
						+ "where username='%s' and task_name='%s'", 
				    	(String)parameters.get("source_ip"),
				    	(String)parameters.get("source_connection_username"),
				    	(String)parameters.get("encrypted_source_connection_password"),
				    	(String)parameters.get("source_mount_disk"),
				    	source_subfolder.replace("\\", "\\\\"),
				    	(String)parameters.get("database_username"),
				    	(String)parameters.get("task_name"));
						stmt.executeUpdate(stmt_string);
			} else if(source_connection_way_string.equals("temporary nfs connection")){
				source_subfolder = (String)parameters.get("source_subfolder");
				stmt_string = String.format("UPDATE tasks "
						+ "SET source_ip='%s',source_subfolder='%s',source_mount_disk='%s' "
						+ "where username='%s' and task_name='%s'", 				
				    	(String)parameters.get("source_ip"),
				    	source_subfolder.replace("\\", "\\\\"),
				    	(String)parameters.get("source_mount_disk"),
				    	(String)parameters.get("database_username"),
				    	(String)parameters.get("task_name"));
						stmt.executeUpdate(stmt_string);	
			}	
			
			if(target_connection_way_string.equals("temporary cifs connection")) {
				stmt_string = String.format("UPDATE tasks "
						+ "SET target_ip='%s',target_connection_username='%s', "
						+ "encrypted_target_connection_password='%s',target_mount_disk='%s' "
						+ "where username='%s' and task_name='%s'", 
				    	(String)parameters.get("target_ip"),
				    	(String)parameters.get("target_connection_username"),
				    	(String)parameters.get("encrypted_target_connection_password"),
				    	(String)parameters.get("target_mount_disk"),
				    	(String)parameters.get("database_username"),
				    	(String)parameters.get("task_name"));
						stmt.executeUpdate(stmt_string);
			} else if(target_connection_way_string.equals("temporary nfs connection")){
				stmt_string = String.format("UPDATE tasks "
						+ "SET target_ip='%s',target_mount_disk='%s' "
						+ "where username='%s' and task_name='%s'", 
					 	(String)parameters.get("target_ip"),
					 	(String)parameters.get("target_mount_disk"),
						(String)parameters.get("database_username"),
				    	(String)parameters.get("task_name"));
						stmt.executeUpdate(stmt_string);	
			}
				
			stmt.close();
			conn.close();	
			System.out.println("Insert a row to table tasks successfully.");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some error happens when we insert a row to database");
			return "Some error happens when we insert a row to database";
		}
		
	}
	
	public static String remove_from_tasks(JSONObject parameters){	
		
		Statement stmt = null;
		Connection conn = connect_to_database();
		if(conn!=null) {
			System.out.println("connect success");
		} else {
			System.out.println("Unable to connect to database");
			return "Unable to connect to database";
		}
			
		try {
			stmt = conn.createStatement();
			String stmt_string = "USE "+ Config.DB_database_name +" ;";
			stmt.executeUpdate(stmt_string);		
			stmt_string = String.format("DELETE FROM tasks WHERE username = '%s' "
			+ "AND task_name='%s';",
			(String)parameters.get("database_username"),
			(String)parameters.get("task_name"));			
			stmt.executeUpdate(stmt_string);
			stmt.close();
			conn.close();
			System.out.println("Delete a row to table tasks successfully.");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "Some error happens when we delete a row from database";
		}	
		
	}

	//============================================================
	
	public static ArrayList<String> execute_windows_command (String command) throws Exception{
		String[] commands= {"cmd.exe","/c",command};
		return execute_windows_command(commands);
	}
	
	public static ArrayList<String> execute_windows_command(String[] command) throws Exception{
		
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader buf = null ;		
		Process pr;
				
		try {		
			pr = Runtime.getRuntime().exec(command);		
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			//infoList.add(sw.toString());
			result.add(sw.toString());
			return result;
		}
		
		try {
			buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				//if(!quiet) {
					System.out.println(line);
				//}			
				//infoList.add(line);
				result.add(line);
			}	
			buf.close();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			//infoList.add(sw.toString());
			result.add(sw.toString());
			buf.close();
			
			return result;
		} 	
	}
	
	//============================================================
	
 	public static void set_and_show_error(String info,boolean withUI) {
		 if(withUI) {
			 JOptionPane.showConfirmDialog(null, info, 
					 "error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); 
		 } else {
			System.out.println(info);
		 }
	}	
	
	public static void set_and_show_warning(String info,boolean withUI) {
		 if(withUI) {
			 JOptionPane.showConfirmDialog(null, info, 
					 "warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		} else {
			System.out.println(info);
		 }
		 
	}
	
	public static void set_and_show_info(String info,boolean withUI) {
		 if(withUI) {
		 JOptionPane.showConfirmDialog(null, info, 
			"information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
		 } else {
			System.out.println(info);
		 }
	}

}
