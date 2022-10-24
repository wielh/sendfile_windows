package chunchi.org.windows.BackEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;
import chunchi.org.windows.App;

public class EditTask {
	
	public static void create_task_and_execute_now(JSONObject parameters,boolean withUI) {				
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space, 
				(String) parameters.get("task_name"));
		if(!task_folder.exists()) {
			task_folder.mkdirs();
		}
		
		String error_message = Utils.write_to_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}		
		
		error_message = create_bat(parameters,withUI);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}
	
		error_message = create_log(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};	
		
		// execute it immediately.
		File parent_path = new File(current_working_space,
				(String) parameters.get("task_name")) ;
		File bat_file = new File(parent_path,"script.bat");
					
		/*String cmd = String.format("echo \"%s\"| runas /user:%s cmd",
				(String) parameters.get("computer_username"),
				(String) parameters.get("computer_password"));
		//String[] command = {"cmd.exe","/c",cmd};	
		try {		
			Process pr = Runtime.getRuntime().exec(command);
			pr.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
				
		System.out.println("start executing script");
		String cmd = String.format("\"%s\"",  bat_file.getAbsolutePath());	
		try {		
			Utils.execute_windows_command(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		remove_from_schtasks(parameters);
	}
	
	public static void modify_task_and_execute_now(JSONObject parameters,boolean withUI) {
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space,
				(String) parameters.get("task_name"));
		if(!task_folder.exists()) {
			task_folder.mkdirs();
		}
								
		String error_message = Utils.remove_from_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};
		
		error_message = Utils.write_to_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};
		
		error_message = create_bat(parameters,withUI);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}
		
		// execute it immediately.
		File parent_path = new File(current_working_space,
				(String) parameters.get("task_name")) ;
		File bat_file = new File(parent_path,"script.bat");
		
		String cmd = String.format("\"%s\"", bat_file.getAbsolutePath());
		System.out.println("start executing script");
		try {		
			Utils.execute_windows_command(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		remove_from_schtasks(parameters);
	}
	
	public static void delete_once_task(JSONObject parameters,boolean withUI) {
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space,
				(String) parameters.get("task_name"));

		Utils.remove_from_tasks(parameters);
		Utils.delete_folder(task_folder);	
		remove_from_schtasks(parameters);
		
		Utils.set_and_show_info("Delete a task successfully.",withUI);	
	}
	
	public static void create_task_periodical(JSONObject parameters,boolean withUI) {				
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space, 
				(String) parameters.get("task_name"));
		if(!task_folder.exists()) {
			task_folder.mkdirs();
		}
		
		String error_message = Utils.write_to_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}		
		error_message = create_bat(parameters,withUI);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}
		error_message = create_log(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};	
		error_message = add_to_schtasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};	
		Utils.set_and_show_info("Create a task successfully.",withUI);	
	}
	
	public static void modify_task_periodical(JSONObject parameters,boolean withUI) {
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space,
				(String) parameters.get("task_name"));
		if(!task_folder.exists()) {
			task_folder.mkdirs();
		}
								
		String error_message = Utils.remove_from_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};
		
		error_message = Utils.write_to_tasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};
		
		error_message = create_bat(parameters,withUI);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		}
		
		error_message = add_to_schtasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
			return;
		};
		
		Utils.set_and_show_info("Modify a task successfully.",withUI);
	}
	
	public static void delete_periodical_task(JSONObject parameters,boolean withUI) {
		File current_working_space;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			Utils.set_and_show_error("Unable to locate the dictionary of jar files.",withUI);
			return;
		}
		
		File task_folder = new File(current_working_space,
				(String) parameters.get("task_name"));
		
		String error_message = remove_from_schtasks(parameters);
		if(!error_message.equals("")) {
			Utils.set_and_show_error(error_message,withUI);
		};	
		Utils.remove_from_tasks(parameters);
		Utils.delete_folder(task_folder);
		Utils.set_and_show_info("Delete a task successfully.",withUI);
	}
	//===============================================
	
	// In window, we communicate with task manager instead of crontab	
	public static String add_to_schtasks(JSONObject parameters) {
			
			File current_working_space;
			try {
				current_working_space = new File(App.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI()).getParentFile();
			} catch (URISyntaxException e1) {
				return "Unable to locate the dictionary of jar files.";
			}
			
			File script_location = new File(current_working_space, 
					(String)parameters.get("task_name")+"\\script.bat");	
			String add_task ="";
			if(parameters.get("period").equals("monthly")) {
				int day = Integer.parseInt((String)parameters.get("day_of_month"));
				int hour = Integer.parseInt((String)parameters.get("hour"));
				int minute = Integer.parseInt((String)parameters.get("minute"));
				add_task = String.format("schtasks /create /f "
						+ "/tn \"%s\"  /tr \"'%s'\" /sc monthly /mo 1 /d %s /st %s:%s", 
				 		parameters.get("task_name"), 
				 		script_location.getAbsolutePath(), 
				 		day, Utils.add_zero(hour), Utils.add_zero(minute)); 	
			} else if(parameters.get("period").equals("weekly")) {
				int hour = Integer.parseInt((String)parameters.get("hour"));
				int minute = Integer.parseInt((String)parameters.get("minute"));
				String weekday = "SUN";
				switch ((String)parameters.get("day_of_week")) {
				case "1" :	
					weekday = "MON";
					break;
				case "2" :	
					weekday = "TUE";
					break;
				case "3" :	
					weekday = "WED";
					break;
				case "4" :	
					weekday = "THU";
					break;
				case "5" :		
					weekday = "FRI";
					break;
				case "6" :		
					weekday = "SAT";
					break;
				default:
					break;
				}
				add_task = String.format("schtasks /create /f "
						+ "/tn \"%s\"  /tr \"'%s'\" "
				 		+ "/sc weekly /mo 1 /d %s /st %s:%s", 
				 		parameters.get("task_name"),
				 		script_location.getAbsolutePath(), 
				 		weekday, Utils.add_zero(hour), Utils.add_zero(minute));
			} else if(parameters.get("period").equals("daily")){
				int hour = Integer.parseInt((String)parameters.get("hour"));
				int minute = Integer.parseInt((String)parameters.get("minute"));
				add_task = String.format("schtasks /create /f "
						+ "/tn \"%s\"  /tr \"'%s'\" /sc daily /mo 1 /st %s:%s", 
				 		parameters.get("task_name"),
				 		script_location.getAbsolutePath(), 
				 		Utils.add_zero(hour), Utils.add_zero(minute));
			}				
			//System.out.println("command:"+add_task);			
			//ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", add_task);
			//builder.redirectErrorStream(true);
			Process p;
			try {
				 p = Runtime.getRuntime().exec(add_task);
				 p.waitFor();
				 BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				 String line;
				 while (true) {					
		            line = r.readLine();
		            if (line == null) {break;}
		            System.out.println(line);
				 }
			} catch (Exception e) {
				e.printStackTrace();
				return "Some error happens when we add task to task scheduler.";
			}
			 
			 return "";		
		}
		
	public static String remove_from_schtasks(JSONObject parameters) {
			 String remove_task = String.format("SCHTASKS /Delete /f /TN %s",
				 		parameters.get("task_name") );
			
			 ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", remove_task);
			 builder.redirectErrorStream(true);
			 Process p;
			 try {
				 p = builder.start();
				 BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				 String line;
				 while (true) {
		            line = r.readLine();
		            if (line == null) { break; }
		            System.out.println(line);
				 }
			 } catch (IOException e) {
				e.printStackTrace();
				return "Some error happens when we add task to task scheduler.";
			 }
			 return "";
		}
		
		// In window create_sh should be changed to create_bat instead
		// TODO 1.get username 2.what is /c in cmd?
	public static String create_bat(JSONObject parameters,boolean withUI) {
			File current_working_space = null;
			try {
				current_working_space = new File(App.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI()).getParentFile();
			} catch (URISyntaxException e) {
				return "Unable to locate the dictionary of jar files.";
			}
			
			File parent_path;
			File bat_file;
			FileWriter fw;
		
			try {
				parent_path = new File(current_working_space,
						(String) parameters.get("task_name")) ;
				bat_file = new File(parent_path,"script.bat");
				System.out.println("Start create batch file");
				System.out.println(bat_file.getAbsolutePath());
				fw = new FileWriter(bat_file);
				//fw.write("@echo off");
				//fw.write("\n");
				//fw.write(String.format("echo \"%s\"| runas /user:%username% cmd",
				//		(String) parameters.get("computer_password")));
				//fw.write("\n");
				fw.write(String.format("java -jar \"%s\\sendfile.jar\""
						+ " \"%s\"  \"%s\"", 
					current_working_space.getAbsolutePath(),			
					(String) parameters.get("database_username"),
					(String) parameters.get("task_name")));
				
				if(parameters.get("force_umount").equals("true")) {
					fw.write(" --force_umount");
				}
				
				if(!withUI) {
					fw.write(" --withoutUI");
				}
							
				fw.flush();
				fw.close();
				System.out.println("create bat successfully");
			} catch (IOException e) {
				return "Unable to  write bat file.";
			}
			
			return "";
		
	}
	
	public static String create_log(JSONObject parameters){	
		
		File current_working_space = null;
		try {
			current_working_space = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			return "Unable to locate the dictionary of jar files.";
		}
			
		try {	
			System.out.println("start create log.txt");
			File parent_path = new File(current_working_space,(String) parameters.get("task_name")) ;
			FileWriter fw = new FileWriter(new File(parent_path, "log.txt"));
			String timeStamp = new SimpleDateFormat
		    		("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
			fw.write(String.format("===create datetime:%s===", timeStamp));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			return "Unable to create log file.";
		}
		
		return "";
	}
	

}
