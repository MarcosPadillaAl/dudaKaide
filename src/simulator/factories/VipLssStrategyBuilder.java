package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.VipLssStrategy;

public class VipLssStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public VipLssStrategyBuilder() {
		super("vip_lss");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		int timeSlot;
		if(data.has("timeslot"))
			 timeSlot=data.getInt("timeslot");
		else
		 timeSlot=1;
		String string =data.has("viptag") ? data.getString("viptag"):"vip";
		return new VipLssStrategy(timeSlot,string);
	}

}