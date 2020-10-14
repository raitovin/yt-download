package fr.raitovin.ytdl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Main {
	
	//jframe element
	
	private static JTextField url_input = new JTextField();
	private static JButton download = new JButton("Download");
	private static JTextArea console_view = new JTextArea();
	private static JScrollPane scroll;
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		
		frame.setTitle("yt-downloader_V1.0");
		frame.setSize(450, 480);
		frame.setLocationRelativeTo(null);
		
		JPanel pan = new JPanel();
		pan.setPreferredSize(new Dimension(450, 480));
		
		String[] format_list = {"3840x2160-60fps", "3840x2160-30fps", "3840x2160-24fps", "1920x1080-60fps", "1920x1080-30fps", "1920x1080-24fps"};
		
		//combobox settings
		JComboBox format_selection = new JComboBox(format_list);
		format_selection.setSelectedItem(0);
		format_selection.setPreferredSize(new Dimension(120, 20));
		
		
		
		//console view
		console_view.setPreferredSize(new Dimension(400, 320));
		console_view.setBorder(BorderFactory.createLoweredBevelBorder());
		console_view.setCaretPosition(console_view.getDocument().getLength());
		console_view.setEditable(false);
		
		scroll = new JScrollPane(console_view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		scroll.update(null);
		
		console_view.setLineWrap(true);
	    console_view.setWrapStyleWord(true);
	    
		redirect();
		//input setting
		url_input.setPreferredSize(new Dimension(350, 30));
		
		
		//button settings
		download.setPreferredSize(new Dimension(100, 30));
		
		download.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				if (url_input.getText() != null) {
					String val = url_input.getText();
					int SF = 299;
					
					switch (format_selection.getSelectedItem().toString()) {
						case "3840x2160-60fps":
							SF = 401;
							YTDownload(val, SF);
							break;
						case "3840x2160-30fps":
							SF = 271;
							YTDownload(val, SF);
							break;
						case "3840x2160-24fps":
							SF = 313;
							YTDownload(val, SF);
							break;
						case "1920x1080-60fps":
							SF = 299;
							YTDownload(val, SF);
							break;
						case "1920x1080-30fps":
							SF = 137;
							YTDownload(val, SF);
							break;
						case "1920x1080-24fps":
							SF = 137;
							YTDownload(val, SF);
							break;
						default:
							
					}
				}
			}
			
		});
		
		pan.add(format_selection);
		pan.add(url_input);
		pan.add(download);
		pan.add(scroll);
		
		frame.setContentPane(pan);
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void YTDownload(String url_input, int format) {
		
		/***
		 * 
		 * in file
		 * 
		 * 
		 * 
		 * get the current location of youtube-dl.exe folder
		 */
		
		String download_path = System.getProperty("user.dir") + "\\Download";
		System.out.println(download_path);
		
		//get url
		String url = url_input;
		System.out.println(url);
		
		String[] command = {
				"cmd",
		};
		Process p;
		
		try {
			p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("cd \"" + download_path + "\"");
			stdin.println(download_path + "\\youtube-dl -f "+ format +" " + url);
			stdin.close();
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static PrintStream redirect() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				console_view.append(String.valueOf((char) b));
			}
		};
		PrintStream ps = new PrintStream(out);
		
		System.setOut(ps);
		System.setErr(ps);
		
		return ps;
	}
}
