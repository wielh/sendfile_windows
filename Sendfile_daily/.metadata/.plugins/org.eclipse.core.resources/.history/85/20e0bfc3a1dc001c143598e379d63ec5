package  chunchi.org.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashSet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONObject;

public class Utils {
	
	public static String windowspath_to_cwrsyncpath(String s) {
		String[] section = s.split("\\\\");
		StringBuilder result=new StringBuilder("/cygdrive/");
		result.append(section[0].substring(0, 1));
		if(section.length>1) {
			for(int i=1;i<section.length;i++) {
				result.append("/");
				result.append(section[i]);
			}
		}
		
		return result.toString();
	}
	
	
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
	
	//================================================================
	
	public static String Millisecond_to_String(long Millisecond) {
		long current_Millisecond = Millisecond;
		long days = current_Millisecond/(1000*60*60*24);
		current_Millisecond = current_Millisecond- days*1000*60*60*24;
		long hours = current_Millisecond/(1000*60*60);
		current_Millisecond = current_Millisecond-hours*1000*60*60;
		long minutes = current_Millisecond/(1000*60);
		current_Millisecond = current_Millisecond-minutes*1000*60;
		long seconds = current_Millisecond/1000;
		
		return String.format("%dD,%dH,%dM,%dS", days,hours,minutes,seconds);
	}
	
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
			
		int index = 0;
		Calendar a = (Calendar) first_next_time.clone();
		int day_difference = (int) ((first_next_time_long-today_long)/1000/60/60/24);
		while(day_difference < next_n_days) {
			next_times.add(a.getTimeInMillis()-today_long);
			a.set(Calendar.MONTH, first_next_time.get(Calendar.MONTH)+index);	
			day_difference = (int) ((a.getTimeInMillis()-today_long)/1000/60/60/24);
			index +=1;
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
	
	//===========================================================

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
	
	//==========================================================
		
	public static HashSet<Long> get_pids_of_rsync() throws Exception{	
		ArrayList<String> result_string = Utils.execute_windows_command
				("tasklist /FI \"imagename eq rsync.exe\"", new ArrayList<>(), true);
		HashSet<Long> result=new HashSet<>();
		
		for (String s:result_string) {
			String t=new String(s.getBytes(),StandardCharsets.UTF_8);
			if(t.startsWith("rsync.exe")) {
				t=t.substring(new String("rsync.exe").length(),t.length());
				while(t.startsWith(" ")) {
					t=t.substring(1,t.length());
				}
				
				String num="";
				for(String letter:t.split("")) {
					if(letter.equals(" ")) {
						result.add(Long.valueOf(num));
						break;
					} else {
						num+=letter;
					}						
				}					
			}
		}
		
		return result;	
	}
	
	public static ArrayList<String> execute_windows_command (String command, 
			ArrayList<String> infoList, boolean quiet) throws Exception{
		String[] commands= {"cmd.exe","/c",command};
		return execute_windows_command(commands, infoList, quiet);
	}
	
	public static ArrayList<String> execute_windows_command
		(String[] command, ArrayList<String> infoList,boolean quiet) throws Exception{
		
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader buf = null ;		
		Process pr;
				
		try {		
			pr = Runtime.getRuntime().exec(command);
			// pr.waitFor();
			// System.out.println("Process number:"+ Utils.getPidOfProcess(pr));			
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());
			result.add(sw.toString());
			return result;
		}
		
		try {
			buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				//append_string_to_sfui(sfui, line);
				if(!quiet) {
					System.out.println(line);
				}			
				infoList.add(line);
				result.add(line);
			}	
			buf.close();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());
			result.add(sw.toString());
			buf.close();
			
			return result;
		} 
		
	}
		
	public static ArrayList<String> execute_windows_rsync_command
	(String command, ArrayList<String> infoList, Single_upload_task task,boolean quiet) throws Exception{		
		String[] commands= {"cmd.exe","/c",command};
		return execute_windows_rsync_command(commands, infoList, task,quiet);
	}
	
	public static ArrayList<String>  execute_windows_rsync_command
	(String[] command, ArrayList<String> infoList,Single_upload_task task,boolean quiet) throws Exception{		
		
		BufferedReader buf = null ;		
		ArrayList<String> result = new ArrayList<String>();
		Process pr;
				
		try {		
			task.original_pid = Utils.get_pids_of_rsync();
			pr = Runtime.getRuntime().exec(command);
			Thread.sleep(300);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());
			result.add(sw.toString());		
			return result;
		}
		
		try {		
			task.all_pid = Utils.get_pids_of_rsync();
			buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));		
			String line = "";
			while ((line=buf.readLine())!=null) {
				if(!quiet) {
					System.out.println(line);
				}			
				infoList.add(line);
				result.add(line);
			}	
			buf.close();						
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());
			result.add(sw.toString());			
			return result;
		}
	}

	/*
	public static void append_string_to_sfui(Sending_file_UI sfui, String line) {
		System.out.println(line);	
		sfui.show_info.append(line+"\n");	
		sfui.show_info.update(sfui.show_info.getGraphics());
		sfui.scrollPane.update(sfui.scrollPane.getGraphics());
		sfui.validate();
	}*/

	//====================================================================

	
	
	public static void write_log(String workspace_path,JSONObject parameters,
			 ArrayList<String> infoList) {
		try {
			File f = new File(new File(workspace_path, (String)parameters.get("task_name")),"log.txt");
			//System.out.println(f.getAbsolutePath());
			FileWriter fw = new FileWriter(f.getAbsolutePath(),true);
			/*
			String command = String.format
					("echo %s|sudo chmod 777 %s/log.txt",linux_password, task_name);
			String[] cmd = {"/bin/bash","-c", command};	
			infoList = Utils.execute_linux_command(cmd, infoList);*/
			String end_time = "End time:"+
			new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
			infoList.add(end_time);
			
			for(String st: infoList) {
				fw.write("\n");		
				fw.write(st);
			}		
			
			fw.flush();
			fw.close();	
			return;
		} catch (Exception e) {
		    StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			System.out.println(sw.toString());
			e.printStackTrace();
		}
	}
}
