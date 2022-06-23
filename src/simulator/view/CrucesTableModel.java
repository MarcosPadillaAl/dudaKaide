package simulator.view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class CrucesTableModel extends AbstractTableModel implements TrafficSimObserver  {

	private Controller ctrl;
	private List<Junction> crucesList;
	private String[] colNames = {"Id","Green","Queues"};
	public  CrucesTableModel(Controller ctrl) {
		// TODO Auto-generated constructor stub
		ctrl=ctrl;
		ctrl.addObserver(this);
	}
	public String getColumnName(int col) {
		return colNames[col];
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		if(crucesList==null)
			return 0;
			else
		return crucesList.size();
	}

	@Override
	public Object getValueAt(int filas, int columnas) {
		// TODO Auto-generated method stub
		Object c = null;
		if(columnas==0) {
			c = crucesList.get(filas).getId();
		}
		else if(columnas==1) {
			c = crucesList.get(filas).getGreenLightIndexRoads();}
		else if(columnas==2) {
			c = "";
			if(!crucesList.get(filas).getInRoads().equals(null)) {
				c = crucesList.get(filas).getInRoads();
			}
		}
		
		return c;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		crucesList=map.getJunctions();
		update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		crucesList=map.getJunctions();
		update();
	}
	public void update() {
		fireTableDataChanged();
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

	