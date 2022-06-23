package simulator.model;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

	@Override
	public int compare(Event a, Event b) {
		//-1 si a < b
		//0 si a == b
		//1 si a > b
		
		int resul=0;
		
		if(a.getTime()<b.getTime()) {
			resul=-1;
		}
		else if(a.getTime()==b.getTime()) {
			resul=0;
		}
		else {
			resul=1;
		}
		
		
		return resul;
	}
}
