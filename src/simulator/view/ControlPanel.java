package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private boolean _stopped;
	private int _ticks;
	private int tiempo =0;
	private Controller ctrl;
	
	private JButton stopButton;
	private JButton runButton;
	private JButton exitButton;
	private JButton changeWeatherButton;
	private JButton loadButton;
	private JButton historyContamination;
	
	private JButton co2ChangeButton;

	private JToolBar toolBar;
	private RoadMap roadMap;
	private JSpinner tickSpinner;
	
	public ControlPanel(Controller ctrl) {
		super();
		this.ctrl = ctrl;
		this._ticks = 10;
		this._stopped = true;
		initGUI();
		ctrl.addObserver(this);
		
	}
	private void initGUI() {
		
		toolBar = new JToolBar();
		
		//EXIT
		exitButton=new JButton();
		exitButton.setToolTipText("Exit");
		exitButton.setBounds(10,10,400,400);
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int n = JOptionPane.showOptionDialog(new JFrame(), "Are sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) 
					System.exit(0); 
			}
		});
		//LOAD
		loadButton=new JButton();
		loadButton.setToolTipText("load");
		loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//updateUI();
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser(); 
				int v =chooser.showOpenDialog(null);
				if (v==JFileChooser.APPROVE_OPTION){
					try {
						InputStream i ;
						i = new FileInputStream(chooser.getSelectedFile());
						ctrl.reset();
						
						ctrl.loadEvents(i);
					
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else System.out.println("load cancelled by user");
				
			}
		});
		//CAMBIO CO2
		co2ChangeButton=new JButton();
		co2ChangeButton.setToolTipText("Change CO2");
		co2ChangeButton.setBounds(10,10,400,400);
		co2ChangeButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		co2ChangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {//VER PORQUE NO CARGAN LOS VEHICULOS NI LEE LO SELECCIONADO
				// TODO Auto-generated method stub
				ChangeCO2ClassDialog co=new ChangeCO2ClassDialog(roadMap);
				if(co.getStatus()==1) {
					String vehicle= co.getVehicle();
					int co2class= co.getCO2Class();
					int t = co.getTicks();
					
					List<Pair<String,Integer>>l = new ArrayList<Pair<String,Integer>>();
					Pair<String, Integer>p = new Pair<String,Integer>(vehicle,co2class);
					l.add(p);

					SetContClassEvent e = new SetContClassEvent(tiempo + t, l);
					ctrl.addEvent(e);
				}
			}
			
		});
		
		//////////////////////////////////////////////////
		historyContamination=new JButton();
		historyContamination.setToolTipText("Roads Contamination History");
		historyContamination.setBounds(10,10,400,400);
		historyContamination.setIcon(new ImageIcon("resources/icons/pie-chart.png"));
		historyContamination.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HistoryContDialog ho=new HistoryContDialog(ctrl);
				ho.open();
			}
		}
				);
		
		
		///////////////////////////////////////////////////////

		//STOP
		stopButton = new JButton();
		stopButton.setToolTipText("Stop");
		stopButton.setBounds(10,10,400,400);
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_stopped = true;
			}
		});
		//TICKS
		SpinnerNumberModel n= new SpinnerNumberModel();
		n.setMaximum(10000);
		n.setMinimum(0);
		tickSpinner=new JSpinner(n);
		JLabel textoTicks=new JLabel("Ticks:");
		tickSpinner.setMaximumSize(new Dimension(150, (int) tickSpinner.getPreferredSize().getHeight()));
		
		//RUN
		runButton = new JButton();
		runButton.setToolTipText("Run");
		runButton.setBounds(10,10,400,400);
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				_stopped =false;
				_ticks = (int)tickSpinner.getValue();
				loadButton.setEnabled(false);
				co2ChangeButton.setEnabled(false);
				changeWeatherButton.setEnabled(false);
				runButton.setEnabled(false);
				run_sim(_ticks);
			}});
		//WEATHER
		changeWeatherButton=new JButton();
		changeWeatherButton.setToolTipText("Change Weather");
		changeWeatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		changeWeatherButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ChangeWeatherDialog w=new ChangeWeatherDialog(roadMap);
				if(w.getStatus() == 1) {
					int t =w.getTicks();
					String road =w.getRoad();
					Weather weather = w.getWeather();
					
					List<Pair<String,Weather>>l =new ArrayList<Pair<String, Weather>>();
					
					Pair<String, Weather>p =new Pair<String, Weather>(road, weather);
					l.add(p);
					SetWeatherEvent e=new SetWeatherEvent(tiempo+ t,l);
					ctrl.addEvent(e);
				}
			}});
		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.PAGE_START);
		this.add(toolBar);
		toolBar.add(loadButton);
		toolBar.addSeparator();
		toolBar.add(co2ChangeButton);
		toolBar.add(changeWeatherButton);
		toolBar.add(historyContamination);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(textoTicks);
		toolBar.add(tickSpinner);
		toolBar.add(new JSeparator());
		//toolBar.addSeparator(getPreferredSize());
		toolBar.add(exitButton);
		
		exitButton.setEnabled(true);
		historyContamination.setEnabled(true);
		co2ChangeButton.setEnabled(true);
		stopButton.setEnabled(true);
		loadButton.setEnabled(true);
		changeWeatherButton.setEnabled(true);
	}
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
			ctrl.run(1,null);
		} catch (Exception e) {
		//ver que se puede añadir aqui de errores
			
			loadButton.setEnabled(true);
			runButton.setEnabled(true);
			co2ChangeButton.setEnabled(true);
			changeWeatherButton.setEnabled(true);
			historyContamination.setEnabled(true);
			_stopped = true;

			return;
		}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
		});
			} else {
				_stopped = true;
				loadButton.setEnabled(true);
				runButton.setEnabled(true);
				co2ChangeButton.setEnabled(true);
				changeWeatherButton.setEnabled(true);
				historyContamination.setEnabled(true);
			}
	}
	private void stop() {
		_stopped = true;
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		//roadMap = map;
		tiempo = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		//roadMap = map;
		tiempo = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		//roadMap = map;
		tiempo = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		roadMap = map;
		tiempo = time;
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
