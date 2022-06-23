package simulator.model;

import java.util.Comparator;

public class VehicleComparator implements Comparator<Vehicle>{

	@Override
	public int compare(Vehicle a, Vehicle b) {
		if(a.getLocation() < b.getLocation()) {
			return 1;
		}
		else if(a.getLocation() > b.getLocation()) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
