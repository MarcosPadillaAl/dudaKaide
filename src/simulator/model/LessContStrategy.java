package simulator.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class LessContStrategy implements DequeuingStrategy{

	private int limit;
	
	private Comparator<Vehicle>cmp=new Comparator<Vehicle>() {

		@Override
		public int compare(Vehicle arg0, Vehicle arg1) {
			// TODO Auto-generated method stub
			if(arg0.getContClass()<arg1.getContClass()) {
				return-1;
			}else if(arg0.getContClass()>arg1.getContClass()) {
				return 1;
			}
			else
				return 0;
		}
		};
	
	public LessContStrategy(int limit) {
		// TODO Auto-generated constructor stub
		if(limit>=1) {
			this.limit=limit;
		}else {
			throw new IllegalArgumentException("the value of limit must be postive");
		}
	}
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		// TODO Auto-generated method stub
		q.sort(cmp);
		List<Vehicle>vs=new ArrayList<Vehicle>();
		for(int i=0;i<q.size();i++) {
			if(vs.size()<limit)
				vs.add(q.get(i));
		}
		return vs;
	}

	
}
