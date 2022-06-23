package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{
	
	private RoadMap roadMap;
	private int status;
	private JLabel descripcion;
	private JComboBox road;
	private JComboBox weather;
	private DefaultComboBoxModel<String> roadModel;
	private DefaultComboBoxModel<Weather> weatherModel;
	private JSpinner ticks;
	
	public ChangeWeatherDialog(RoadMap roadMap) {
		super(new Frame(), true);
		this.roadMap = roadMap;
		initGUI();
		
	}
	private void initGUI() {
		this.status=0;
		
		this.setTitle("Change Weather Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));//forma que va a llevar el apnel
		setContentPane(mainPanel);//REVISAR ESTO, creo que es establecer el contenido que haya dentro de mainPanel
		//MENSAJE
		descripcion = new JLabel("Schedule an event to change the weather of a road after a fiven number of simulation ticks from now");
		descripcion.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(descripcion);
		//DISTINTOS CAMPOS
		JPanel panelCampos = new JPanel();
		JPanel panelBotones = new JPanel();
		panelCampos.setAlignmentX(CENTER_ALIGNMENT);
		panelBotones.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(panelCampos);
		mainPanel.add(panelBotones);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));//ver esto
		///campo
		///
		roadModel = new DefaultComboBoxModel<>();
		for(Road r : roadMap.getRoads()) {
			roadModel.addElement(r.getId());
		}
		road=new JComboBox<>(roadModel);
		road.setPreferredSize(new Dimension(100, 25));
		JLabel textoR=new JLabel("Road:");
		///
		weatherModel = new DefaultComboBoxModel<>();
		for(Weather w : Weather.values()) {
			weatherModel.addElement(w);
		}
		weather = new JComboBox<>(weatherModel);
		weather.setPreferredSize(new Dimension(100, 25));
		JLabel textoCO=new JLabel("Weather:");
		///
		SpinnerNumberModel n= new SpinnerNumberModel();
		n.setMaximum(10000);
		n.setMinimum(0);
		ticks=new JSpinner(n);
		JLabel textoTicks=new JLabel("Ticks:");
		//Añadimos al panel
		panelCampos.add(textoR);
		panelCampos.add(road);
		panelCampos.add(textoCO);
		panelCampos.add(weather);
		panelCampos.add(textoTicks);
		panelCampos.add(ticks);
		
		/////Botones
		JButton botonCancelar=new JButton("Cancel");
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
			
		});
		
		JButton botonOk=new JButton("Ok");
		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (road.getSelectedItem() != null && weather.getSelectedItem() != null) {
					status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
			
		});
		panelBotones.add(botonCancelar);
		panelBotones.add(botonOk);
		setPreferredSize(new Dimension(700, 200));
		pack();
		setVisible(true);
	}
	public int getTicks() {
		return (int) ticks.getValue();
	}
	
	public String getRoad() {
		return  road.getSelectedItem().toString();
	}
	
	public Weather getWeather() {
		return Weather.valueOf(weather.getSelectedItem().toString());
	}

	public int getStatus() {
		return this.status;
	}
}
