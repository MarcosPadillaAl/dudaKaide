package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.control.Controller;
import simulator.model.RoadMap;

public class MainWindow  extends JFrame {
	private Controller ctrl;
	private RoadMap map;
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		this.ctrl = ctrl;
		initGUI();
	}
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);//lo añadimos a la vista
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);//lo añadimos a la vista
		// tables
		//eventos
		JPanel eventsView =createViewPanel(new JTable(new EventsTableModel(ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);	
		
		//vehiculos
		JPanel vehiclesView=createViewPanel(new JTable(new VehicleTableModel(ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);	
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		//carreteras
		JPanel roadsView=createViewPanel(new JTable(new RoadTableModel(ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);	
		
		//cruces
		JPanel crucesView=createViewPanel(new JTable(new CrucesTableModel(ctrl)), "Junctions");
		crucesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(crucesView);
		
		// TODO add other tables
		// ...
		// maps//VISUAL DE LOS MAPAS
		
		JPanel mapView = createViewPanel(new MapComponent(ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		// TODO add a map for MapByRoadComponent
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(ctrl), "Map by Road");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);
		//
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}

}
