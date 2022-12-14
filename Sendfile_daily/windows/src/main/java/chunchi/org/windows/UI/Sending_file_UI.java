package chunchi.org.windows.UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.json.simple.JSONObject;
import chunchi.org.windows.Single_upload_task;

public class Sending_file_UI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JTextArea show_info;
	public JButton continue_button;
	public JButton exit_button;
	public JScrollPane scrollPane;
	AdjustmentListener adl;
	
	GridBagLayout layout;
	JPanel panel;
	Sending_file_UI self;	
	
	Single_upload_task task; 
	Thread task_thread; 

	ArrayList<String> infoList;
	
	public Sending_file_UI(String workspace_path,
			JSONObject parameters, ArrayList<String> infoList){
		
		UIManager.put("Button.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("Label.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("TextArea.font", new Font("Courier", Font.BOLD, 18));
		UIManager.put("ComboBox.font", new Font("Courier", Font.BOLD, 18));		
		UIManager.put("RadioButton.font", new Font("Courier", Font.BOLD, 18));
		
		this.infoList = infoList;
		self = this;
		layout = new GridBagLayout();
		panel = new JPanel(layout);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(900, 700);
	    this.setLocation(250, 200);  
	    this.setTitle("sending_files");
	    
	    show_info = new JTextArea();
	    scrollPane = new JScrollPane(show_info);
		scrollPane.setPreferredSize(new Dimension(850,600));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add_a_component(scrollPane, 0, 0, 10, 10, panel, layout);	
		
		continue_button = new JButton("continue");
		continue_button.setPreferredSize(new Dimension(150,50));
		continue_button.setEnabled(false);	
		continue_button.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {								
				continue_button.setEnabled(false);
				exit_button.setEnabled(true);
				scrollPane.getVerticalScrollBar().addAdjustmentListener(adl);
				task_thread.resume();
			}
		});
		add_a_component(continue_button, 6, 10, 2, 1, panel, layout);
		
		exit_button = new JButton("stop");
		exit_button.setPreferredSize(new Dimension(150,50));
		exit_button.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				continue_button.setEnabled(true);
				exit_button.setEnabled(false);
				scrollPane.getVerticalScrollBar().removeAdjustmentListener(adl);
				task_thread.suspend();
			}
		});
		add_a_component(exit_button, 8, 10, 2, 1, panel, layout);
	    
	    panel.setBounds(this.getBounds());
	    panel.setVisible(true);		
	    adl=new AdjustmentListener() {					
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());						
			}
		};
		this.add(panel);
		this.setBounds(panel.getBounds());
		this.setVisible(true);
		
		task = new Single_upload_task(workspace_path, parameters, infoList);
		task_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				continue_button.setEnabled(false);
				exit_button.setEnabled(true);
				scrollPane.getVerticalScrollBar().addAdjustmentListener(adl);
				redirect_to_showinfo();
				//==========================================================
				task.start();	
				//==========================================================
				continue_button.setEnabled(false);
				exit_button.setEnabled(false);
				for(AdjustmentListener al:
					scrollPane.getVerticalScrollBar().getAdjustmentListeners()) {
					scrollPane.getVerticalScrollBar().removeAdjustmentListener(al);
				}
				redirect_to_console();
			}				
		});
		
		task_thread.start();
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
	
	public void set_and_show_error(String info) {
		 JOptionPane.showConfirmDialog(null, info, 
				 "error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}	
	
	public void set_and_show_info(String info) {
		 JOptionPane.showConfirmDialog(null, info, 
				 "information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}	

	//==============================================================================
	
	// ???????????????console???????????????jtextarea
	public void updateTextArea(String s) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				self.show_info.append(s);
			}
		});
	}
	
	public void redirect_to_showinfo() {
		OutputStream out = new OutputStream() {			
			@Override
			public void write(int b) throws IOException {
				// TODO print to console as well
				updateTextArea(String.valueOf((char)b));
			}
			
			@Override
			public void write(byte[] b,int off,int on) throws IOException {
				updateTextArea(new String(b,off,on));
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				write(b,0,b.length);
			}		
		};
		
		System.setOut(new PrintStream(out,true));
		System.setErr(new PrintStream(out,true));
	}
	
	public void redirect_to_console() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
	}
	
	/*
	public void test() {
		System.out.println("Hello");	
		for(int i=0;i<50;i++) {
			System.out.println(String.valueOf(i));
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
	}*/
}






