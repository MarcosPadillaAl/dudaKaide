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
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehicleTableModel extends AbstractTableModel implements TrafficSimObserver {
	private Controller _ctrl;
	private List<Vehicle> vehicleList;
	private String[] columnasTabla = {"Id", "Location","Itinerary","CO2 Class","Max.Speed","Speed","Total CO2","Distance" };
	
	public VehicleTableModel(Controller _ctrl) {
		this._ctrl = _ctrl;
		_ctrl.addObserver(this);
		}
	public void update() {
		fireTableDataChanged();
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
		if(vehicleList==null)
			return 0;
		else
		// TODO Auto-generated method stub
		return vehicleList.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		Object v = null;
		if(columna==0) {
			v = vehicleList.get(fila).getId();
		}else if(columna==1) {
			if(vehicleList.get(fila).getStatus()==VehicleStatus.ARRIVED){
				v ="ARRIVED";
			}
			else {
				v= vehicleList.get(fila).getRoad().getId() +":"+ vehicleList.get(fila).getLocation();
			}
		}else if(columna==2) {
			List<Junction> jList = vehicleList.get(fila).getItinerary();
			v = "[";
			for(Junction j : jList) {
				if(!j.getId().equals(jList.get(jList.size() - 1).getId())) {
					v += j.getId() + ", ";
				}
			}
			v += "]";
		}else if(columna==3) {
			v = vehicleList.get(fila).getContClass();
		}else if(columna==4) {
			v = vehicleList.get(fila).getMaxSpeed();
		}else if(columna==5) {
			v = vehicleList.get(fila).getSpeed();
		}else if(columna==6) {
			v = vehicleList.get(fila).getTotalCO2();
		}else if(columna==7) {
			v = vehicleList.get(fila).getLocation();
		}
		return v;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		//vehicleList=map.getVehicles();
		//update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		vehicleList=map.getVehicles();
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
	
}
