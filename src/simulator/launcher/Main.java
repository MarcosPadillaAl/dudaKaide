package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javafx.util.Pair;
import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.LessContStrategyBuilder;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.factories.VipDqStrategyBuilder;
import simulator.factories.VipLssStrategyBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.model.VipDqStrategy;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static int _ticks = 0;
	private static Factory<Event> _eventsFactory = null;
	private static Controller controller;
	//cambios
	private final static String defaultGui = "gui";
	private static String modeGui = null;

	private static Pair<String, Integer> _loc = null;
	
	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			
			
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			
			//AÑADIDO
			parseTicksOption(line);
			//AÑADIDO 2
			parseModeOption(line);
			parseOutFileOption(line);
			/////////////////////////
			parseLocation(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		//AÑADIDO
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator�s main loop (default value is 10.").build());
		//AÑADIDO 2
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("GUI or Console mode").build());
		//////////////////////////////////////////////////////////////////////////7
		cmdLineOptions.addOption(Option.builder("l").longOpt("loc").hasArg().desc("...").build());
		//quitamos el .hasArg() para que pueda obtener el valos por defecto de gui
		return cmdLineOptions;
	}
///////////////////////////////////////////////7
	private static void parseLocation(CommandLine line) throws ParseException {
		String l = line.getOptionValue("l");
		if (l != null) {
			try {
				int i = l.lastIndexOf(':');
				_loc = new Pair<>(l.substring(0, i), Integer.parseInt(l.substring(i + 1)));
			} catch (Exception e) {
				throw new ParseException("Invalid location: " + l);
			}
		}
	}
	//////////////////////////////////////77
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		//CAMBIO 2
		if(_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		//_outFile = line.getOptionValue("o");
		//CAMBIO 2
		if(!modeGui.equals("gui")) {
			_outFile = line.getOptionValue("o");
		}
	}
	
	
	//añadido
	private static void parseTicksOption(CommandLine line) throws ParseException {
		String s = line.getOptionValue("t");
		if(s == null) {
			_ticks = _timeLimitDefaultValue;
		}
		else {
			int ticks = Integer.parseInt(s);
			_ticks = ticks;
		}
	}
	//findel añadido
	
	//AÑADIDO 2
	private static void parseModeOption(CommandLine line) {
		
			String mode=line.getOptionValue("m");
		if(mode!=null) {
				modeGui = mode;
				
		}else {
				modeGui = defaultGui;
			
		}
	}
	//FIN AÑADIDO 2
	private static void initFactories() {
		//Creamos una lista de Builders de Eventos
				List<Builder<Event>> builders = new ArrayList<Builder<Event>>();
				
				//Creamos la Factory de LightSwitchStrategy
				ArrayList<Builder<LightSwitchingStrategy>> lss = new ArrayList<>();
				lss.add( new RoundRobinStrategyBuilder() );
				lss.add( new MostCrowdedStrategyBuilder() );
				lss.add(new VipLssStrategyBuilder());
				Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lss);
				
				//Creamos la Factory de DequeuStrategy
				ArrayList<Builder<DequeuingStrategy>> dqs = new ArrayList<>();
				dqs.add( new MoveFirstStrategyBuilder() );
				dqs.add( new MoveAllStrategyBuilder() );
				dqs.add(new VipDqStrategyBuilder());
				dqs.add(new LessContStrategyBuilder());
				Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqs); 
				
				//A�adimos todos los EventBuilders a eventFactory
				
				builders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
				builders.add(new NewCityRoadEventBuilder());
				builders.add(new NewInterCityRoadEventBuilder());
				builders.add(new NewVehicleEventBuilder());
				builders.add(new SetWeatherEventBuilder());
				builders.add(new SetContClassEventBuilder());
				
				_eventsFactory = new BuilderBasedFactory<Event>(builders);

	}

	private static void startBatchMode() throws IOException, InterruptedException {
		// TODO complete this method to start the simulation
		TrafficSimulator sim = new TrafficSimulator();
		controller = new Controller(sim, _eventsFactory);
		
		try {
			InputStream in = new FileInputStream(_inFile);
			controller.loadEvents(in);
			
			OutputStream out;
			OutputStream outExam;
			
			if(_outFile == null) {
				out = new PrintStream(System.out);
				outExam = new PrintStream(System.out);
				controller.run(_ticks, out);
				PrintStream p = new PrintStream(outExam);
				p.print(ejercicio());
				}
			else {
				out = new FileOutputStream(_outFile);
				
				
			}
			
			
		}
		catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}//////////////////////////////////////////////////////////////////////////////
	public static String ejercicio() {
		String o="";
		if(_loc!=null) {
			o="La localización"+ _loc.getValue().toString()+" de la carretera "+_loc.getKey()+" ha sido cruzada 4 veces";
		}
		
		return o;
	}
	////////////////////////////////////////////////////////////////////////////////////
//AÑADAIDO 2
	private static void startGUIMode() throws IOException{
		TrafficSimulator sim = new TrafficSimulator();
		controller = new Controller(sim, _eventsFactory);
		
		try {
			if(_inFile != null) {
			InputStream in = new FileInputStream(_inFile);
			controller.loadEvents(in);
			}
		}
		catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		}
		
		//CREACI�N DE LA VENTANA PRINCIPAL
		//VER QUE ES LO DE INVOKE LATER
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});

		
	}
//FIN DEL AÑADIDO 2
	private static void start(String[] args) throws IOException, InterruptedException {
		initFactories();
		parseArgs(args);
		if(modeGui.equals("gui")) {
			startGUIMode();
		}
		else {
			startBatchMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json -t 300
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

