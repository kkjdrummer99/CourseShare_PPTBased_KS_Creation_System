/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */

//package edu.uci.ics.jung.samples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.math.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.*;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.decorators.VertexIconShapeTransformer;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * A demo that shows flag images as vertices, and uses unicode
 * to render vertex labels.
 * 
 * @author Tom Nelson 
 * 
 */

class MyLink {
  double capacity;
	double weight;
	int id;
	
	public MyLink(int id, double weight, double capacity){
		this.id = id;
		this.weight = weight;
		this.capacity = capacity;
		
	}
	
	public MyLink(int id, int weight, int capacity){
		this.id = id;
		this.weight = weight;
		this.capacity = capacity;
		
	}
	
	public String toString(){
		return "E"+id;
	}
}

public class KSVisualization {
	private JPanel APanel;
	private Border KSMapBorder = BorderFactory.createLineBorder(Color.gray);
	int userSelectedTermsCount = 0;
	int[] termIndex;
	
	/**
     * the graph
     */
    Graph<Integer,MyLink> graph;

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<Integer,MyLink> vv;
    
    boolean showLabels;
    
    public JPanel KSGenerate() {
    	// create a simple graph for the demo
        graph = new SparseMultigraph<Integer,MyLink>();               
        
        Integer[] v = createVertices(getTermNum());
        createEdges(v);
        
        vv =  new VisualizationViewer<Integer,MyLink>(new KKLayout<Integer,MyLink>(graph));
        //vv.setPreferredSize(new Dimension(540,500));
        vv.setPreferredSize(new Dimension(610,570)); // 570, 640 | 565, 640 | 565, 570
        
        vv.getRenderContext().setVertexLabelTransformer(new UnicodeVertexStringer<Integer>(v));
        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.magenta));
        vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.magenta));
        VertexIconShapeTransformer<Integer> vertexIconShapeFunction =
            new VertexIconShapeTransformer<Integer>(new EllipseVertexShapeTransformer<Integer>());
        DefaultVertexIconTransformer<Integer> vertexIconFunction = new DefaultVertexIconTransformer<Integer>();
        vv.getRenderContext().setVertexShapeTransformer(vertexIconShapeFunction);
        vv.getRenderContext().setVertexIconTransformer(vertexIconFunction);
        loadImages(v, vertexIconFunction.getIconMap());
        vertexIconShapeFunction.setIconMap(vertexIconFunction.getIconMap());
        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), new Color(0, 102, 255), Color.red));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<MyLink>(vv.getPickedEdgeState(), Color.orange, Color.cyan));
        vv.setBackground(Color.white);
        
        final int maxSize = findMaxSizeNumber();
              		
        File file = new File("./output/DESC_TERM_COUNT.txt");
        String s;
                      
    	try{
        	BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        	
        	s = fis.readLine();
        	userSelectedTermsCount = Integer.parseInt(s);
        	termIndex = new int[userSelectedTermsCount];
        	
        	int i=0;
        	while((s = fis.readLine()) != null){
        		String[] tmp = s.split("=");
        		termIndex[i] =  Integer.parseInt(tmp[1].trim());  
        		i++;
        	}        	
        	
        } catch (IOException e){
        	System.out.println(e.getMessage());    			
        }
    	
    	Transformer<Integer, Shape> vertexSize = new Transformer<Integer, Shape>() {
    		public Shape transform(Integer i) {
    			double sizeInt = termIndex[i];
 	 		   	sizeInt = (double)sizeInt/(double)maxSize;
 				sizeInt = (double)sizeInt*(double)30+10;
 	 			Ellipse2D circle = new Ellipse2D.Double(sizeInt / 2 * (-1),sizeInt / 2 * (-1), sizeInt, sizeInt);
 	 			return circle; 					
 			}		      
 		}; 	   
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
        
        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller<Integer>());        
        
        // create a frome to hold the graph
        APanel = new JPanel();
        APanel.setLayout(new BoxLayout(APanel, BoxLayout.Y_AXIS));        
        
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        
        final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer,Number>();
        vv.setGraphMouse(gm);
        
        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });

        JCheckBox lo = new JCheckBox("Show Labels");
        lo.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                showLabels = e.getStateChange() == ItemEvent.SELECTED;
                
                vv.repaint();
            }
        });
        lo.setSelected(true);
        
        JPanel controls = new JPanel();
        controls.add(plus);
        controls.add(minus);
        controls.add(lo);
        controls.add(((DefaultModalGraphMouse<Integer,Number>) gm).getModeComboBox());
        vv.setBorder(KSMapBorder);
        APanel.add(vv);
        APanel.add(controls);
                
        return APanel;
    }
    
    public JPanel printableKSGenerate() {
    	// create a simple graph for the demo
        graph = new SparseMultigraph<Integer,MyLink>();
               
        Integer[] v = createVertices(getTermNum());
        createEdges(v);
        
        vv =  new VisualizationViewer<Integer,MyLink>(new KKLayout<Integer,MyLink>(graph));
        vv.setPreferredSize(new Dimension(520, 520)); // 570, 640 | 565, 640 
        
        vv.getRenderContext().setVertexLabelTransformer(new UnicodeVertexStringer<Integer>(v));
        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.magenta));
        vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.magenta));
        VertexIconShapeTransformer<Integer> vertexIconShapeFunction =
            new VertexIconShapeTransformer<Integer>(new EllipseVertexShapeTransformer<Integer>());
        DefaultVertexIconTransformer<Integer> vertexIconFunction = new DefaultVertexIconTransformer<Integer>();
        vv.getRenderContext().setVertexShapeTransformer(vertexIconShapeFunction);
        vv.getRenderContext().setVertexIconTransformer(vertexIconFunction);
        loadImages(v, vertexIconFunction.getIconMap());
        vertexIconShapeFunction.setIconMap(vertexIconFunction.getIconMap());
        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), new Color(0, 102, 255), Color.red));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<MyLink>(vv.getPickedEdgeState(), Color.orange, Color.cyan));
        vv.setBackground(Color.white);
        
        final int maxSize = findMaxSizeNumber();
  		
        File file = new File("./output/DESC_TERM_COUNT.txt");
        String s;
                      
    	try{
        	BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        	
        	s = fis.readLine();
        	userSelectedTermsCount = Integer.parseInt(s);
        	termIndex = new int[userSelectedTermsCount];
        	
        	int i=0;
        	while((s = fis.readLine()) != null){
        		String[] tmp = s.split("=");
        		termIndex[i] =  Integer.parseInt(tmp[1].trim());  
        		i++;
        	}        	
        	
        } catch (IOException e){
        	System.out.println(e.getMessage());    			
        }
    	
    	Transformer<Integer, Shape> vertexSize = new Transformer<Integer, Shape>() {
    		public Shape transform(Integer i) {
    			double sizeInt = termIndex[i];
 	 		   	sizeInt = (double)sizeInt/(double)maxSize;
 				sizeInt = (double)sizeInt*(double)30+10;
 	 			Ellipse2D circle = new Ellipse2D.Double(sizeInt / 2 * (-1), sizeInt / 2 * (-1), sizeInt, sizeInt);
 	 			return circle; 					
 			}		      
 		}; 	   
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
        
        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller<Integer>());        
        
        // create a frome to hold the graph
        APanel = new JPanel();
        APanel.setLayout(new BoxLayout(APanel, BoxLayout.Y_AXIS));
               
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        
        final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer,Number>();
        vv.setGraphMouse(gm);
        
        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });

        JCheckBox lo = new JCheckBox("Show Labels");
        lo.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                showLabels = e.getStateChange() == ItemEvent.SELECTED;
                
                vv.repaint();
            }
        });
        lo.setSelected(true);
        
        JPanel controls = new JPanel();
        controls.add(plus);
        controls.add(minus);
        controls.add(lo);
        controls.add(((DefaultModalGraphMouse<Integer,Number>) gm).getModeComboBox());
        APanel.add(vv);
        APanel.add(controls);
                
        return APanel;
    }    
    
    class UnicodeVertexStringer<V> implements Transformer<V,String> {

        Map<V,String> map = new HashMap<V,String>();
        Map<V,Icon> iconMap = new HashMap<V,Icon>();
                       
        public UnicodeVertexStringer(V[] vertices) {
        	File file = new File("./output/DESC_TERM_COUNT.txt");
        	String s;
    		int i = 0;
    		
        	try{
        		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        		s = fis.readLine();
        		while((s = fis.readLine()) != null){
        			String[] tmp = s.split("=");
        			String modifiedVertex = tmp[0].trim()+"("+tmp[1].trim()+")";        			
        			map.put(vertices[i++], modifiedVertex);    	
        		}
        		
        	}catch (IOException e){
    			System.out.println(e.getMessage());    			
    		}        
        }
        
        /**
         * @see edu.uci.ics.jung.graph.decorators.VertexStringer#getLabel(edu.uci.ics.jung.graph.Vertex)
         */
        public String getLabel(V v) {
            if(showLabels) {
                return (String)map.get(v);
            } else {
                return "";
            }
        }
        
		public String transform(V input) {
			return getLabel(input);
		}
    }
    
    /**
     * create some vertices
     * @param count how many to create
     * @return the Vertices in an array
     */
    private Integer[] createVertices(int count) {
        Integer[] v = new Integer[count];
        for (int i = 0; i < count; i++) {
            v[i] = new Integer(i);
            graph.addVertex(v[i]);
        }
        return v;
    }

    /**
     * create edges for this demo graph
     * @param v an array of Vertices to connect
     */
    void createEdges(Integer[] v) {
    	
    	File file = new File("./KNOTData/data_pf.pf");
    	String s;
    	int id = 1;
		
    	try{
    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
    		for(int i = 0; i < 10; i++) {
    			s = fis.readLine();
    			//System.out.println(s);
    		}
    		
    		while((s = fis.readLine()) != null){
    			String[] array;
    			array = s.split("\\s+");    			   			
    			graph.addEdge(new MyLink(id++, Integer.parseInt(array[3]), Integer.parseInt(array[3])*20), v[Integer.parseInt(array[1])-1], v[Integer.parseInt(array[2])-1], EdgeType.UNDIRECTED);
    		}
    		
    	} catch (IOException e){
			System.out.println(e.getMessage());			
		}   
    }

    /**
     * A nested class to demo ToolTips
     */
    
    protected void loadImages(Integer[] vertices, Map<Integer,Icon> imageMap) {
        
        ImageIcon[] icons = null;
        try {
            icons = new ImageIcon[] {
                    new ImageIcon(getClass().getResource("/images/ks.gif")),
                    new ImageIcon(getClass().getResource("/images/ks.gif")),
                    new ImageIcon(getClass().getResource("/images/ks.gif")),
                    new ImageIcon(getClass().getResource("/images/ks.gif"))
                    
            };
        } catch(Exception ex) {
            System.err.println("You need flags.jar in your classpath to see the flag icons.");
        }
        int z = 0;
        for(int i=0; icons != null && i<vertices.length; i++) {
            imageMap.put(vertices[i],icons[z%icons.length]);
        }
    }
    
    /**
     * a driver for this demo
     */
    public static int getTermNum() {
    	File file = new File("./output/DESC_TERM.txt");
    	String s;
		int TermNum;
		TermNum = 0;
    	try{
    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
    		
    		s = fis.readLine();
    	    TermNum = Integer.parseInt(s);    	
    		
    	}catch (IOException e){
			System.out.println(e.getMessage());
			
		}
    	
    	return TermNum;
    }
    
    public int findMaxSizeNumber() {
    	File file = new File("./output/DESC_TERM_COUNT.txt");
        String s;
        int max=0;
                      
    	try{
        	BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        	s = fis.readLine();
        	        	
        	while((s = fis.readLine()) != null){
        		String[] tmp = s.split("=");
        		int tempNum = Integer.parseInt(tmp[1].trim()); 
        		if(max < tempNum) {
        			max = tempNum;
        		};       		
        	}       	
        } catch (IOException e){
        	System.out.println(e.getMessage());    			
        }
    	
    	return max;   	
    }
    
    /*
    public static void main(String[] args) 
    {
        new KSVisualization();
    }*/
}
