package simulator.model;

public class InterCityRoad  extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reduceTotalContamination() {
		// TODO Auto-generated method stub
		int tc = 0;
		 if(weather==Weather.STORM) {tc= 20;}
		else if(weather==Weather.SUNNY) {tc=2;}
		else if(weather==Weather.CLOUDY) {tc= 3;}
		else if(weather==Weather.RAINY) {tc = 10;	}
		else if(weather==Weather.WINDY) {tc = 15;}
		
		contaminacionTot = (int) (((100.0-tc)/100.0)*contaminacionTot);
	}

	@Override
	public void updateSpeedLimit() {
		// TODO Auto-generated method stub
		if(contaminacionTot>contaminacion) {
			limiteActVel=getMaxSpeed()/2;
		}else {
			limiteActVel =getMaxSpeed();
		}
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		// TODO Auto-generated method stub
		int vel=getSpeedLimit();
		if(weather==getWeather().STORM) {
			vel=(getSpeedLimit()*8)/10;
		}
		return vel;
	}

}
