package simulator.model;

public class NewInterCityRoadEvent extends Event {

	private String id;
	private String srcJun;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;

	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) { 
		super(time);
		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
		
	}

	@Override
	void execute(RoadMap map) {
		InterCityRoad r = new InterCityRoad(this.id, map.getJuntion(this.srcJun), map.getJuntion(this.destJunc), this.maxSpeed, this.co2Limit, this.length, this.weather);
		map.addRoad(r);
	}
	public String toString() {
		return "New Inter City Road '" + id + "'";
	}
}
