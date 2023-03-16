

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

/**
 * 
 *
 */
public class Main extends  JFrame
{
	JPanel Field1;
	JButton bStart, bRestart;
	JSlider slider;
	int ballValue;
	Frame2 frame2;
	JLabel labelMode;
	
	public  void showPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ballValue = 4;
		Field1 = new JPanel();
		Field1.setLayout(null);
		Border border =  BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		Field1.setBorder(border);
		Field1.setBackground(Color.decode("#a5ffd6"));
		addLabel();
		addSlider();
		addButton();
		
		add(Field1);
		
		JPanel instructions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		instructions.setBorder(border);
		instructions.setBounds(200,350,300,80);
		
		JLabel instructionEasy = new JLabel("--->   Plot 4 bubbles for easy mode");
		instructions.add(instructionEasy);
		JLabel instructionMed = new JLabel("--->   Plot 5 bubbles for Medium mode");
		instructions.add(instructionMed);
		JLabel instructionHard = new JLabel("--->   Plot 6 bubbles for hard mode");
		instructions.add(instructionHard);
		instructions.setBackground(Color.decode("#ff8200"));
		
		Field1.add(instructions);
				
		setResizable(false);
		setSize(800,550);
		setLocationRelativeTo(null);
		setTitle("Bubble Burst");
		setVisible(true);
	}
	
	public void addLabel() {
		labelMode = new JLabel("Difficulty Level");
		labelMode.setBounds(200, 150, 150, 50);
		Field1.add(labelMode);
	}
	
	public void addButton() {
		bStart = new JButton("Start");
		bStart.setBounds(200,250,150,40);
		bStart.setMargin(new Insets(0,0,0,0));
		bStart.setBackground(Color.decode("#a8dadc"));

		Field1.add(bStart);
		bRestart = new JButton("Restart");
		bRestart.setBounds(450,250,150,40);	
		bRestart.setBackground(Color.decode("#a8dadc"));
		Field1.add(bRestart);
		addStartButtonClickListener();
		addRestartButtonClickListener();
	}
	
	public void addSlider(){
		slider = new JSlider(SwingConstants.HORIZONTAL, 1, 3,1);  
		slider.setBounds(350,150,200,50);
		slider.setBackground(Color.decode("#a8dadc"));
		Hashtable<Integer,JLabel> labels = new Hashtable<>();
        labels.put(1, new JLabel("Easy"));
        labels.put(2, new JLabel("Medium"));
        labels.put(3, new JLabel("Hard"));
        slider.setLabelTable(labels);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true); 
        slider.setSize(250, 50);
        slider.setVisible(true);
        Field1.add(slider);
        
        slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting()) {
					ballValue = slider.getValue() + 3;
				}
				
			}
		});
        
	}
	
	public void addStartButtonClickListener() {
		ActionListener startGameOnClick = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(frame2==null) {
					frame2 = new Frame2(ballValue);
					System.out.println("frame is null");
				}
				else {
					if(frame2.timerLabel == null) {
						System.out.println("label is null");
						frame2 = new Frame2(ballValue);
					}
				}
			}
		};
		bStart.addActionListener(startGameOnClick);
	}
	
	public void addRestartButtonClickListener() {
		ActionListener restartGameOnClick = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(frame2==null) {
					System.out.println("in restart: frame is null");
				}
				else {
						frame2.closeWindow();
						frame2 = null;
						frame2 = new Frame2(ballValue);
				}
			}
		};
	
		bRestart.addActionListener(restartGameOnClick);
	}
	
    public static void main( String[] args )
    {
        Main frame1 = new Main();
        frame1.showPanel();
    }
}

// JFrame class for the second window which holds the playing area JPanel
class Frame2 extends JFrame implements MouseListener, MouseMotionListener, WindowListener {
	
	Field2 panel;
	int bubbleVal, initalBubbleVal;
	boolean gameStarted;
	int level, time, startTime;
	JLabel timerLabel;
	Timer timer, randomDisplayTimer ;
	UIManager UImanager;
	
	public Frame2(int val){
		bubbleVal = val;
		initalBubbleVal = val;
		gameStarted = false;
		level = 0;
		time= 15;
		
//		reference: https://stackoverflow.com/questions/9064943/how-to-change-background-color-of-joptionpane
		
		UImanager=new UIManager();
		UImanager.put("OptionPane.background", new ColorUIResource(233, 245, 219));
		UImanager.put("Panel.background", new ColorUIResource(233, 245, 219) );
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Bubble Burst");
		setLayout(null);
		
		timerLabel =  new JLabel("Level- "+(level+1)+"   Remaining Time 00:"+time,JLabel.CENTER);
		timerLabel.setBounds(10,0,700,50);
		timerLabel.setOpaque(true);
		
		timerLabel.setBackground(new Color(176,195,198));
		timerLabel.setBorder(BorderFactory.createEtchedBorder());
		
		add(timerLabel);
		
//		panel = new Field2(val, 17,717,80,580);  /// for local system
		
		panel = new Field2(val, 10,710,70,570);   // for repl it
		panel.setLayout(null);
		panel.setBounds(10,50,700,500);
		
		
		panel.addLabel();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(this);
		Border border =  BorderFactory.createLoweredBevelBorder();
		panel.setBorder(border);
		panel.setBackground(new Color(237, 246, 249));
		add(panel);
		setSize(734,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

				
			if(bubbleVal > 0) {
//				xmin = 17, xmax=717, ymin = 80, ymax = 580    dimensions for play area locally
//				if(e.getX()<37 || e.getX() >697 || e.getY() <100 || e.getY()>560 ) {
//				xmin = 10, xmax=710, ymin = 70, ymax = 570    dimensions for play area on replit
				if(e.getX()<30 || e.getX() >690 || e.getY() <90 || e.getY()>550 ) {
					JOptionPane.showMessageDialog(this, "select a point such that bubble is inside the game field");
				}
				else {
					panel.addCircleCenterPoint(e.getX(),e.getY(),level);
					bubbleVal--;
				}
			}
			else {
				if(!gameStarted) {
					int ch = JOptionPane.showConfirmDialog(this,"Are you ready to start?","Start playing !!!!",JOptionPane.YES_NO_OPTION);
					if(JOptionPane.OK_OPTION == ch) {
						gameStarted = true;
						timer = new Timer(1000, new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								time--;
								if(time<=5) {
									timerLabel.setForeground(Color.RED);
								}
								if(time<=0) {
									timerLabel.setText("Level- "+(level+1)+"   Remaining Time 00:00");
									timer.stop();
									randomDisplayTimer.stop();
									gameEnd();
									return;
								}
								if(time<10) {
									timerLabel.setText("Level- "+(level+1)+"   Remaining Time 00:0"+time);
								}
								else {
									timerLabel.setText("Level- "+(level+1)+"   Remaining Time 00:"+time);
								}
							}
						});
						timer.setRepeats(true);
						timer.start();
						addRandomTimerForBubbleMovement();
					}	
				}
				else {
					randomDisplayTimer.stop();
					int success = panel.burstBubble(e.getX(),e.getY());
					randomDisplayTimer.restart();
					if(success == 2 && level<9) {
						timer.stop();
						randomDisplayTimer.stop();
						level++;
						time = 15-level;
						timerLabel.setText("Level- "+(level)+"   Remaining Time 00:"+time);
						gameStarted = false;
						timerLabel.setForeground(Color.BLACK);
						panel.prepareForNextLevel(level);
						randomDisplayTimer.start();
						timer.start();
						gameStarted = true;
						
					}
					else if(success == 2 && level == 9) {
//						close the frame
						timer.stop();
						randomDisplayTimer.stop();
						JOptionPane.showMessageDialog(this, "ALL LEVEL'S COMPLETE...You played a champ !!!", "Game Over",JOptionPane.INFORMATION_MESSAGE);
						dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					}
					else if(success == 0) {
						timer.stop();
						randomDisplayTimer.stop();
						
						JOptionPane.showMessageDialog(this, "OOOPS!!!!! You clicked outside bubble","Game Over",JOptionPane.INFORMATION_MESSAGE);
						dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
						
						return;
					}
					
				}
			}
			
	}
	
	public void addRandomTimerForBubbleMovement() {
		randomDisplayTimer = new Timer(1000,new ActionListener() { // interval is 1200 for replit and 1000 for local
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.moveBubbleRandomly();
			}
		});
		randomDisplayTimer.setRepeats(true);
		randomDisplayTimer.start();
	}
	
	public void gameEnd() {
		JOptionPane.showMessageDialog(this, "Times Up!!!!!!!!!!!","Game Over",JOptionPane.INFORMATION_MESSAGE);
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void closeWindow(){
		
		if(timer!= null && timer.isRunning())
			timer.stop();
		if(randomDisplayTimer!= null && randomDisplayTimer.isRunning())
			randomDisplayTimer.stop();
		
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
//		panel.updateLabel(e.getX(), e.getY());
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		timerLabel = null;
		if(timer!= null && timer.isRunning())
			timer.stop();
		if(randomDisplayTimer!= null && randomDisplayTimer.isRunning())
			randomDisplayTimer.stop();
		panel.removeAll();
		System.out.println("log: Play area window closing");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("log: Play area window closed");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("log: Play area window deactivated");
	}
}
