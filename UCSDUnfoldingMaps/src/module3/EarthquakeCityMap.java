package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Rick Stovall
 * Date: Jan. 3, 2017
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = true;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(1350, 1000, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 1100, 900, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 1100, 900, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    /* These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    */
	   
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    
	    //TODO: Add code here as appropriate
	    // load the markers list.
	    for (PointFeature p: earthquakes) {
	    	//TODO: populate markers here
	    	markers.add(createMarker(p));
	    }
	    System.out.print("Loaded "); 
	    System.out.print(markers.size());
	    System.out.println(" markers."); 
	    
	    //TODO: load markers to the map.
	    for (Marker m: markers) {
	    	map.addMarker(m); 
    	};

	
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	private SimplePointMarker createMarker(PointFeature feature)
	{
		SimplePointMarker s = new SimplePointMarker(feature.getLocation(),feature.getProperties());
		// Get the magnitude.
		Object magObj = s.getProperty("magnitude");
		int mag = (int) Float.parseFloat(magObj.toString());
		// set the radius
		s.setRadius(8+(mag*2) );
		// set the color
		int val = (mag < 9) ? mag*31 : 255;
		int col = color(val,255-val,0);
		s.setColor(col);
		return s;
	}
	
	public void draw() {
		background(color(10,10,100));
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	private void addKey() 
	{	
		// legand canvas
		fill(color(240,240,240));
		rect(25,40,150,420,20);
		// fill out the legand
		for (int i=0; i< 11; i++) {
			// get vertical offset
			int vofst = 100+i*30;
			// Text legand
			String s = "Mag "+ String.valueOf(i);
			fill(10);
			text(s, 60, vofst);
			// -- draw the illustration
			// set the radius
			int radius = 8+(i*2);
			// set the color
			int val = (i < 9) ? i*31 : 255;
			int col = color(val,255-val,0);
			fill(col);
			ellipseMode(CENTER);
			ellipse(130,vofst,radius,radius);
		}
	}
}
