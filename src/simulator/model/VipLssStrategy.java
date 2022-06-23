package simulator.model;

import java.util.List;

public class VipLssStrategy implements LightSwitchingStrategy{

	private String string;
	private int timeSlot;
	private RoundRobinStrategy rrs;
	public VipLssStrategy(int timeSlot, String string) {
		// TODO Auto-generated constructor stub
		if( string==null) {
			throw new IllegalArgumentException("tag cannot be null");
		}
		if(timeSlot<1) {
			throw new IllegalArgumentException("limit cant be negative");
		}
		this.string=string;
		this.timeSlot=timeSlot;
		 rrs = new RoundRobinStrategy(timeSlot);
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		// TODO Auto-generated method stub
		int sol=0;
		if(roads.size()==0)
			sol=-1;
		else {
			if(currGreen==-1) {
				sol=busqueda(qs,0);
			}else
				sol=busqueda(qs,currGreen);
			if(sol==-1) {
				sol=rrs.chooseNextGreen(roads, qs, currGreen, lastSwitchingTime, currTime);
			}
		}
		return sol;
	}
	public int busqueda(List<List<Vehicle>> qs,int pos) {
		while(pos<qs.size()) {
			for(int i=0;i<qs.get(pos).size();i++) {
				if(qs.get(pos).get(i).getId().endsWith(string))
					return pos;
			}
			pos++;
		}
		pos=-1;
		return pos;
	}
}
