package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JComponent  implements TrafficSimObserver{
	private Controller ctrl;
	private RoadMap map;
	private Image car;
	private Image imagenContaminacion;
	private Image coche;
	private Image imagenTiempo;
	
	private static final int RADIO = 10;
	
	private static final Color JUNCTION_COLOR = Color.BLUE;
	private static final Color COLORFONDO = Color.WHITE;
	private static final Color GREENCOLOR = Color.GREEN;
	private static final Color ROAD_COLOR = Color.BLACK;
	private static final Color REDCOLOR = Color.RED;
	
	private static final Color COLOR_CRUCE = Color.ORANGE;
	
	public MapByRoadComponent(Controller ctrl) {
		// TODO Auto-generated constructor stub
		this.ctrl=ctrl;
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		// TODO Auto-generated method stub
		car = loadImage("car.png");
		setPreferredSize(new Dimension(300, 200));
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {}
		return i;
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(COLORFONDO);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		int i = 0;
		for (Road r : map.getRoads()) {

			int x1 = 50;
			int x2 = getWidth()-100;
			int y = (i + 1)*50;
			//dibujamos la linea y el id
			g.setColor(ROAD_COLOR);
			g.drawLine(x1, y, x2, y);
			g.drawString(r.getId(), x1-40,y+4);
			//dibuja el circulo desde x1 hasta y
			int circulox = x1;
			int circuloy = y;
			g.setColor(JUNCTION_COLOR);//color del iniciio del cruce
			g.fillOval(circulox -RADIO/2,circuloy - RADIO/2,RADIO,RADIO);
			g.setColor(COLOR_CRUCE);
			g.drawString(r.getSrc().getId(), circulox - 3, circuloy - 10);
			
			//el otro circulo del final
			circulox = x2;
			circuloy = y;
				Color destColor;
			int indiceSemadoro = r.getDest().getGreenLightIndex();
			if (indiceSemadoro != -1 && r.equals(r.getDest().getInRoads().get(indiceSemadoro)))
				destColor = GREENCOLOR;
			else
				destColor = REDCOLOR;
			g.setColor(destColor);//color bola final
			g.fillOval(circulox - RADIO/2,circuloy - RADIO/2,RADIO,RADIO);
			
			g.setColor(COLOR_CRUCE);//color del string del cruce
			g.drawString(r.getDest().getId(),circulox - 3, circuloy - 10);
			
			//coches de cada carretera
			if (!r.getVehicles().isEmpty()) {
				for(Vehicle v : r.getVehicles()) {
					if(v.getStatus() != VehicleStatus.ARRIVED) {
						int B = r.getLength();
						int A = v.getLocation();
						int coordenadaY = y;
						int coordenadaX = x1+(int)((x2-x1)*((double)A/(double)B));
						g.drawImage(car, coordenadaX, coordenadaY - 8, 16, 16, this);
						int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
						g.setColor(GREENCOLOR);
						g.drawString(v.getId(), coordenadaX, coordenadaY - 10);
					}
				}
			}
			//imagen de tiempo 
			if(r.getWeather().toString()=="SUNNY") {
				imagenTiempo = loadImage("sun.png");}
			else if(r.getWeather().toString() =="CLOUDY") {
				imagenTiempo = loadImage("cloud.png");}
			else if(r.getWeather().toString()=="RAINY") {
				imagenTiempo = loadImage("rain.png");}
			else if(r.getWeather().toString()=="STORM") {
				imagenTiempo = loadImage("storm.png");}
			else if( r.getWeather().toString()=="WINDY") {
					imagenTiempo = loadImage("wind.png");}
			
			g.drawImage(imagenTiempo, x2 + 10, y - 16, 32, 32, this);
			//imagen de contaminacion
			int A = r.getTotalCO2();
			int B = r.getContLimit();
			int C = (int) Math.floor(Math.min((double) A/(1.0 + (double) B),1.0) / 0.19);
			if(C==0)
				imagenContaminacion = loadImage("cont_0.png");
			else if(C==1)
				imagenContaminacion = loadImage("cont_1.png");
			else if(C==2)
				imagenContaminacion = loadImage("cont_2.png");
			else if(C==3)
				imagenContaminacion = loadImage("cont_3.png");
			else if(C==4)
				imagenContaminacion = loadImage("cont_4.png");
			else if(C==5)
				imagenContaminacion = loadImage("cont_5.png");
			else if(C==6)
				imagenContaminacion = loadImage("cont_6.png");
			g.drawImage(imagenContaminacion, x2 + 50, y - 16, 32, 32, this);

			i++;
				
		}
	}
	

	private void updatePrefferedSize() {
		int maxW = getWidth();
		int maxH = getHeight();
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	///////////////////////
	public void update(RoadMap map) {
		this.map = map;
		repaint();
	}

}
