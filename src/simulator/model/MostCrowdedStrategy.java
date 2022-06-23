package simulator.model;

import java.util.Iterator;
import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	private int timeSlot;
	
	 public MostCrowdedStrategy(int time) {
		// TODO Auto-generated constructor stub
		this.timeSlot=time;
	}
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		// TODO Auto-generated method stub

		if(roads.isEmpty()){
			return -1;
		}
		else if(currGreen==-1){
			int tamCola = 0;
			int pos = 0;
			
			for (int i=0; i<qs.size(); i++){
				if(tamCola<qs.get(i).size()){
					tamCola=qs.get(i).size();
					pos = i;
				}
			}
			return pos;
		}
		else if (currTime - lastSwitchingTime < timeSlot){
			return currGreen;
		}
		else{
			int tamCola=0, pos=0, busco=(currGreen + 1)%qs.size();
			for (int i=0;i<qs.size();i++){
				if(tamCola<qs.get(busco).size()){
					tamCola=qs.get(busco).size();
					pos=busco;
				}
				busco++;
				if(busco==qs.size()){
					busco=0;
				}
			}
			return pos;
		}
	}

}
