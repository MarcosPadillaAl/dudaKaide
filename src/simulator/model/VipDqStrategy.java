package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class VipDqStrategy implements DequeuingStrategy {

	private String string;
	private int limit;
	public VipDqStrategy(String string, int limit) {
		// TODO Auto-generated constructor stub
		if( string==null) {
			throw new IllegalArgumentException("tag cannot be null");
		}
		if(limit<1) {
			throw new IllegalArgumentException("limit cant be negative");
		}
		this.string=string;
		this.limit=limit;
	}
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		// TODO Auto-generated method stub
		List<Vehicle>vs =new ArrayList<>();
		ArrayList<Vehicle>array=new ArrayList<>();
		array=	filter(q);
		//filter(q,vs,(v)->v.getId().endsWith(string));
		for(int j=0;j<array.size();j++) {
			vs.add(array.get(j));
			q.remove(array.get(j));
		}
		for(int i=0;i<q.size();i++) {
			if(vs.size()<limit)
			vs.add(q.get(i));
		}
		return vs;
	}
	private ArrayList<Vehicle> filter(List<Vehicle> q) {
		ArrayList<Vehicle>array=new ArrayList<>();
		for(int i=0;i<q.size();i++) {
			if(q.get(i).getId().endsWith(string)) {
				array.add(q.get(i));
			}
		}
		return array;
	}
}
