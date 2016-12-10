package tools;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;


public class EnvironmentGenerator extends JFrame implements ComponentListener, FocusListener{

	private static final long serialVersionUID = 7729824747876208044L;
	
	private static int Height = 635;
	private static int Width = 570;
	private JPanel contentPane;
	private MapPainter painterPanel;
	private JPanel generalPane;
	private JScrollPane scrb;
	private ToggleState currentState;
	private static int squareSize = 20;
	private int X_DIMENSION = 80;
	private int Y_DIMENSION = 40;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnvironmentGenerator maze = new EnvironmentGenerator();
					maze.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EnvironmentGenerator() {
		setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		
		setType(Type.POPUP);
		setTitle("Custom maze");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 50, 650, 650);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		painterPanel = new MapPainter(this);
		
		painterPanel.setToolTipText("Your maze");
		painterPanel.addFocusListener(this);
		painterPanel.addComponentListener(this);
		painterPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		painterPanel.setLayout(new BorderLayout(0, 0));
		painterPanel.setBounds(100, 50, 650, 650);
		
		generalPane=new JPanel();
		generalPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		generalPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		generalPane.add(painterPanel);

		scrb = new JScrollPane(generalPane);
		scrb.setViewportBorder(null);
		contentPane.add(scrb, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setToolTipText("Controls");
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnReset = new JButton("Reset");
		buttonPanel.add(btnReset);
		
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// restart the maze
				painterPanel.initializeEmpty();
				painterPanel.repaint();
			}} );
		
		JButton btnPreferences = new JButton("Preferences");
		btnPreferences.setToolTipText("Select the way your maze is created");
		buttonPanel.add(btnPreferences);
		
		JButton btnSave = new JButton("Save");
		buttonPanel.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// SAVE MAP
				painterPanel.saveMap();
				
				
			}} );
		

		JButton btnCancel = new JButton("Cancel");
		buttonPanel.add(btnCancel);	
		btnCancel.setActionCommand("Cancel");
		buttonPanel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				setVisible(false);
				dispose();
			}
		});

		JPanel elementsPanel = new JPanel();
		contentPane.add(elementsPanel, BorderLayout.NORTH);
		elementsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JToggleButton tglbtnExit = new JToggleButton("Exit");
		tglbtnExit.setToolTipText("Place the exit");
		elementsPanel.add(tglbtnExit);
		
		JToggleButton tglbtnPath = new JToggleButton("Path");
		tglbtnPath.setToolTipText("Clear the path");
		elementsPanel.add(tglbtnPath);

		JToggleButton tglbtnWall = new JToggleButton(" Wall");
		tglbtnWall.setToolTipText("Place a wall");
		elementsPanel.add(tglbtnWall);
		
		tglbtnWall.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentState != ToggleState.WALL){
					currentState = ToggleState.WALL;
					tglbtnExit.setSelected(false);
					tglbtnPath.setSelected(false);
				} else {
					currentState = ToggleState.NONE;
					tglbtnWall.setSelected(false);
				}

			}
		});

		tglbtnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentState != ToggleState.EXIT){
					currentState = ToggleState.EXIT;
					tglbtnWall.setSelected(false);
					tglbtnPath.setSelected(false);
				} else {
					currentState = ToggleState.NONE;
					tglbtnExit.setSelected(false);
				}

			}
		});
		
		tglbtnPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentState != ToggleState.PATH){
					currentState = ToggleState.PATH;
					tglbtnExit.setSelected(false);
					tglbtnWall.setSelected(false);
				} else {
					currentState = ToggleState.NONE;
					tglbtnPath.setSelected(false);
				}

			}
		});
		
		updateSize();
		setVisible(true);
		pack();
	}

	public ToggleState getCurrentState() {
		return currentState;
	}

	@Override
	public void focusGained(FocusEvent arg0) {}

	public void updateSize(){
		Width = X_DIMENSION*squareSize + 100;
		
		if(Y_DIMENSION<=11)
			Height = Y_DIMENSION*squareSize + 130;		
		else Height = Y_DIMENSION*squareSize + 100;
		
		setPreferredSize(new Dimension(Width, Height));
		setMinimumSize(new Dimension(Width, Height));
		
		pack();
		repaint();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		painterPanel.requestFocusInWindow();
		
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		painterPanel.repaint();
		contentPane.repaint();
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		painterPanel.repaint();
		contentPane.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		painterPanel.repaint();
		contentPane.repaint();
	}
	
}
