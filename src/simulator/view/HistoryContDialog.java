package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

import javafx.util.Pair;
import jdk.nashorn.internal.scripts.JD;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class HistoryContDialog extends JDialog implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Pair<Integer,List<Pair<String,Integer>>>>_history;
	private String[] columnasTabla = {"Tick", "Roads"};
	private Controller ctrl;
	private JLabel descripcion;
	private JSpinner ticks;
	
	private HistoryTableModel _histroryTableModel;
	/////////////////////////////////////////////////////
			class HistoryTableModel extends AbstractTableModel{
		
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public int getColumnCount() {
					return columnasTabla.length;
				}
				public String getColumnName(int col) {
					return columnasTabla[col];
				}
				@Override
				public int getRowCount() {
					// TODO Auto-generated method stub
					return _history.size();
				}
		
				@Override
				public String getValueAt(int rowIndex, int columnIndex) {
					Pair<Integer,List<Pair<String,Integer>>>e= _history.get(rowIndex);
					String v = "";
					if(columnIndex==0) {
						v=e.getKey()+"";
					}else if(columnIndex==1) {
						v=filter(e.getValue());
					}
					return v;
				}
				public String filter(List<Pair<String,Integer>> l) {
					List<String> roads=new ArrayList<>();
					int c=(int) ticks.getValue();
					for(Pair<String,Integer>e:l) {
						roads.add(e.getKey());
					}
					return roads.toString();
				}
				public void update() {
					fireTableDataChanged();
				}
			}
	/////////////////////////////////////////////////////////
	public  HistoryContDialog(Controller ctrl) {
		super(new Frame(), true);//VER QUE HACE ETO MEJOR
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}
	private void initGUI() {
		_history=new ArrayList<>();
		_histroryTableModel= new HistoryTableModel();

		this.setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));//forma que va a llevar el apnel
		setContentPane(mainPanel);//REVISAR ESTO/establecer el contenido que haya dentro de mainPanel
		//MENSAJE
		descripcion = new JLabel("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		descripcion.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(descripcion);
		//DISTINTOS CAMPOS
		JPanel panelCampos = new JPanel();
		JPanel panelBotones = new JPanel();
		panelCampos.setAlignmentX(CENTER_ALIGNMENT);
		panelBotones.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(panelBotones);
		mainPanel.add(panelCampos);

		mainPanel.add(Box.createRigidArea(new Dimension(0,22)));//ver esto
		//////////
		SpinnerNumberModel n= new SpinnerNumberModel();
	//	ticks.setAlignmentX(CENTER_ALIGNMENT);
		n.setMaximum(10000);
		n.setMinimum(0);
		ticks=new JSpinner(n);
		JLabel textoTicks=new JLabel("Contamination Limit:");
		panelCampos.add(textoTicks);
		panelCampos.add(ticks);
	///////////	
		JButton botonCancelar=new JButton("Cancel");
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				HistoryContDialog.this.setVisible(false);
			}
		});
		
		JButton botonOk=new JButton("Update");
		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					_histroryTableModel.update();
				
			}
		});
		panelBotones.add(botonCancelar);
		panelBotones.add(botonOk);
		panelCampos.add(new JScrollPane(new JTable(_histroryTableModel)));
		setPreferredSize(new Dimension(650, 500));
		pack();
		setVisible(false);
	}
	public void open() {
		_histroryTableModel.update();
		setVisible(true);
	}
	public void updateHist(RoadMap map,int time) {
		List<Pair<String,Integer>>l=new ArrayList<>();
		for(Road r: map.getRoads()) {
			l.add(new Pair<>(r.getId(),r.getTotalCO2()));
		}
		_history.add(new Pair<>(time,l));
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		updateHist(map, time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
