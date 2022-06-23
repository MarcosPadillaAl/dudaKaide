package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	
	private String messageBar;
	private Controller ctrl;
	private JLabel time;
	private JLabel message;
	
	private int currentTime;
	public StatusBar(Controller _ctrl) {
		currentTime = 0;
		ctrl = _ctrl;
		messageBar = "Welcome!";
		
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(BorderFactory.createEmptyBorder());
		
		time =new JLabel("Time: " + currentTime);
		message = new JLabel(messageBar);
		time.setPreferredSize(new Dimension(150, 25));
		add(time);
		JSeparator s =new JSeparator(SwingConstants.VERTICAL);
		s.setPreferredSize(new Dimension(2, 25));
		
		add(s);
		add(message);
		setVisible(true);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.time.setText("Time: " + time);
		message.setText("");
		repaint();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.time.setText("Time: " + time);
		message.setText("");
		repaint();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this.time.setText("Time: " + time);
		message.setText("Event added (" + e.toString() + ")");
		repaint();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.time.setText("Time: " + 0);
		message.setText("Welcome!");
		repaint();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.time.setText("Time: " + 0);
		message.setText("Welcome!");
		repaint();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub

	}

}
