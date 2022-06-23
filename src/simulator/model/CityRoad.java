package simulator.model;
///COSASA A HACER
//VER QUE PASA CON LOS TEST DE VEHICLE
//ENTENDER EL MOSTCROWDED
//PASAR LOS TEST
//ENTENDER EL ROAD MAP Y PASAR TEST
//entender junction advance y report
//entender todos los evenetos
//entender junction a la perfeccion

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reduceTotalContamination() {
		// TODO Auto-generated method stub
		int x = 0;
		if(weather==Weather.WINDY || weather==Weather.STORM) {
			x = 10;
		}else if (weather == Weather.SUNNY || weather == Weather.CLOUDY || weather == Weather.RAINY){
			x = 2;
		}
		contaminacionTot-=x;
		if(contaminacionTot<0) {
			contaminacionTot=0;
		}
	}

	@Override
	public void updateSpeedLimit() {
		// TODO Auto-generated method stub
		//limiteActVel=getMaxSpeed();
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		// TODO Auto-generated method stub
		int veloc = 0;
		
		if (v.getStatus() == VehicleStatus.TRAVELING){
			int s = limiteActVel, f = v.getContClass();
			veloc = (int)(((11.0-f)/11.0)*s);
		}
		return veloc;
	}

}
