package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.VipDqStrategy;

public class VipDqStrategyBuilder extends Builder<DequeuingStrategy> {

	public VipDqStrategyBuilder( ) {
		super("vip_dqs");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		if(!data.has("viptag")) {
			throw new IllegalArgumentException("missing key");
		}
		String string=data.getString("viptag");
		int limit =data.has("limit") ? data.getInt("limit") :1;
		return new VipDqStrategy(string, limit);
	}
	

}
