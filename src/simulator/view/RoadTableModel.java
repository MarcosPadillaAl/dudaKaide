package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadTableModel extends AbstractTableModel implements TrafficSimObserver{

	Controller ctrl;
	List<Road>roadList;
	private String[] columnasTabla = {"Id","Lenght","Weather","Max.Speed","Speed","Total CO2","CO2 Limit" };
	public RoadTableModel(Controller ctrl) {
		// TODO Auto-generated constructor stub
		this.ctrl = ctrl;
		ctrl.addObserver(this);
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnasTabla.length;
	}
	public String getColumnName(int col) {
		return columnasTabla[col];
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		if(roadList==null)return 0;
		else
		return roadList.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		// TODO Auto-generated method stub
		Object r = null;
		if(columna==0) {
			r=roadList.get(fila).getId();
		}else if(columna==1) {
			r=roadList.get(fila).getLength();
		}else if(columna==2) {
			r=roadList.get(fila).getWeather();
		}else if(columna==3) {
			r=roadList.get(fila).getMaxSpeed();
		}else if(columna==4) {
			r=roadList.get(fila).getSpeedLimit();
		}else if(columna==5) {
			r=roadList.get(fila).getTotalCO2();
		}else if(columna==6) {
			r=roadList.get(fila).getContLimit();
		}
		
		return r;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		roadList=map.getRoads();
		update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		roadList=map.getRoads();
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
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
	public void update() {
		fireTableDataChanged();
	}

}
