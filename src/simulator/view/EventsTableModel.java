package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Event> events;
	private String[] colNames = {"Time", "Desc." };
	
	public EventsTableModel() {
		events=null;
	}
	
	public EventsTableModel(Controller _ctrl) {
		this.ctrl = _ctrl;
		_ctrl.addObserver(this);
	}
	public void update() {
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return  colNames.length;
	}
	@Override//PREGUMTAR PORQUE CON EL OVERRIDE FUNCIONA
	public String getColumnName(int col) {
		return colNames[col];
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		if(events==null)
		return 0;
		else return events.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {//ENTENDER ESXTE
		// TODO Auto-generated method stub
		Object e = null;
		if(columna==0) {
			e = events.get(fila).getTime();
		}else if(columna==1) {
			e = events.get(fila).toString();
		}
		return e;
	}
	public void setEventsList(List<Event> events) {
		this.events = events;
		update();
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.events=events;
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this.events=events;
		update();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
}
