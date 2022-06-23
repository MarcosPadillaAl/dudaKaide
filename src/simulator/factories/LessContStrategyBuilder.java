package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.LessContStrategy;

public class LessContStrategyBuilder extends Builder<DequeuingStrategy>{

	public	LessContStrategyBuilder() {
			super("less_cont_dqs");
			// TODO Auto-generated constructor stub
		}
	
	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		int limit=data.has("limit") ? data.getInt("limit"):1;
		
		return new LessContStrategy(limit);
		
	}

}
