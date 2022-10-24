package chunchi.org.windows;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

public class Single_upload_task {
	
	String workspace_path="";
	JSONObject parameters;

	HashSet<Long> original_pid = new HashSet<>();
	HashSet<Long> all_pid = new HashSet<>();
	
	ArrayList<String> infoList;
	public boolean complete_copy = true;

	
	String source_mount_disk;
	String target_mount_disk;
	File source_mount_disk_file;
	File target_mount_disk_file;
	
	ArrayList<File> all_files_to_be_copied = new ArrayList<File>();
	int index_of_finish_copy = 0;
	int number_of_errors_during_copy = 0;
	
	public Single_upload_task(String workspace_path,JSONObject parameters,ArrayList<String> infoList) {
//		this.workspace_path = workspace_path;
		this.parameters = parameters; 
		this.infoList = infoList;	
		
		if(!parameters.get("source_connection_way").equals("localhost/connected remote")) {
			this.source_mount_disk = (String) parameters.get("source_mount_disk");
			this.source_mount_disk_file = new File(source_mount_disk);
		}
		
		if(!parameters.get("target_connection_way").equals("localhost/connected remote")) {
			this.target_mount_disk = (String) parameters.get("target_mount_disk");
			this.target_mount_disk_file = new File(target_mount_disk);
		}
		
		try {
			this.workspace_path = new File(App.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile().getAbsolutePath();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			return;
		}
			
	}
	
	/*
	 * net use [{<DeviceName> | *}] [\\<ComputerName>\<ShareName>[\<volume>]] 
	 * [{<Password> | *}]] [/user:[<DomainName>\]<UserName] >
	 * [/user:[<DottedDomainName>\]<UserName>]
	 * [/user: [<UserName@DottedDomainName>]
	 *  [/savecred] [/smartcard]
	 * [{/delete | /persistent:{yes | no}}]
		net use [<DeviceName> [/home[{<Password> | *}] [/delete:{yes | no}]]
		net use [/persistent:{yes | no}]
	*/
	
	public String cifs_mount(String ip,String user,String password,String folder,
			String mount_disk) throws Exception{		
		 
		if(is_mount(mount_disk)) {
			if(parameters.get("force_umount").equals("true")) {
				String command = String.format("net use /delete %s /y",  mount_disk);
				Utils.execute_windows_command(command, infoList,true);
			} else {
				System.out.println("The disk "+mount_disk+" is already mount, so we stop the task");
				return "The disk "+mount_disk+" is already mount, so we stop the task";
			}			
		} 
		
		String command = String.format("net use %s \\\\%s\\%s  /user:%s %s",
				mount_disk, ip,folder,user,password);
		System.out.println("mount command:"+command);
		System.out.println("try to mount to remote server.");
		//System.out.println("command:"+ command);
		Utils.execute_windows_command(command, infoList,true);
				
		if(!is_mount(mount_disk)) {
			System.out.println("mount failed");
			return "The disk "+mount_disk+ " mount to remote folder failed.";
		} 
		
		return "";
	}
	
	/* TODO
	 * mount [-o <option>[...]] [-u:<username>] [-p:{<password> | *}]
	 *  {\\<computername>\<sharename> | <computername>:/<sharename>}
	 *   {<devicename> | *}
	 * */
	
	//https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/mount
	public String nfs_mount(String ip,String folder,String mount_disk) throws Exception{
		if(is_mount(mount_disk)) {
			if(parameters.get("force_umount").equals("true")) {
				String command = String.format("net use /delete %s /y",  mount_disk);
				Utils.execute_windows_command(command, infoList,true);
			} else {
				System.out.println("The disk "+mount_disk+" is already mount, so we stop the task");
				return "The disk "+mount_disk+" is already mount, so we stop the task";
			}			
		}
		String command = String.format("mount %s:/%s %s",
				 ip,folder, mount_disk);
		System.out.println("mount command:"+command);
		System.out.println("try to mount to remote server.");
		//System.out.println("command:"+ command);
		Utils.execute_windows_command(command, infoList,true);
				
		if(!is_mount(mount_disk)) {
			System.out.println("mount failed");
			return "The disk "+mount_disk+ " mount to remote folder failed.";
		} 
		
		return "";
	}
	
	public String umount() throws Exception{	
					
		if(source_mount_disk!=null && !parameters.get("source_connection_way").equals("localhost/connected remote")) {
			String command = String.format("net use /delete %s /y",  source_mount_disk);
			//System.out.println("command:"+ command);
			Utils.execute_windows_command(command, infoList,true);
		
			if(!is_mount(source_mount_disk)) {
				String info = "umount success";
				System.out.println(info);
				infoList.add(info);
			} else {
				String info = "umount failed";
				infoList.add(info);
				System.out.println(info);
				return info;
			}		
		}
		
		if(target_mount_disk!=null && !parameters.get("target_connection_way").equals("localhost/connected remote")) {
			String command = String.format("net use /delete %s /y",  target_mount_disk);
			//System.out.println("command:"+ command);
			Utils.execute_windows_command(command, infoList,true);
		
			if(!is_mount(target_mount_disk)) {
				String info = "umount success";
				System.out.println(info);
				infoList.add(info);
			} else {
				String info = "umount failed";
				infoList.add(info);
				System.out.println(info);
				return info;
			}		
		}
	
		return "";
	}
	
	//=================================================================
	
	public boolean is_mount(String disk) throws Exception{		
		
		ArrayList<String> cmd_result = (ArrayList<String>) 
				Utils.execute_windows_command("wmic logicaldisk get caption", infoList,true);		
		for(String s:cmd_result) {
			//System.out.println(s);
			if (s.substring(0,Math.min(2, s.length())).equals(disk.toUpperCase())) {
				return true;
			}
		}	
		
		return false;
	}
		
	//flag of rsync:-a= -Dgloptr, and -g is unknown for windows	
	public void copy_files() throws Exception{		
		
		// get source list
		if(parameters.get("source_connection_way").equals("localhost/connected remote")) {		
			String source_path = (String) parameters.get("source_folder");				
			String[] source_strings = source_path.split(";");
			
			for(String s: source_strings) {
				if(s.endsWith("*.*")){
					File parent_p = new File(s).getParentFile();
					for(File child_file: parent_p.getAbsoluteFile().listFiles()) {
						if(child_file.isFile()) {
							all_files_to_be_copied.add(child_file);
						}						
					}
				 } else if(s.endsWith("*")) {
					File parent_p = new File(s).getParentFile();
					for(File child_file: parent_p.getAbsoluteFile().listFiles()) {
						all_files_to_be_copied.add(child_file);
					}
				 } else {
					if(new File(s).exists()) {
						all_files_to_be_copied.add(new File(s));
					}
				 }
			}		
		} else {	
			String sub_source_string = (String) parameters.get("source_subfolder");		
			if(sub_source_string.equals("*")) {
				for(File child_file: 
					new File(source_mount_disk).getAbsoluteFile().listFiles()) {
					all_files_to_be_copied.add(child_file);
				}
			} else {
				//TODO
				String[] sub_source_strings = sub_source_string.split(";");
				for(String s:sub_source_strings) {
					if(s.endsWith("*.*")) {
						File parent_p = new File(source_mount_disk,s).getParentFile();
						for(File child_file: parent_p.getAbsoluteFile().listFiles()) {
							if(child_file.isFile()) {
								all_files_to_be_copied.add(child_file);
							}						
						}
					} else if(s.endsWith("*")){					
						File parent_p = new File(source_mount_disk,s).getParentFile();
						for(File child_file: parent_p.getAbsoluteFile().listFiles()) {
							all_files_to_be_copied.add(child_file);
						}
					} else {
						if(new File(source_mount_disk,s).exists()) {
							all_files_to_be_copied.add(new File(source_mount_disk,s));
						} else {
							System.out.println("folder"+
									new File(source_mount_disk,s).getAbsolutePath()+" does not exist");
						}
					}
				}
			}					
		}
		
		String source_files_name ="";
		for(File f: all_files_to_be_copied) {
			 source_files_name += (String.format(" '%s' ", Utils.windowspath_to_cwrsyncpath(f.getAbsolutePath())));
			 System.out.println(f.getAbsolutePath() +" exists");
		}
		
		//get target list
		String target_path = "";
		if(parameters.get("target_connection_way").equals("localhost/connected remote")) {
			target_path = (String) parameters.get("target_folder");
		} else {	
			target_path = new File(target_mount_disk).getAbsolutePath();
		}
				
		System.out.println("==================================");
		System.out.println(String.format("Start copy folders %s to dest '%s'", 
				source_files_name, Utils.windowspath_to_cwrsyncpath(target_path)));
		if(new File(target_path).exists() && 
		   parameters.get("target_connection_way").equals("localhost/connected remote")) {
			give_777_in_windows(target_path);
		}	
		//--chmod 777
		String command = String.format("%s\\cwRsync\\bin\\rsync -Dlort --chmod 777 --progress --stats %s '%s'", 
			  workspace_path ,source_files_name, Utils.windowspath_to_cwrsyncpath(target_path));		
		System.out.println("============================================");
		System.out.println("rsync command:"+command);
		System.out.println("============================================");
		Utils.execute_windows_rsync_command(command, infoList, this, false);
	
	}
	
	public void give_777_in_windows(String path) {
		String whoami;
		try {
			whoami = Utils.execute_windows_command("whoami", infoList, true).get(0);
			String command = String.format("icacls %s /grant %s:F", path, whoami);
			Utils.execute_windows_command(command, infoList, false);
			System.out.println("Give all privilage of file "+path+ " successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void remove_file() throws Exception{				
		String command;
		for(File f:all_files_to_be_copied) {
			if(f.isDirectory()) {
				command = String.format("rd /s /q %s", f.getAbsolutePath());
				Utils.execute_windows_command(command,infoList,false);
			} else if(f.isFile()) {
				command = String.format("del %s", f.getAbsolutePath());
				Utils.execute_windows_command(command,infoList,false);
			}		
		}			
				
		String t = "remove success";		
		//sfui.show_info.append(t+"\n");
		System.out.println(t);
		infoList.add(t);			
	}
	
	public void pause_copy_files() {				
		String t = "pause copy file by user.";
		this.complete_copy = false;
		System.out.println(t);
		infoList.add(t);
				
		for(Long pid:all_pid) {
			if(!original_pid.contains(pid)) {
				String command = String.format("taskkill /PID %d /f /t", pid);	
				//System.out.println("command:"+command);
				try {
					Utils.execute_windows_command(command, infoList,true);
				} catch (Exception e) {
					e.printStackTrace();		
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					infoList.add(sw.toString());
				}
			}		
		}						
	}
		
	public void exit_with_error(String error_info) {
		System.out.println(error_info);
		try {
			this.umount();
			if(source_mount_disk!=null && source_mount_disk_file.exists()) {
				source_mount_disk_file.delete();
			}
			
			if(target_mount_disk!=null && target_mount_disk_file.exists()) {
				target_mount_disk_file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
			
		return;
	}
	
	public void copy_files_with_timeout(){
			
    	ExecutorService es = Executors.newSingleThreadExecutor();
    	@SuppressWarnings("rawtypes")
		Future fu = es.submit(new rsync_run(this));	
    	int timeout_minute = Math.round((float)parameters.get("timeout")*60);
    	try {
			//fu.get(5,TimeUnit.SECONDS);
    		System.out.println("start copy file, timeout:"+ parameters.get("timeout")+"h");
    		infoList.add("start copy file, timeout:"+ parameters.get("timeout")+"h");
    		fu.get(timeout_minute, TimeUnit.MINUTES);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			pause_copy_files();
			
		}
    	  	   	
		es.shutdown();  		
	}
	
	public void move_files_with_timeout() throws Exception{
		try {
			this.copy_files_with_timeout();
		} catch (Exception e) {
			String t = "Since some error happens when we copy the file, "
					 + "so we wont remove source folder";
			//Utils.append_string_to_sfui(sfui, t);
			infoList.add(t);
			e.printStackTrace();
			exit_with_error(t);
			return;
		}
		
		if(this.complete_copy) {
			this.remove_file();
		} 	
	}
	
	public void buffered_write_log() {
		if(infoList.size()>2000) {
			Utils.write_log(workspace_path, parameters, infoList);
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}	
	}
	
	//====================================================================
	
	public void start() {	
		
		String start_time = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(new Date());
		long start_time_long = Calendar.getInstance().getTimeInMillis();
		
		try {
				
			this.complete_copy = true;
					
			System.out.println("copy data to another computer");
         	infoList.add("copy data to another computer");
         			
			// step1 mount to remote 
			if(parameters.get("source_connection_way").equals("temporary cifs connection")) {	
				String info = cifs_mount((String)parameters.get("source_ip"), 
				(String)parameters.get("source_connection_username"), 
				(String)parameters.get("source_connection_password"), 
				(String)parameters.get("source_folder"), source_mount_disk);	
				
				if(!info.equals("")) {
					exit_with_error(info);
					return;
				} 			
			} else if(parameters.get("source_connection_way").equals("temporary nfs connection")) {
				
				String info = nfs_mount((String)parameters.get("source_ip"), 
				(String)parameters.get("source_folder"), source_mount_disk);
				
				if(!info.equals("")) {
					exit_with_error(info);
					return;
				} 	 
			}
			
			if(parameters.get("target_connection_way").equals("temporary cifs connection")) {		
				String info = cifs_mount((String)parameters.get("target_ip"), 
				(String)parameters.get("target_connection_username"), 
				(String)parameters.get("target_connection_password"), 
				(String)parameters.get("target_folder"), target_mount_disk);
				
				if(!info.equals("")) {
					exit_with_error(info);
					return;
				} 	
			} else if(parameters.get("target_connection_way").equals("temporary nfs connection")) {		
				String info = nfs_mount((String)parameters.get("target_ip"), 
				(String)parameters.get("target_folder"), target_mount_disk);
				
				if(!info.equals("")) {
					exit_with_error(info);
					return;
				} 	
			}
		} catch (Exception e) {
			String s = "Unable to connect to remote server";
			infoList.add(s);
			System.out.println(s);
			e.printStackTrace();		
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());	
			buffered_write_log();
			exit_with_error(s);
			return;
		}	
		
		//step2 copy/move
		try {
			if(parameters.get("move_or_copy").equals("copy")) {
				this.copy_files_with_timeout();
			} else {	
				this.move_files_with_timeout();  
			}						
		} catch (Exception e) {
			e.printStackTrace();		
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoList.add(sw.toString());
			String s = "Some error happens during copy file";
			System.out.println(s);
			infoList.add(s);
			buffered_write_log();
			return;
		}
			
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//step3 umount
		try {
			String current_time = new SimpleDateFormat("yyyyMMdd_HH:mm:ss")
					.format(Calendar.getInstance().getTime());
			if(this.complete_copy) {
				String end_time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
				long end_time_long = Calendar.getInstance().getTimeInMillis();
				String time_summary = String.format(
					"Copy success.Start time:%s,end time:%s, time cost:%s", 
					start_time,end_time,Utils.Millisecond_to_String(end_time_long-start_time_long)
				);
				System.out.println(time_summary);
				infoList.add(time_summary);
				String t = "===========================================";
				infoList.add(t);
				System.out.println(t);					
				umount();				
			} else {				
				System.out.println("the copy process is stopped,current_time:"+current_time);			
				String t = "==========";
				infoList.add(t);
				System.out.println(t);
				umount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
						
		Utils.write_log(workspace_path, parameters, infoList);		
	}
	
}

class rsync_run extends Thread{
	
	Single_upload_task task;
	
	public rsync_run(Single_upload_task task) {
		this.task = task;
	}
	
	public void run() {
		try {
			task.copy_files();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



