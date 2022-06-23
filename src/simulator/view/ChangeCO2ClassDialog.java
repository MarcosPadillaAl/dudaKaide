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

import javafx.scene.control.Spinner;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	private RoadMap roadMap;
	private JComboBox<Vehicle> vehicle;
	private JComboBox <Integer>co2Class;
	
	private JLabel descripcion;
	private JSpinner ticks;
	private DefaultComboBoxModel<Integer> co2ClassModel;
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	private int status; //0 si se pulsa cancel, 1 si se pulsa OK
	
	public ChangeCO2ClassDialog(RoadMap roadMap) {
		super(new Frame(), true);//VER QUE HACE ETO MEJOR
		this.roadMap = roadMap;
		initGUI();
	}
	private void initGUI() {
		this.status=0;
		
		this.setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));//forma que va a llevar el apnel
		setContentPane(mainPanel);//REVISAR ESTO/establecer el contenido que haya dentro de mainPanel
		//MENSAJE
		descripcion = new JLabel("Schedule an event to change the CO2 class of a vehicle after given number of simulation ticks from now");
		descripcion.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(descripcion);
		//DISTINTOS CAMPOS
		JPanel panelCampos = new JPanel();
		JPanel panelBotones = new JPanel();
		panelCampos.setAlignmentX(CENTER_ALIGNMENT);
		panelBotones.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(panelCampos);
		mainPanel.add(panelBotones);
		mainPanel.add(Box.createRigidArea(new Dimension(0,22)));//ver esto
		
		///campo
		///
	
		vehicleModel = new DefaultComboBoxModel<>();
		for(Vehicle v : roadMap.getVehicles()) {
			vehicleModel.addElement(v);
		}
		vehicle=new JComboBox<>(vehicleModel);
		vehicle.setPreferredSize(new Dimension(100, 25));
		JLabel textoVeh=new JLabel("Vehicle:");
		///
		co2ClassModel = new DefaultComboBoxModel<>();
		for(int i = 0; i < 11; i++) {
			co2ClassModel.addElement(i);
		}
		co2Class = new JComboBox<>(co2ClassModel);
		co2Class.setPreferredSize(new Dimension(100, 25));
		JLabel textoCO=new JLabel("CO2 Class:");
		///
		SpinnerNumberModel n= new SpinnerNumberModel();
		n.setMaximum(10000);
		n.setMinimum(0);
		ticks=new JSpinner(n);
		JLabel textoTicks=new JLabel("Ticks:");
		//Añadimos al panel
		panelCampos.add(textoVeh);
		panelCampos.add(vehicle);
		panelCampos.add(textoCO);
		panelCampos.add(co2Class);
		panelCampos.add(textoTicks);
		panelCampos.add(ticks);
		
		/////Botones
		JButton botonCancelar=new JButton("Cancel");
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		
		JButton botonOk=new JButton("Ok");
		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (vehicle.getSelectedItem()!=null && co2Class.getSelectedItem()!= null) {
					status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		panelBotones.add(botonCancelar);
		panelBotones.add(botonOk);
		setPreferredSize(new Dimension(650, 200));
		pack();
		setVisible(true);
	}
	public int getStatus() {
		return this.status;
	}
	
	public int getTicks() {
		return (int) ticks.getValue();
	}
	
	public String getVehicle() {
		return vehicle.getSelectedItem().toString();
	}
	
	public int getCO2Class() {
		return (int)co2Class.getSelectedItem();
	}
	
}
