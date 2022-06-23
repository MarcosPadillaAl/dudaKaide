package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	private int timeSlot;
	
	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
		this.timeSlot = 1;
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject json) {
		MostCrowdedStrategy crowdedstrategy;
		if (json!=null&&json.has("timeslot")){
			crowdedstrategy=new MostCrowdedStrategy(json.getInt("timeslot"));
		}
		else{
			crowdedstrategy=new MostCrowdedStrategy(1);
		}
		return crowdedstrategy;
	}
}
