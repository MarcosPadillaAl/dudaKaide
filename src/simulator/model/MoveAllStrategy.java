package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		//Creamos una lista de igual tama�o
			//List<Vehicle> l = new ArrayList<Vehicle>(q.size());
				//Copiamos la lista q a l, porque luego cuando eliminemos los coches de q no queremos que se eliminen de la lista original
				//Collections.copy(l, q);
				List<Vehicle> v=new ArrayList<Vehicle>(q);
				
				/* M�todo alternativo - hace lo mismo
				List<Vehicle> v = new ArrayList<Vehicle>(q);
				*/
				
				return v;
	}


}
