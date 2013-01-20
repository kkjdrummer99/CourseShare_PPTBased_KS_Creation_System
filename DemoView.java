import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.*;
import java.util.StringTokenizer;
import java.util.HashMap;
//import java.awt.print.*;
//import java.awt.print.PageFormat;

public class DemoView extends FrameView {
  
	String text;
	private JFrame frame;
	private JFrame tableFrame = new JFrame("Analysis Results as a Table");
	private JFrame printFrame = new JFrame("Analysis Results with Knowledge Structure Map");
	private JPanel emptyPanel;
	private JPanel northPanel;
	private JPanel northUpPanel;
	private JPanel northMiddlePanel;
	private JPanel northDownPanel;
	private JPanel northSavePanel;
    private JPanel leftPanel;
    private JPanel leftupPanel;
    private JPanel leftbottomUpPanel;
    private JPanel leftbottomMiddlePanel;
    private JPanel leftbottomDownPanel;
    private JPanel buttonPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;
    
    private JPanel KSMap = new JPanel();
    private JPanel Head = new JPanel();
    static JPanel Body = new JPanel();
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openFileMenuItem;
    private JMenuItem printMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenu helpMenu;
    private JFileChooser fileChooser;
    private JFileChooser saveXMLFileChooser;
    private JFileChooser saveTXTFileChooser;
    private List keywordList;
    private List selectedkeywordList;
    private JButton toleft;
    private JButton toright;
    private JButton addallitems;
    private JButton removeallitems;
    private JButton top10;
    private JButton top15;
    private JButton top20;
    private JButton top30;
    private JButton top40;
    private JButton top50;
    private JButton drawgraph;
    private JButton selectFile;
    private JButton showAnalysisResultsTable;
    private JButton print;
    private JTextArea txtresult;
    private JScrollPane scroller;
    static JScrollPane scrollBar;
    private JTable showTable;
    private Graphics2D g2;
    private ArrayList<String> data = new ArrayList();
    private ArrayList<String> selectedList;
    private String filename;
    private String fname;
    private String fnameWithExtension;
    private String extension;
    private String path;
    private String tableFileName = "";
	private String tableFileExtension = "";
	final private String defaultXMLFile = "Default_XML_Data_File.xml";
    private Calendar cal;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;
    private int ampm;
    private static int callCount = 0;
    private final String BASE_LINK = "C:\\workspace\\PPTBased_KS_CreationProcess\\output\\";
    private String OPENFile_BASE_PATH = "";
    private String XMLFile_BASE_LINK = "";
    private String TXTFile_BASE_LINK = "";
    private static int ROWS = 100;
    private static Object[][] finalData = new Object[ROWS][13];
    private String[] columnName = {"Case Number", "KS Map Generated Date & Time", "No. of User Selected Terms", "Processing Time to Generate KS Map", "No. of Nodes", "No. of Links", "Coherence", "Weight Information", "File Name w/ Extension", "File Uploaded Date & Time", "Total No. of Words", "No. of Auto Extracted Terms", "Image of KS Map"};
  	static int caseNumber = 0;  
  	private boolean isSaveXMLFile = false;
  	  	  	
  	public DemoView(SingleFrameApplication app) { 
        super(app); 
        initComponents(); 
    } 
	
    private void initComponents() { 
        // all the GUI stuff is somehow defined here 
    	// set mainframe and panel
    	emptyPanel = new JPanel();
    	frame = new JFrame("CourseShare - Knowledge Structure");
    	   	
    	northPanel = new JPanel();
    	northUpPanel = new JPanel();
    	northMiddlePanel = new JPanel();
    	northDownPanel = new JPanel();
    	String panelTotalTitle1 = "Key Concepts and their Frequencies";
    	String panelTotalTitle2 = "Knowledge Structure Map                                ";
    	String panelSubTitle1 = "Automatically ";
    	String panelSubTitle1_1 = "Extracted Keywords ";
    	String panelSubTitle2 = "User                                                                                Generated based on                                                    ";
    	String panelSubTitle2_1 = "Selected Keywords                                                                      Selected Keywords                                                         ";
    	String panelSubTitle3 = "Analysis Results";
    	    	
    	JLabel label1 = new JLabel(panelTotalTitle1, JLabel.LEFT);
    	JLabel label2 = new JLabel(panelTotalTitle2, JLabel.LEFT);
    	JLabel emptyLabel1 = new JLabel("                                                         ", JLabel.LEFT);
    	JLabel label3 = new JLabel(panelSubTitle1, JLabel.LEFT);
    	JLabel label4 = new JLabel(panelSubTitle2, JLabel.LEFT);
    	JLabel emptyLabel2 = new JLabel("                                    ", JLabel.LEFT);
    	JLabel label5 = new JLabel(panelSubTitle1_1, JLabel.LEFT);
    	JLabel label6 = new JLabel(panelSubTitle2_1, JLabel.LEFT);
    	JLabel emptyLabel3 = new JLabel("                       ", JLabel.LEFT);
    	JLabel label7 = new JLabel(panelSubTitle3, JLabel.LEFT);
    	
    	Font f1 = new Font("Serif", Font.BOLD+Font.ITALIC, 20);
    	Font f2 = new Font("Serif", Font.BOLD+Font.ITALIC, 14);
    	label1.setFont(f1);
    	label1.setForeground(Color.black);
    	label2.setFont(f1);
    	label2.setForeground(Color.black);
    	label3.setFont(f2);
    	label3.setForeground(Color.blue);
    	label4.setFont(f2);
    	label4.setForeground(Color.blue);
    	label5.setFont(f2);
    	label5.setForeground(Color.blue);
    	label6.setFont(f2);
    	label6.setForeground(Color.blue);
    	label7.setFont(f2);
    	label7.setForeground(Color.blue);
    	northUpPanel.setLayout(new BoxLayout(northUpPanel, BoxLayout.X_AXIS));
    	northUpPanel.add(label1);
    	northUpPanel.add(emptyLabel1);
    	northUpPanel.add(label2);
    	northMiddlePanel.setLayout(new BoxLayout(northMiddlePanel, BoxLayout.X_AXIS));
    	northMiddlePanel.add(label3);
    	northMiddlePanel.add(emptyLabel2);
    	northMiddlePanel.add(label4);
    	northDownPanel.setLayout(new BoxLayout(northDownPanel, BoxLayout.X_AXIS));
    	northDownPanel.add(label5);
    	northDownPanel.add(emptyLabel3);
    	northDownPanel.add(label6);
    	northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    	northPanel.add(northUpPanel);
    	northPanel.add(northMiddlePanel);
    	northPanel.add(northDownPanel);
    	frame.getContentPane().add(BorderLayout.NORTH, northPanel);
     	
    	// Preview
    	printFrame.setBackground(Color.white);
    	printFrame.getContentPane().add(BorderLayout.CENTER, Body);
    	scrollBar = new JScrollPane(Body, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        printFrame.add(scrollBar);
    	    	
    	leftPanel = new JPanel();
    	leftupPanel = new JPanel();
    	leftbottomUpPanel = new JPanel();
    	leftbottomMiddlePanel = new JPanel();
    	leftbottomDownPanel = new JPanel();
    	buttonPanel = new JPanel();
    	rightPanel = new JPanel();
    	bottomPanel = new JPanel();
    	
    	// Set Menu variables
    	menuBar = new JMenuBar();
    	fileMenu = new JMenu("File");
    	openFileMenuItem = new JMenuItem("Open");
    	printMenuItem = new JMenuItem("Preview & Print");
    	exitMenuItem = new JMenuItem("Exit");
    	helpMenu = new JMenu("Help");
    	aboutMenuItem = new JMenuItem("* CourseShare - knowledge structure ver1.0 written by Kyung Jin Kim");
    	
    	fileChooser = new JFileChooser();
    	FileNameExtensionFilter openFileFilter = new FileNameExtensionFilter("PPT File(*.ppt)", "ppt");
		fileChooser.addChoosableFileFilter(openFileFilter);
		fileChooser.setFileFilter(openFileFilter);
    	
		saveXMLFileChooser = new JFileChooser();
    	FileNameExtensionFilter saveXMLFileFilter = new FileNameExtensionFilter("XML File(*.xml)", "xml");
		saveXMLFileChooser.addChoosableFileFilter(saveXMLFileFilter);
		saveXMLFileChooser.setFileFilter(saveXMLFileFilter);
		
    	saveTXTFileChooser = new JFileChooser();
    	FileNameExtensionFilter saveTXTFileFilter = new FileNameExtensionFilter("TEXT File(*.txt)", "txt");
		saveTXTFileChooser.addChoosableFileFilter(saveTXTFileFilter);
		saveTXTFileChooser.setFileFilter(saveTXTFileFilter);
				
    	// Left Panel
    	keywordList = new List(25); //30
    	selectedkeywordList =  new List(25); //30
    	toright = new JButton("   →   ");
    	toleft = new JButton("   ←   ");
    	addallitems = new JButton(" All → ");
    	removeallitems = new JButton(" ← All ");
    	top10 = new JButton("TOP10");
    	top15 = new JButton("TOP15");
    	top20 = new JButton("TOP20");
    	top30 = new JButton("TOP30");
    	top40 = new JButton("TOP40");
    	top50 = new JButton("TOP50");
    	selectFile = new JButton(" Select a PPT File");
    	drawgraph = new JButton(" Generate Knowledge Structure ");
    	showAnalysisResultsTable = new JButton("Show Analysis Results as a Table");
    	print = new JButton("Preview & Print");
    	// bottomPanel
    	txtresult = new JTextArea(6,45); // 120
    	scroller = new JScrollPane(txtresult);    	
    	    	
    	// toright, toleft
    	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    	buttonPanel.add(toright);
    	buttonPanel.add(toleft);
    	buttonPanel.add(addallitems);
    	buttonPanel.add(removeallitems);
    	buttonPanel.add(top10);
    	buttonPanel.add(top15);
    	buttonPanel.add(top20);
    	buttonPanel.add(top30);
    	buttonPanel.add(top40);
    	buttonPanel.add(top50);
    	leftbottomUpPanel.add(selectFile);
    	leftbottomUpPanel.add(drawgraph);
    	leftbottomMiddlePanel.add(showAnalysisResultsTable);
    	leftbottomMiddlePanel.add(print);
    	leftbottomDownPanel.add(label7);
    	    	
    	// add button event
    	toright.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			torighbtnActionPerformed(evt);
    		}
    	});
    	toleft.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			toleftbtnActionPerformed(evt);
    		}
    	});
    	addallitems.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			addallitemsActionPerformed(evt);
    		}
    	});
    	removeallitems.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			removeallitemsActionPerformed(evt);
    		}
    	});
    	top10.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top10ActionPerformed(evt);
    		}
    	});
    	top15.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top15ActionPerformed(evt);
    		}
    	});
    	top20.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top20ActionPerformed(evt);
    		}
    	});
    	top30.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top30ActionPerformed(evt);
    		}
    	});
    	top40.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top40ActionPerformed(evt);
    		}
    	});
    	top50.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			top50ActionPerformed(evt);
    		}
    	});
    	selectFile.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			openFileMenuItemActionPerformed(evt);
    		}
    	});
    	drawgraph.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			drawgraphActionPerformed(evt);
    		}
    	});
    	showAnalysisResultsTable.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			showAnalysisResultsTableActionPerformed(evt);
    		}
    	});
    	print.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			printActionPerformed(evt);
    		}
    	});
    	
    	// bottomPanel : textarea
    	txtresult.setLineWrap(true);
    	scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    	//scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    	bottomPanel.add(scroller);
    	
    	// Menu Setting
    	fileMenu.add(openFileMenuItem);
    	fileMenu.add(printMenuItem);
    	fileMenu.add(exitMenuItem);
    	helpMenu.add(aboutMenuItem);
    	menuBar.add(fileMenu);
    	menuBar.add(helpMenu);
    	frame.setJMenuBar(menuBar);
    	
    	openFileMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			openFileMenuItemActionPerformed(evt);
    		}
    	});
    	printMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			printMenuItemActionPerformed(evt);
    		}
    	});
    	javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(DemoApp.class).getContext().getActionMap(DemoView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
    	
    	// left panel
    	leftupPanel.add(keywordList);
    	leftupPanel.add(buttonPanel);
    	leftupPanel.add(selectedkeywordList);
    	leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    	leftPanel.add(leftupPanel);
    	leftPanel.add(leftbottomUpPanel); 
    	leftPanel.add(leftbottomMiddlePanel); 
    	leftPanel.add(leftbottomDownPanel); 
    	//leftPanel.add(label5);
    	leftPanel.add(bottomPanel);      	
    	   	
    	frame.getContentPane().add(BorderLayout.WEST, leftPanel);
    	frame.getContentPane().add(BorderLayout.EAST, rightPanel);
    	frame.setTitle("KAIST KSLab - Knolwdge Structure Generator");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setResizable(false);
    	frame.setSize(985, 760); // 950, 800 | 950, 750 | 940, 760
      	frame.setVisible(true);
    
    }   
    
    protected void torighbtnActionPerformed(ActionEvent evt) {
		String[] items = keywordList.getSelectedItems();
		int[] selectindice = keywordList.getSelectedIndexes();
		
		for(int i=0; i<selectindice.length; i++) {
			selectedkeywordList.add(items[i]);
			keywordList.deselect(selectindice[i]);
			keywordList.remove(items[i]);
		}
	}
    
    protected void toleftbtnActionPerformed(ActionEvent evt) {
    	String[] items = selectedkeywordList.getSelectedItems();
    	int[] selectindice = selectedkeywordList.getSelectedIndexes();
    	
    	for(int i=0; i<selectindice.length; i++) {
    		keywordList.add(items[i]);
    		selectedkeywordList.remove(items[i]);
    	}
    }
    
    protected void addallitemsActionPerformed(ActionEvent evt) {
    	String[] items = keywordList.getItems();
    	int itemsize = keywordList.getItemCount();
    	
    	for(int i=0; i<itemsize; i++) {
    		selectedkeywordList.add(items[i]);
    		keywordList.remove(items[i]);
    	}
    }
    
    protected void removeallitemsActionPerformed(ActionEvent evt) {
    	String[] items = selectedkeywordList.getItems();
    	int itemsize = selectedkeywordList.getItemCount();
    	
    	for(int i=0; i<itemsize; i++) {
    		keywordList.add(items[i]);
    		selectedkeywordList.remove(items[i]);
    	}
    }    
    
    protected void top10ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
    	ArrayList<String> tmplist = new ArrayList<String>();
    	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	    	         	
    	String[] items = keywordList.getItems();
    	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<10) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<10; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }          	        
                	}  
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }   
    
    protected void top15ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
        	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	    	         	
      	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<15) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<15; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }           	        
                	}  
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }   
    
    protected void top20ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
    	   	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	    	         	
    	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<20) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<20; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }               		
                	}  
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }    
    
    protected void top30ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
    	   	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	    	         	
    	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<30) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<30; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }                		
                	}  
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }    
    
    protected void top40ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
   	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	    	         	
    	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<40) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<40; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }
            		}  
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }    
    
    protected void top50ActionPerformed(ActionEvent evt) {
    	String[] items1 = selectedkeywordList.getItems();
    	int itemsize1 = selectedkeywordList.getItemCount();
    	   	
    	for(int i=0; i<itemsize1; i++) {
    		keywordList.add(items1[i]);
    		selectedkeywordList.remove(items1[i]);
    	}
    	   	
    	int itemsize2 = keywordList.getItemCount();
    	if(itemsize2==0) {
    		txtresult.append("[USAGE] Open a PPT File!\n\n");
    	}
    	else{
    		String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";
        	File file = new File(extractedPath);
        	String line;
        	
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(file));
        		
        		if(itemsize2<50) {
            		for(int i=0; i<itemsize2; i++) {
            			line = null;    	        
            	        	
            	        if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        }       	        	
            		}
            	}
            	else {
            		for(int i=0; i<50; i++) {
            			line = null;
            	        
            			if((line = br.readLine()) != null) {
            	        	selectedkeywordList.add(line);
            	        	keywordList.remove(line);
            	        } 
            		}
            	}    	  	
        	} catch(Exception e) {
        		System.out.println(e);
        	}   			
    	}   	
    }    
     
    protected void drawgraphActionPerformed(ActionEvent evt) {
    	rightPanel.remove(KSMap);
    	printFrame.remove(scrollBar);
    	caseNumber++;
    	String[] items = selectedkeywordList.getItems();
    	int itemsize = selectedkeywordList.getItemCount();
    	int noOfTerm = 0;
    	String[] tmp;
    	String[][] termCount;
    	File file;
    	String str;
    	String userSelectedTermsCnt = null;
    	String nodesCnt = null;
    	String linksCnt = null;
    	String coherenceInfo = null;
        int ksv_year;
        int ksv_month;
        int ksv_date;
        int ksv_hour;
        int ksv_min;
        int ksv_sec;
        int ksv_ampm;
    	ArrayList<String> tmplist = new ArrayList<String>();

    	for(int i=0; i<itemsize; i++) {
    		tmplist.add(items[i]);
    	}
    	CommonUtil.writeToFile(tmplist, "./output/TERM_COUNT.txt");
    	
    	tmplist.clear();
    	for(int i=0; i<itemsize; i++) {
    		tmp = items[i].split("=");
    		tmplist.add(tmp[0]);
    	}    	
    	CommonUtil.writeToFile(tmplist, "./output/TERM.txt");
    	tmplist.clear();
    	
       	file = new File("./output/TERM_COUNT.txt");
        String s;
        int arrIndex=0;
        
    	try{
        	BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        	
        	if((s = fis.readLine()) != null) {
        		arrIndex = Integer.parseInt(s);
        	}
        	
          	termCount = new String[arrIndex][2];
        	
        	int i=0;
        	while((s = fis.readLine()) != null){
        		String[] temp = s.split("=");
        		termCount[i][0] = temp[0].trim();
        		termCount[i][1] = temp[1].trim();  
        		i++;
        	}
        	
        	for(int j=0; j<arrIndex-1; j++) {
        		String temp = "";
        		for(int k=0; k<arrIndex-j-1; k++) {
        			if(Integer.parseInt(termCount[k][1]) < Integer.parseInt(termCount[k+1][1])) {
        				temp = termCount[k][0];
        				termCount[k][0] = termCount[k+1][0];
        				termCount[k+1][0] = temp;
        				
        				temp = termCount[k][1];
        				termCount[k][1] = termCount[k+1][1];
        				termCount[k+1][1] = temp;
        			}
        		}
        	}
        	
        	for(int t=0; t<arrIndex; t++) {
        		tmplist.add(termCount[t][0]);
        	}       	
        	
        	CommonUtil.writeToFile(tmplist, "./output/DESC_TERM.txt");
        	tmplist.clear(); 	
        	
        	for(int t=0; t<arrIndex; t++) {
        		tmplist.add(termCount[t][0]+"="+termCount[t][1]);
        	}  
        	
        	CommonUtil.writeToFile(tmplist, "./output/DESC_TERM_COUNT.txt");
        	tmplist.clear();         	 
        } catch (IOException e){
        	System.out.println(e.getMessage());    			
        }   	
    	
        int pos1 = filename.lastIndexOf("\\");
        fname = filename.substring(pos1+1);
        System.out.println("* filename with ppt: "+fname);
        fnameWithExtension = fname;
        int pos2 = fname.lastIndexOf(".");
		fname = fname.substring(0, pos2);
		System.out.println("* filename: "+fname);
    			
		data.add(Integer.toString(caseNumber));
		
        KSVisualization ksv = new KSVisualization();
                   	               
        cal = Calendar.getInstance();
        ksv_year = cal.get(Calendar.YEAR);
        ksv_month = cal.get(Calendar.MONTH)+1;
        ksv_date = cal.get(Calendar.DATE);
        
        ksv_hour = cal.get(Calendar.HOUR);
        ksv_min = cal.get(Calendar.MINUTE);
        ksv_sec = cal.get(Calendar.SECOND);
        ksv_ampm = cal.get(Calendar.AM_PM); 
		
        Font fontBody = new  Font("Serif", Font.BOLD, 12);
    	Body.setBackground(Color.white);
    	Body.setLayout(new BoxLayout(Body, BoxLayout.Y_AXIS));
       
    	JPanel info1 = new JPanel();
    	info1.setBackground(Color.white);
    	JPanel info2 = new JPanel();
    	info2.setBackground(Color.white);
      	JPanel info3 = new JPanel();
      	info3.setBackground(Color.white);
    	JPanel info4 = new JPanel();
    	info4.setBackground(Color.white);
    	JPanel info5 = new JPanel();
    	info5.setBackground(Color.white);
    	JPanel info6 = new JPanel();
    	info6.setBackground(Color.white);
    	JPanel info7 = new JPanel();
    	info7.setBackground(Color.white);
    	JPanel info8 = new JPanel();
    	info8.setBackground(Color.white);
    	JPanel info9 = new JPanel();
    	info9.setBackground(Color.white);
    	JPanel info10 = new JPanel();
    	info10.setBackground(Color.white);
    	JPanel info11 = new JPanel();    	
    	info11.setBackground(Color.white);    	
        
    	file = new File("./output/TERM_COUNT.txt");
    	str = null; 
    	try{
    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
    		   		
    		txtresult.append("1. File Name\n");
    		txtresult.append("-> "+fname+"\n");
    		txtresult.append("2. File Name with Extension\n");
    		txtresult.append("-> "+fnameWithExtension+"\n");
    		JLabel fileNameWithExtension = new JLabel("1. File Name with Extension :- ", JLabel.LEFT);
    		fileNameWithExtension.setFont(fontBody);
    		info1.add(fileNameWithExtension);
    		JLabel fileNameWithExtension1 = new JLabel(fnameWithExtension, JLabel.LEFT);
    		fileNameWithExtension1.setFont(fontBody);
    		info1.add(fileNameWithExtension1);
    		JLabel emptySpace1 = new JLabel("                                                                       ", JLabel.LEFT);
    		info1.add(emptySpace1);
    		Body.add(info1);
    		
    		txtresult.append("3. Date and Time to Upload the File\n");
    		JLabel fileUploadedDateTime = new JLabel("2. Date and Time to Upload the File :- ", JLabel.LEFT);
			fileUploadedDateTime.setFont(fontBody); 
			info2.add(fileUploadedDateTime);
    		if(ampm==0) {
    			txtresult.append("-> Date: "+year+"."+month+"."+date+"\n");
    			txtresult.append("-> Time: "+hour+":"+min+":"+sec+" AM"+"\n");
    			JLabel fileUploadedDateTime1 = new JLabel("Date: "+year+"."+month+"."+date+" / "+"Time: "+hour+":"+min+":"+sec+" AM", JLabel.LEFT);
    			fileUploadedDateTime1.setFont(fontBody);
    			info2.add(fileUploadedDateTime1);
    		}
    		else {
    			txtresult.append("-> Date: "+year+"."+month+"."+date+"\n");
    			txtresult.append("-> Time: "+hour+":"+min+":"+sec+" PM"+"\n");
    			JLabel fileUploadedDateTime2 = new JLabel("Date: "+year+"."+month+"."+date+" / "+"Time: "+hour+":"+min+":"+sec+" PM", JLabel.LEFT);
    			fileUploadedDateTime2.setFont(fontBody);
    			info2.add(fileUploadedDateTime2);
    		}  
    		JLabel emptySpace2 = new JLabel("                                 ", JLabel.LEFT);
    		info2.add(emptySpace2);
    		Body.add(info2);
    		
    		txtresult.append("4. Weight Information\n");
    		txtresult.append("-> [FullText:Title:Content=1:4:1]\n");
    		JLabel weightInfo = new JLabel("3. Weight Information :- ", JLabel.LEFT);
    		weightInfo.setFont(fontBody); 
			info3.add(weightInfo);
			JLabel weightInfo1 = new JLabel("[FullText:Title:Content=1:4:1]", JLabel.LEFT);
    		weightInfo1.setFont(fontBody); 
			info3.add(weightInfo1);
			JLabel emptySpace3 = new JLabel("                                                      ", JLabel.LEFT);
    		info3.add(emptySpace3);
			Body.add(info3);
    		
    		txtresult.append("5. Total No. of Words\n");
    		txtresult.append("-> "+PPTBased_KS_CreationProcess.total_word_cnt+" words\n"); 
    		JLabel totalWordCnt = new JLabel("4. Total No. of Words :- ", JLabel.LEFT);  
    		totalWordCnt.setFont(fontBody);
    		info4.add(totalWordCnt);
    		JLabel totalWordCnt1 = new JLabel(Integer.toString(PPTBased_KS_CreationProcess.total_word_cnt), JLabel.LEFT);  
    		totalWordCnt1.setFont(fontBody);
    		info4.add(totalWordCnt1);
    		JLabel emptySpace4 = new JLabel("                                                                                             ", JLabel.LEFT);
    		info4.add(emptySpace4);
    		Body.add(info4);    		
    		
    		txtresult.append("6. Total No. of Automatically Extracted Terms\n");
    		txtresult.append("-> "+PPTBased_KS_CreationProcess.auto_extracted_terms_cnt+" keywords\n");
    		JLabel autoExtractedTermsCnt = new JLabel("5. Total No. of Automatically Extracted Terms :- ", JLabel.LEFT);  
    		autoExtractedTermsCnt.setFont(fontBody);
    		info5.add(autoExtractedTermsCnt);
    		JLabel autoExtractedTermsCnt1 = new JLabel(Integer.toString(PPTBased_KS_CreationProcess.auto_extracted_terms_cnt), JLabel.LEFT);  
    		autoExtractedTermsCnt1.setFont(fontBody);
    		info5.add(autoExtractedTermsCnt1);
    		JLabel emptySpace5 = new JLabel("                                                             ", JLabel.LEFT);
    		info5.add(emptySpace5);
    		Body.add(info5);
    		
    		str = fis.readLine();
    		userSelectedTermsCnt = str;
    		txtresult.append("7. No. of User Selected Terms: "+userSelectedTermsCnt+"\n");
    		JLabel userSelectedTerms = new JLabel("6. No. of User Selected Terms :- ", JLabel.LEFT);
    		userSelectedTerms.setFont(fontBody);
    		info6.add(userSelectedTerms);
    		JLabel userSelectedTerms1 = new JLabel(userSelectedTermsCnt, JLabel.LEFT);
    		userSelectedTerms1.setFont(fontBody);
    		info6.add(userSelectedTerms1);
    		JLabel emptySpace6 = new JLabel("                                                                                     ", JLabel.LEFT);
    		info6.add(emptySpace6);
    		Body.add(info6);
    		
    		noOfTerm = Integer.parseInt(str);
    		    		
    		int i=0;    		
    		if((str = fis.readLine()) != null) {
    			tmp = str.split("=");     			
    			txtresult.append("-> "+(++i)+"st term with its count\n  : "+tmp[0].trim()+" ("+tmp[1].trim()+")\n");
    		}
    		
    		if((str = fis.readLine()) != null) {
    			tmp = str.split("=");
    			txtresult.append("-> "+(++i)+"nd term with its count\n  : "+tmp[0].trim()+" ("+tmp[1].trim()+")\n");
    		}
    		
    		if((str = fis.readLine()) != null) {
    			tmp = str.split("=");  
    			txtresult.append("-> "+(++i)+"rd term with its count\n  : "+tmp[0].trim()+" ("+tmp[1].trim()+")\n");
    		}
    		
    		while((str = fis.readLine()) != null){
    			tmp = str.split("=");
    			txtresult.append("-> "+(++i)+"th term with its count\n  : "+tmp[0].trim()+" ("+tmp[1].trim()+")\n");  	
    		}   		
    		
    	}catch (IOException e){
			System.out.println(e.getMessage());
			
		}    	
    	
    	PPTBased_KS_CreationProcess pe2 = new PPTBased_KS_CreationProcess(noOfTerm);
        try {
     	   pe2.calculationKSAndWriteToPRX(fname, path, noOfTerm);
        } catch(Exception e) {
     	   System.out.println(e);
        }    	
    	  
        txtresult.append("8. Date and Time to Perform the Knowledge Structure Map\n");
        JLabel KSMapGeneratedDateTime = new JLabel("7. Date and Time to Perform the Knowledge Structure Map :- ", JLabel.LEFT);
		KSMapGeneratedDateTime.setFont(fontBody);
		info7.add(KSMapGeneratedDateTime);
        if(ksv_ampm==0) {
			txtresult.append("-> Date: "+ksv_year+"."+ksv_month+"."+ksv_date+"\n");
			txtresult.append("-> Time: "+ksv_hour+":"+ksv_min+":"+ksv_sec+" AM"+"\n");
			JLabel KSMapGeneratedDateTime1 = new JLabel("Date: "+ksv_year+"."+ksv_month+"."+ksv_date+" / "+"Time: "+ksv_hour+":"+ksv_min+":"+ksv_sec+" AM", JLabel.LEFT);
			KSMapGeneratedDateTime1.setFont(fontBody);
			info7.add(KSMapGeneratedDateTime1);
		}
		else {
			txtresult.append("-> Date: "+ksv_year+"."+ksv_month+"."+ksv_date+"\n");
			txtresult.append("-> Time: "+ksv_hour+":"+ksv_min+":"+ksv_sec+" PM"+"\n");
			JLabel KSMapGeneratedDateTime2 = new JLabel("Date: "+ksv_year+"."+ksv_month+"."+ksv_date+" / "+"Time: "+ksv_hour+":"+ksv_min+":"+ksv_sec+" PM", JLabel.LEFT);
			KSMapGeneratedDateTime2.setFont(fontBody);
			info7.add(KSMapGeneratedDateTime2);
		}
        JLabel emptySpace7 = new JLabel(" ", JLabel.LEFT);
		info7.add(emptySpace7);
        Body.add(info7);
        
        Date date1 = new Date();
     	long beforeTime = date1.getTime();
        
     	KSMap = ksv.KSGenerate();
        rightPanel.add(KSMap);
        
        Date date2 = new Date();
    	long afterTime = date2.getTime();
        
    	long taskTime = afterTime - beforeTime;
        
    	txtresult.append("9. Task Time to Generate KS Map\n");
    	txtresult.append("-> "+((double)taskTime/1000)+" seconds\n");
    	JLabel KSMapGeneratedTaskTime = new JLabel("8. Task Time to Generate Knowledge Structure Map :- ", JLabel.LEFT);
     	KSMapGeneratedTaskTime.setFont(fontBody);
		info8.add(KSMapGeneratedTaskTime);
		JLabel KSMapGeneratedTaskTime1 = new JLabel(((double)taskTime/1000)+" seconds", JLabel.LEFT);
     	KSMapGeneratedTaskTime1.setFont(fontBody);
		info8.add(KSMapGeneratedTaskTime1);
		JLabel emptySpace8 = new JLabel("                                       ", JLabel.LEFT);
		info8.add(emptySpace8);
		Body.add(info8);
    	
	  	file = new File("./KNOTData/data_pf.pf");
    	str = null; 
    	try{
    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
    		
    		fis.readLine();
    			
    		if((str = fis.readLine()) != null){ 
    			txtresult.append("10. No. of Nodes\n");
     			nodesCnt = str;
     			txtresult.append("-> "+nodesCnt+"\n");
     			JLabel NoOfNodes = new JLabel("9. No. of Nodes :- ", JLabel.LEFT);
     			NoOfNodes.setFont(fontBody);
     			info9.add(NoOfNodes);
     			JLabel NoOfNodes1 = new JLabel(nodesCnt, JLabel.LEFT);
     			NoOfNodes1.setFont(fontBody);
     			info9.add(NoOfNodes1);
     			JLabel emptySpace9 = new JLabel("                                                                                              ", JLabel.LEFT);
     			info9.add(emptySpace9);
    		}
    		Body.add(info9);
    		
    		if((str = fis.readLine()) != null){ 
    			txtresult.append("11. No. of Links\n");
     			linksCnt = str;
     			txtresult.append("-> "+linksCnt+"\n");
     			JLabel NoOfLinks = new JLabel("10. No. of Links :- ", JLabel.LEFT);
     			NoOfLinks.setFont(fontBody);
     			info10.add(NoOfLinks);
     			JLabel NoOfLinks1 = new JLabel(linksCnt, JLabel.LEFT);
     			NoOfLinks1.setFont(fontBody);
     			info10.add(NoOfLinks1);
     			JLabel emptySpace10 = new JLabel("                                                                                               ", JLabel.LEFT);
     			info10.add(emptySpace10);
    		}
    		Body.add(info10);
    		
    	}catch (IOException e){
			System.out.println(e.getMessage());			
		}    	
		
	  	file = new File("./KNOTData/data_out.out");
    	str = null; 
    	try{
    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
    		fis.readLine();
    		fis.readLine();
    		fis.readLine();
    		fis.readLine();
    		fis.readLine();
    		fis.readLine();
    		txtresult.append("12. ");
    		    		
    		if((str = fis.readLine()) != null){    			
    			txtresult.append(str+"\n");  	
    		}
    		
    		if((str = fis.readLine()) != null){
    			coherenceInfo = str;
    			txtresult.append("    "+coherenceInfo+"\n");
    		}
    		
    		while((str = fis.readLine()) != null) {
    			txtresult.append(str+"\n");    			
    		}

    		txtresult.append("*********************************************\n\n"); 
    	}catch (IOException e){
			System.out.println(e.getMessage());			
		}
    	
    	// 2. Date and Time to Perform the Knowledge Structure Map
    	if(ksv_ampm==0) {
			data.add(ksv_year+"."+ksv_month+"."+ksv_date+" "+ksv_hour+":"+ksv_min+":"+ksv_sec+" AM");
		}
		else {
			data.add(ksv_year+"."+ksv_month+"."+ksv_date+" "+ksv_hour+":"+ksv_min+":"+ksv_sec+" PM");
		}  
    	
    	// 3. No. of User Selected Terms
    	data.add(userSelectedTermsCnt);
    	
    	// 4. Task Time to Generate KS Map
    	data.add(Double.toString((double)taskTime/1000)+" seconds");
    	
    	// 5. No. of Nodes
    	data.add(nodesCnt);
    	
    	// 6. No. of Links
    	data.add(linksCnt);
    	
    	// 7. Coherence 
    	StringTokenizer st = new StringTokenizer(coherenceInfo, " ");
		if(st.hasMoreTokens()) {
			String coherence = st.nextToken();
			data.add(coherence);
			JLabel Coherence = new JLabel("11. Coherence :- ", JLabel.LEFT);
			Coherence.setFont(fontBody);
 			info11.add(Coherence);
 			JLabel Coherence1 = new JLabel(coherence, JLabel.LEFT);
			Coherence1.setFont(fontBody);
 			info11.add(Coherence1);
 			JLabel emptySpace11 = new JLabel("                                                                                                   ", JLabel.LEFT);
 			info11.add(emptySpace11);
		}
		Body.add(info11);
		    	
		// 8. Weight Information
		data.add("[FullText:Title:Content=1:4:1]");
		
		// 9. File Name w/ Extension
		data.add(fnameWithExtension);
		
		// 10. File Uploaded Date & Time
		if(ampm==0) {
			data.add(year+"."+month+"."+date+" "+hour+":"+min+":"+sec+" AM");
		}
		else {
			data.add(year+"."+month+"."+date+" "+hour+":"+min+":"+sec+" PM");
		}  
		
		// 11. Total No. of Words
		data.add(Integer.toString(PPTBased_KS_CreationProcess.total_word_cnt));
		
		// 12. No. of Auto Extracted Terms
		data.add(Integer.toString(PPTBased_KS_CreationProcess.auto_extracted_terms_cnt));    	
           	    	
    	frame.getContentPane().add(BorderLayout.EAST, rightPanel);
    	frame.setVisible(true);    	
    	
    	// 13. Image of KS Map
    	data.add(saveImageKSMap());
    	
    	if(data.isEmpty()) {
    		txtresult.append("[USAGE] Use this button after drawing the visualization task!\n\n");
    	}
    	else {
    		for(int i=0; i<data.size(); i++) {
    			finalData[callCount][i] = data.get(i);
    		}
    		callCount++;
    		data.clear();	
    	}
    	   
    	KSVisualization ksv1 = new KSVisualization();
    	Body.add(ksv1.printableKSGenerate());
    	printFrame.add(Body);    	
    	  
    	scrollBar = new JScrollPane(Body, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        printFrame.add(scrollBar);
    }
    
	protected void openFileMenuItemActionPerformed(ActionEvent evt) {
		printFrame.remove(scrollBar);
		selectedkeywordList.removeAll();
		
		PPTBased_KS_CreationProcess.initiallyPerformed = true;
		PPTBased_KS_CreationProcess.total_word_cnt = 0;
			
    	int returnVal = fileChooser.showOpenDialog(this.getFrame());
    	text = "";
    	    	
    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    	   keywordList.removeAll();
           try {
        	   File file = fileChooser.getSelectedFile();
                      	   
               cal = Calendar.getInstance();
               year = cal.get(Calendar.YEAR);
               month = cal.get(Calendar.MONTH)+1;
               date = cal.get(Calendar.DATE);
                   
               hour = cal.get(Calendar.HOUR);
               min = cal.get(Calendar.MINUTE);
               sec = cal.get(Calendar.SECOND);
               ampm = cal.get(Calendar.AM_PM); 
               
               OPENFile_BASE_PATH = file.getParent()+ file.separator;
               System.out.println("* OPENFile_BASE_PATH: "+OPENFile_BASE_PATH);
               path = file.toString();
               int pos1 = path.lastIndexOf("\\");
               filename = path.substring(pos1+1);
               System.out.println("* filename with ppt: "+filename);
               int pos2 = filename.lastIndexOf(".");
               fname = filename.substring(0, pos2);
               System.out.println("* filename: "+fname);
               //extension = filename.substring(pos2+1);
               //System.out.println("* file extension: "+extension);
               
               PPTBased_KS_CreationProcess pe2 = new PPTBased_KS_CreationProcess();
               try {
            	   pe2.extractKeywords(fname, path);
               } catch(Exception e) {
                   System.out.println(e);
               }
                   
               String extractedPath = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";

               System.out.println("* Extracted Path: "+extractedPath);	
               File file2 = new File(extractedPath);
                            
               BufferedReader br = new BufferedReader(new FileReader(file2));
               String line;
               int i=0;
               while ((line = br.readLine()) != null) {
                   keywordList.add(line);
                   keywordList.setMultipleMode(true);
                   selectedkeywordList.setMultipleMode(true);
               }
                                          
               SwingUtilities.invokeLater(new Runnable() {
            	   public void run() {
            	   }             
               });
           } catch (IOException ex) {
        	   Logger.getLogger(DemoView.class.getName()).log(Level.SEVERE, null, ex);
           }

        } else {
        	// ...
        }          
	}
	protected void printMenuItemActionPerformed(ActionEvent evt) {
		PrintComponents pc = new PrintComponents(printFrame);
	}
	
	protected void printActionPerformed(ActionEvent evt) {
		PrintComponents pc = new PrintComponents(printFrame);
	}

	protected void showAnalysisResultsTableActionPerformed(ActionEvent evt) {
		isSaveXMLFile = false;
		//tableFrame = new JFrame("Analysis Results as a Table");
		JMenuBar tableMenuBar = new JMenuBar();
		JMenu tableFileMenu = new JMenu("File");
		JMenu tableHelpMenu = new JMenu("Help"); 
		JMenuItem tableSaveTEXTFileMenuItem = new JMenuItem("Save as a TEXT File Format(*.txt)");
		JMenuItem tableSaveXMLFileMenuItem = new JMenuItem("Save as an XML File Format(*.xml)");
		JMenuItem tableViewXSLTFileMenuItem = new JMenuItem("View as an HTML File Format");
		JMenuItem tableExitMenuItem = new JMenuItem("Exit");
		JMenuItem tableAboutMenuItem = new JMenuItem("* CourseShare knowledge structure ver1.0 written by Kyung Jin Kim");
		
		tableFileMenu.add(tableSaveTEXTFileMenuItem);
		tableFileMenu.add(tableSaveXMLFileMenuItem);
		tableFileMenu.add(tableViewXSLTFileMenuItem);
		tableFileMenu.add(tableExitMenuItem);
		tableHelpMenu.add(tableAboutMenuItem);
		tableMenuBar.add(tableFileMenu);
		tableMenuBar.add(tableHelpMenu);
		tableFrame.setJMenuBar(tableMenuBar);
		
		northSavePanel = new JPanel();
		northSavePanel.setBackground(Color.white);
		JButton saveTEXTFileButton = new JButton("Save as a TEXT File Format(*.txt)");
		JButton saveXMLFileButton = new JButton("Save as an XML File Format(*.xml)");
		JButton viewXSLTFileButton = new JButton("View as an HTML File Format");
		JLabel emptySpace1 = new JLabel("     ");
		JLabel emptySpace2 = new JLabel("     ");
		northSavePanel.add(saveTEXTFileButton);
		northSavePanel.add(emptySpace1);
		northSavePanel.add(saveXMLFileButton);
		northSavePanel.add(emptySpace2);
		northSavePanel.add(viewXSLTFileButton);
		tableFrame.getContentPane().add(BorderLayout.NORTH, northSavePanel);
		
		tableExitMenuItem.setMnemonic(KeyEvent.VK_C);	
		tableExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		
		showTable = new JTable(finalData, columnName);
		tableFrame.add(new JScrollPane(showTable));
		tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		showTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		showTable.getColumnModel().getColumn(0).setPreferredWidth(95); // Column 0: Case Number
		showTable.getColumnModel().getColumn(1).setPreferredWidth(195); // Column 1: KS Map Generated Date & Time
		showTable.getColumnModel().getColumn(2).setPreferredWidth(170); // Column 2: No. of User Selected Terms
		showTable.getColumnModel().getColumn(3).setPreferredWidth(228); // Column 3: Processing Time to Generate KS Map
		showTable.getColumnModel().getColumn(4).setPreferredWidth(90); // Column 4: No. of Nodes
		showTable.getColumnModel().getColumn(5).setPreferredWidth(80); // Column 5: No. of Links
		showTable.getColumnModel().getColumn(6).setPreferredWidth(75); // Column 6: Coherence
		showTable.getColumnModel().getColumn(7).setPreferredWidth(170); // Column 7: Weight Information 
		showTable.getColumnModel().getColumn(8).setPreferredWidth(150); // Column 8: File Name w/ Extension
		showTable.getColumnModel().getColumn(9).setPreferredWidth(165); // Column 9: File Uploaded Date & Time
		showTable.getColumnModel().getColumn(10).setPreferredWidth(117); // Column 10: Total No. of Words
		showTable.getColumnModel().getColumn(11).setPreferredWidth(175); // Column 11: No. of Auto Extracted Terms 		
		showTable.getColumnModel().getColumn(12).setPreferredWidth(110); // Column 12: Image of KS Map
		
		tableSaveTEXTFileMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableSaveTEXTFileMenuItemActionPerformed(evt);
    		}
    	});
		
		tableSaveXMLFileMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableSaveXMLFileMenuItemActionPerformed(evt);
    		}
    	});
		
		saveTEXTFileButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableSaveTEXTFileMenuItemActionPerformed(evt);
    		}
    	});
		
		saveXMLFileButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableSaveXMLFileMenuItemActionPerformed(evt);
    		}
    	});
		
		tableViewXSLTFileMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableViewXSLTFileMenuItemActionPerformed(evt);
    		}
    	});
		
		viewXSLTFileButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableViewXSLTFileMenuItemActionPerformed(evt);
    		}
    	});
					
		tableExitMenuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
    			tableExitMenuItemActionPerformed(evt);
    		}
    	});
		
		tableFrame.setSize(1855, 400);
		tableFrame.setVisible(true);				
	}
	
	protected void tableSaveTEXTFileMenuItemActionPerformed(ActionEvent evt) {			
		int returnVal = saveTXTFileChooser.showSaveDialog(this.getFrame());
    	   	
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    		File file = saveTXTFileChooser.getSelectedFile();
    		System.out.println(file);
    		
    		TXTFile_BASE_LINK = file.getParent()+file.separator;
      		String tableFilePath = file.toString();
    		int pos1 = tableFilePath.lastIndexOf("\\");
    		tableFileName = tableFilePath.substring(pos1+1); 
            //String tableFileNameWithExtension = tableFileName;
            int pos2 = tableFileName.lastIndexOf(".");
            tableFileExtension = tableFileName.substring(pos2+1);
    		if(tableFileExtension.compareToIgnoreCase("txt")==0) {
    			tableFilePath = file.toString();
    		}
    		else {
    			tableFilePath = file.toString()+".txt";
    		}
    		pos1 = tableFilePath.lastIndexOf("\\");
    		tableFileName = tableFilePath.substring(pos1+1); 
            System.out.println("* filename with txt: "+tableFileName);
            
            pos2 = tableFileName.lastIndexOf(".");
            tableFileExtension = tableFileName.substring(pos2+1);
    		tableFileName = tableFileName.substring(0, pos2);
    		System.out.println("* filename: "+tableFileName);
    		System.out.println("* extension: "+tableFileExtension);
    		    	   
    		try {
    			BufferedWriter out = new BufferedWriter(new FileWriter(TXTFile_BASE_LINK+tableFileName+"."+tableFileExtension));
    	   			
        		// Save TEXT File
        		if(tableFileExtension.compareToIgnoreCase("txt")==0) {
        			// Column Head
            		out.write("Case Number, ");
            		out.write("KS Map Generated Date & Time, ");
            		out.write("No. of User Selected Terms, ");
            		out.write("Processing Time to Generate KS Map, ");
            		out.write("No. of Nodes, ");
            		out.write("No. of Links, ");
            		out.write("Coherence, ");
            		out.write("Weight Information, ");
            		out.write("File Name w/ Extension, ");
            		out.write("File Uploaded Date & Time, ");
            		out.write("Total No. of Words, ");
            		out.write("No. of Auto Extracted Terms");
            		out.newLine();
            			 
            		// Column Body
            		for(int i=0; i<caseNumber; i++) {
            			for(int j=0; j<11; j++) {
            				out.write(finalData[i][j].toString()+", ");
            			}
            			out.write(finalData[i][11].toString());
            			out.newLine();
            		}
            			
            		out.close();
        		}
        	} catch (IOException e) {
    			System.err.println(e);
    			System.exit(1);
    		}		 	
    	}    
	}
	
	protected void tableSaveXMLFileMenuItemActionPerformed(ActionEvent evt) {		
		int returnVal = saveXMLFileChooser.showSaveDialog(this.getFrame());
			   	
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    		File file = saveXMLFileChooser.getSelectedFile();
    		    		
    		boolean match = false;
    		for(int i=0; i<26; i++) {
    			int upperCase = 'A'+i;
    			String temp = (char)upperCase+":";    			
    			if(file.getParent().compareToIgnoreCase(temp+"\\")==0) {
    				XMLFile_BASE_LINK = file.getParent();
    				match = true;
    				break;
    			}
    		}
    		
    		if(!match) {
    			XMLFile_BASE_LINK = file.getParent() + file.separator; 
    		}    		
    		    		
    		System.out.println("XMLFile_BASE_LINK: "+XMLFile_BASE_LINK);
    		saveXMLSchemaFile(XMLFile_BASE_LINK);
    		saveXMLTFile(XMLFile_BASE_LINK);
    		    		
    		String tableFilePath = file.toString();
    		int pos1 = tableFilePath.lastIndexOf("\\");
    		tableFileName = tableFilePath.substring(pos1+1); 
            //String tableFileNameWithExtension = tableFileName;
            int pos2 = tableFileName.lastIndexOf(".");
            tableFileExtension = tableFileName.substring(pos2+1);
    		if(tableFileExtension.compareToIgnoreCase("xml")==0) {
    			tableFilePath = file.toString();
    		}
    		else {
    			tableFilePath = file.toString()+".xml";
    		}
    		pos1 = tableFilePath.lastIndexOf("\\");
    		tableFileName = tableFilePath.substring(pos1+1); 
            System.out.println("* filename with xml: "+tableFileName);
            
            pos2 = tableFileName.lastIndexOf(".");
            tableFileExtension = tableFileName.substring(pos2+1);
    		tableFileName = tableFileName.substring(0, pos2);
    		System.out.println("* filename: "+tableFileName);
    		System.out.println("* extension: "+tableFileExtension);
        	   
    		try {
    			BufferedWriter out = new BufferedWriter(new FileWriter(XMLFile_BASE_LINK+tableFileName+"."+tableFileExtension));
    	   			
         		// Save XML File
        		if(tableFileExtension.compareToIgnoreCase("xml")==0) {
        			// XML Prolog
        			out.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?>"); // Processing Instruction (PI)
        			out.newLine(); out.newLine();
        			out.write("<?xml-stylesheet type=\"text/xsl\" href=\""+XMLFile_BASE_LINK+"CourseShare_KS_View.xsl\" ?>");
        			out.newLine(); out.newLine();
        			out.write("<KSRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""+XMLFile_BASE_LINK+"CourseShare_KS_Schema.xsd\">");
        			out.newLine();
        				
            		// Column Body - XML Body
            		for(int i=0; i<caseNumber; i++) {
            			out.newLine(); out.write("<KSInfo>"); out.newLine();
            			out.write("<CaseNumber>"); out.write(finalData[i][0].toString()); out.write("</CaseNumber>"); out.newLine();
                		out.write("<KSMapGeneratedDateTime>"); out.write(finalData[i][1].toString()); out.write("</KSMapGeneratedDateTime>"); out.newLine();
                		out.write("<NoOfUserSelectedTerms>"); out.write(finalData[i][2].toString()); out.write("</NoOfUserSelectedTerms>"); out.newLine();
                		out.write("<ProcessingTimeToGenerateKSMap>"); out.write(finalData[i][3].toString()); out.write("</ProcessingTimeToGenerateKSMap>"); out.newLine();
                		out.write("<NoOfNodes>"); out.write(finalData[i][4].toString()); out.write("</NoOfNodes>"); out.newLine(); 
                		out.write("<NoOfLinks>"); out.write(finalData[i][5].toString()); out.write("</NoOfLinks>"); out.newLine();
                		out.write("<Coherence>"); out.write(finalData[i][6].toString()); out.write("</Coherence>"); out.newLine();
                		out.write("<WeightInformation>"); out.write(finalData[i][7].toString()); out.write("</WeightInformation>"); out.newLine();
                		out.write("<FileNameWithExtension>"); out.write(finalData[i][8].toString()); out.write("</FileNameWithExtension>"); out.newLine();
                		out.write("<FileUploadedDateTime>"); out.write(finalData[i][9].toString()); out.write("</FileUploadedDateTime>"); out.newLine();
                		out.write("<TotalNoOfWords>"); out.write(finalData[i][10].toString()); out.write("</TotalNoOfWords>"); out.newLine();
                		out.write("<NoOfAutoExtractedTerms>"); out.write(finalData[i][11].toString()); out.write("</NoOfAutoExtractedTerms>"); out.newLine();
                		out.write("<ImageOfKSMap>"); out.write(finalData[i][12].toString()); out.write("</ImageOfKSMap>"); out.newLine();
                		CMD_CopyImageFile(finalData[i][12].toString());
                		out.write("</KSInfo>");
            			out.newLine();
            		}
            			
            		out.newLine();
            		out.write("</KSRoot>");
            		out.close();
            		isSaveXMLFile = true;
        		}       		
 
    		} catch (IOException e) {
    			System.err.println(e);
    			System.exit(1);
    		}		 	
    	}		
	}
	
	protected void tableViewXSLTFileMenuItemActionPerformed(ActionEvent evt) {
		CMD_IExplore();
	}
    
	protected void tableExitMenuItemActionPerformed(ActionEvent evt) {
		tableFrame.dispose();
	}	
	
	protected void saveAsDefaultXMLFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(BASE_LINK+defaultXMLFile));
			
			// Save XML File
    		// XML Prolog
    		out.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?>"); // Processing Instruction (PI)
    		out.newLine(); out.newLine();
    		out.write("<?xml-stylesheet type=\"text/xsl\" href=\"CourseShare_KS_View.xsl\" ?>");
    		out.newLine(); out.newLine();
    		out.write("<KSRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""+BASE_LINK+"CourseShare_KS_Schema.xsd\">");
    		out.newLine();
    				
        	// Column Body - XML Body
        	for(int i=0; i<caseNumber; i++) {
        		out.newLine(); out.write("<KSInfo>"); out.newLine();
        		out.write("<CaseNumber>"); out.write(finalData[i][0].toString()); out.write("</CaseNumber>"); out.newLine();
            	out.write("<KSMapGeneratedDateTime>"); out.write(finalData[i][1].toString()); out.write("</KSMapGeneratedDateTime>"); out.newLine();
            	out.write("<NoOfUserSelectedTerms>"); out.write(finalData[i][2].toString()); out.write("</NoOfUserSelectedTerms>"); out.newLine();
            	out.write("<ProcessingTimeToGenerateKSMap>"); out.write(finalData[i][3].toString()); out.write("</ProcessingTimeToGenerateKSMap>"); out.newLine();
            	out.write("<NoOfNodes>"); out.write(finalData[i][4].toString()); out.write("</NoOfNodes>"); out.newLine(); 
            	out.write("<NoOfLinks>"); out.write(finalData[i][5].toString()); out.write("</NoOfLinks>"); out.newLine();
            	out.write("<Coherence>"); out.write(finalData[i][6].toString()); out.write("</Coherence>"); out.newLine();
            	out.write("<WeightInformation>"); out.write(finalData[i][7].toString()); out.write("</WeightInformation>"); out.newLine();
            	out.write("<FileNameWithExtension>"); out.write(finalData[i][8].toString()); out.write("</FileNameWithExtension>"); out.newLine();
            	out.write("<FileUploadedDateTime>"); out.write(finalData[i][9].toString()); out.write("</FileUploadedDateTime>"); out.newLine();
            	out.write("<TotalNoOfWords>"); out.write(finalData[i][10].toString()); out.write("</TotalNoOfWords>"); out.newLine();
            	out.write("<NoOfAutoExtractedTerms>"); out.write(finalData[i][11].toString()); out.write("</NoOfAutoExtractedTerms>"); out.newLine();
            	out.write("<ImageOfKSMap>"); out.write(finalData[i][12].toString()); out.write("</ImageOfKSMap>"); out.newLine();
            	out.write("</KSInfo>");
        		out.newLine();
        	}
        			
        	out.newLine();
        	out.write("</KSRoot>");
        	out.close();   	
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}		 
	}
	
	protected void saveXMLSchemaFile(String filePath) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath+"CourseShare_KS_Schema.xsd"));
	   			
     		// Save XML Schema File
    		out.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?>"); // Processing Instruction (PI)
    		out.newLine(); out.newLine();
    		out.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">");
    		out.newLine(); out.newLine();
    		out.write("<KSRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""+filePath+"CourseShare_KS_Schema.xsd\">");
    		out.newLine();
    		out.write("\t<xs:element name=\"KSRoot\">");
    		out.newLine();
    		out.write("\t\t<xs:complexType>");
    		out.newLine();
    		out.write("\t\t\t<xs:sequence>");
    		out.newLine();
    		out.write("\t\t\t\t<xs:element name=\"KSInfo\" minOccurs=\"0\" maxOccurs=\"unbounded\">");
    		out.newLine();
    		out.write("\t\t\t\t\t<xs:complexType>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t<xs:sequence>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 1. Case Number -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"CaseNumber\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 2. KS Map Generated Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"KSMapGeneratedDateTime\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 3. No. of User Selected Terms  -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"NoOfUserSelectedTerms\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 4. Processing Time to Generate KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"ProcessingTimeToGenerateKSMap\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 5. No. of Nodes -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"NoOfNodes\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 6. No. of Links -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"NoOfLinks\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 7. Coherence -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"Coherence\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 8. Weight Information -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"WeightInformation\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 9. File Name w/ Extension -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"FileNameWithExtension\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 10. File Uploaded Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"FileUploadedDateTime\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 11. Total No. of Words -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"TotalNoOfWords\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 12. No. of Auto Extracted Terms -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"NoOfAutoExtractedTerms\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 13. Image of KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<xs:element name=\"ImageOfKSMap\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t\t</xs:sequence>");
    		out.newLine();
    		out.write("\t\t\t\t\t</xs:complexType>");
    		out.newLine();
    		out.write("\t\t\t\t</xs:element>");
    		out.newLine();
    		out.write("\t\t\t</xs:sequence>");
    		out.newLine();
    		out.write("\t\t</xs:complexType>");
    		out.newLine();
    		out.write("\t</xs:element>");
    		out.newLine();
    		out.write("</xs:schema>");
    		out.newLine();
    		out.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}	
	}
	
	protected void saveXMLTFile(String filePath) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath+"CourseShare_KS_View.xsl"));
	   			
     		// Save XSLT File
    		out.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?>"); // Processing Instruction (PI)
    		out.newLine(); out.newLine();
    		out.write("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">");
    		out.newLine();
    		out.write("\t<xsl:template match=\"/\">");
    		out.newLine();
    		out.write("\t\t<html>");
    		out.newLine();
    		out.write("\t\t\t<head>");
    		out.newLine();
    		out.write("\t\t\t\t<title>[CourseShare] Knowledge Structure - Analysis Results</title>");
    		out.newLine();
    		out.write("\t\t\t\t<style type=\"text/css\">");
    		out.newLine();
    		out.write("\t\t\t\t\th4 {");
    		out.newLine();
    		out.write("\t\t\t\t\t\tfont-style : \"italic\";");
    		out.newLine();
    		out.write("\t\t\t\t\t\tfont-weight : \"bold\";");
    		out.newLine();
    		out.write("\t\t\t\t\t\tcolor : \"rgb(30, 50, 255)\";");
    		out.newLine();
    		out.write("\t\t\t\t\t\tfont-family : \"georgia\"");
    		out.newLine();
    		out.write("\t\t\t\t\t}");
    		out.newLine();
    		out.write("\t\t\t\t</style>");
    		out.newLine();
    		out.write("\t\t\t</head>");
    		out.newLine();
    		out.write("\t\t\t<body bgcolor=\"#FFFF99\">");
    		out.newLine();
    		out.write("\t\t\t\t<p align=\"center\"><font color=\"##009966\" size=\"6\"> *** [CourseShare] Knowledge Structure - Analysis Results *** </font></p>");
    		out.newLine();
    		out.write("\t\t\t\t<br />");
    		out.newLine();
    		out.write("\t\t\t\t<table border=\"1\" cellpadding=\"5\" cellspacing=\"2\" align=\"center\">");
    		out.newLine();
    		out.write("\t\t\t\t\t<THEAD>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t<tr align=\"center\">");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 1. Case Number -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Case Number</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 2. KS Map Generated Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">KS Map Generated Date &amp; Time</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 3. No. of User Selected Terms  -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">No. of User Selected Terms</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 4. Processing Time to Generate KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Processing Time to Generate KS Map</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 5. No. of Nodes -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">No. of Nodes </font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 6. No. of Links -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">No. of Links</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 7. Coherence -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Coherence</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 8. Weight Information -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Weight Information</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 9. File Name w/ Extension -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">File Name w/ Extension</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 10. File Uploaded Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">File Uploaded Date &amp; Time</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 11. Total No. of Words -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Total No. of Words</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 12. No. of Auto Extracted Terms -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">No. of Auto Extracted Terms</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<!-- 13. Image of KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t\t\t\t<th bgcolor=\"#006666\"><font color=\"#FFFFFF\">Image of KS Map</font></th>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t</tr>");
    		out.newLine();
    		out.write("\t\t\t\t\t</THEAD>");
    		out.newLine();
    		out.write("\t\t\t\t\t<TBODY>");
    		out.newLine();
    		out.write("\t\t\t\t\t\t<xsl:apply-templates select=\"KSRoot\" />");
    		out.newLine();
    		out.write("\t\t\t\t\t</TBODY>");
    		out.newLine();
    		out.write("\t\t\t\t</table>");
    		out.newLine();
    		out.write("\t\t\t</body>");
    		out.newLine();
    		out.write("\t\t</html>");
    		out.newLine();
    		out.write("\t</xsl:template>");
    		out.newLine(); out.newLine();
    		out.write("\t<xsl:template match=\"KSRoot\">");
    		out.newLine();
    		out.write("\t\t<xsl:for-each select=\"./KSInfo\">");
    		out.newLine();
    		out.write("\t\t\t<tr align=\"center\">");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 1. Case Number -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./CaseNumber\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 2. KS Map Generated Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./KSMapGeneratedDateTime\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 3. No. of User Selected Terms  -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./NoOfUserSelectedTerms\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 4. Processing Time to Generate KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./ProcessingTimeToGenerateKSMap\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 5. No. of Nodes -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./NoOfNodes\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 6. No. of Links -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./NoOfLinks\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 7. Coherence -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./Coherence\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 8. Weight Information -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./WeightInformation\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 9. File Name w/ Extension -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./FileNameWithExtension\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 10. File Uploaded Date & Time -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./FileUploadedDateTime\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 11. Total No. of Words -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./TotalNoOfWords\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 12. No. of Auto Extracted Terms -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:value-of select=\"./NoOfAutoExtractedTerms\" /></td>");
    		out.newLine();
    		out.write("\t\t\t\t<!-- 13. Image of KS Map -->");
    		out.newLine();
    		out.write("\t\t\t\t<td><xsl:apply-templates select=\"./ImageOfKSMap\" /></td>");
    		out.newLine();
    		out.write("\t\t\t</tr>");
    		out.newLine();
    		out.write("\t\t</xsl:for-each>");
    		out.newLine();
    		out.write("\t</xsl:template>");
    		out.newLine(); out.newLine();
    		out.write("\t<xsl:template match=\"ImageOfKSMap\">");
    		out.newLine();
    		out.write("\t\t<xsl:element name=\"img\">");
    		out.newLine();
    		out.write("\t\t\t<xsl:attribute name=\"src\">");
    		out.newLine();
    		out.write("\t\t\t\t<xsl:value-of select=\".\" />");
    		out.newLine();
    		out.write("\t\t\t</xsl:attribute>");
    		out.newLine(); out.newLine();
    		out.write("\t\t\t<xsl:attribute name=\"width\">500</xsl:attribute>");
    		out.newLine();
    		out.write("\t\t\t<xsl:attribute name=\"height\">500</xsl:attribute>");
    		out.newLine();
    		out.write("\t\t\t<xsl:attribute name=\"alt\">Image of KS Map</xsl:attribute>");
    		out.newLine();
    		out.write("\t\t</xsl:element>");
    		out.newLine();
    		out.write("\t</xsl:template>");
    		out.newLine();
    		out.write("</xsl:stylesheet>");
    		out.newLine();
    		out.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}			
	}
	
	protected String saveImageKSMap() {
		int i = 0;
	    String filename = Integer.toString(i)+".jpg";
	    File f = new File("./output/"+filename);
	    while(f.exists()) {
	        i++;
	        filename = Integer.toString(i)+".jpg";
	        f = new File("./output/"+filename);
	    }
	   	    
		Dimension size = KSMap.getSize();
		BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		g2 = bi.createGraphics();
		KSMap.paint(g2);
        g2.dispose();
        
		try {
			ImageIO.write(bi, "jpg", new File("./output/"+filename));
		} catch (IOException ioe) {
			System.out.println("Test write help: " + ioe.getMessage());
		}
				
		return filename;
	}
	
	protected String saveImageKSMapAsUserSelectedPath() {
		int i = 0;
	    String filename = Integer.toString(i)+".jpg";
	    File f = new File(XMLFile_BASE_LINK+filename);
	    while(f.exists()) {
	        i++;
	        filename = Integer.toString(i)+".jpg";
	        f = new File(XMLFile_BASE_LINK+filename);
	    }
	   	    
		Dimension size = KSMap.getSize();
		BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		g2 = bi.createGraphics();
		KSMap.paint(g2);
        g2.dispose();
        
		try {
			ImageIO.write(bi, "jpg", new File(XMLFile_BASE_LINK+filename));
		} catch (IOException ioe) {
			System.out.println("Test write help: " + ioe.getMessage());
		}
				
		return filename;
	}
	
	public void CMD_IExplore() {
		try {
			if(tableFileExtension.compareToIgnoreCase("xml")==0 && isSaveXMLFile==true) {
				ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start \"title\" iexplore.exe "+XMLFile_BASE_LINK+tableFileName+"."+tableFileExtension);
				Process p = pb.start();
				saveAsDefaultXMLFile();
				isSaveXMLFile = false;
			}
			else {	
				saveAsDefaultXMLFile();        		        	        	
	        	ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start \"title\" iexplore.exe "+BASE_LINK+defaultXMLFile);
				Process p = pb.start();					
			}			
		} catch(IOException e) {
			System.err.println("Error! Fail to execute external commend." + e.getMessage());
		}
	}
	
	public void CMD_CopyImageFile(String KSMap) {
		try {			
			ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "copy \""+BASE_LINK+KSMap+"\" \""+XMLFile_BASE_LINK+KSMap+"\"");
			Process p = pb.start();								
		} catch(IOException e) {
			System.err.println("Error! Fail to execute external commend." + e.getMessage());
		}
	}
}

class PrintComponents implements Printable, ActionListener {
	
	JFrame frameToPrint;
	
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException { 
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		frameToPrint.print(g);
		return PAGE_EXISTS;
	}

	public void actionPerformed(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();  
		if (ok) {     
			try {    
				job.print();    
			} catch (PrinterException pe) {      
				System.out.println("Error printing: " + pe);
			}
		}
	}

	 public PrintComponents(JFrame f) {
		 frameToPrint = f;
		 frameToPrint.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 UIManager.put("swing.boldMetal", Boolean.FALSE);
		 frameToPrint.setSize(620, 900);
		 String printHead = "Analysis Results with Knowledge Structure Map ";
	     JLabel head = new JLabel(printHead, JLabel.CENTER);
	     Font fontHead = new Font("Serif", Font.BOLD+Font.ITALIC, 18);
	     head.setFont(fontHead);
	     JPanel topPanel = new JPanel();
		 topPanel.setBackground(Color.white);
		 JButton printButton = new JButton("Print");
		 printButton.addActionListener(this);
		 topPanel.add(head);
		 topPanel.add(printButton);
		 frameToPrint.setLocationRelativeTo(null);
		 frameToPrint.getContentPane().add(BorderLayout.NORTH, topPanel);
		 //frameToPrint.setResizable(false);
		 frameToPrint.setVisible(true);		
	} 
}
