package ie.findmyways.android;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class BrowseHikesActivity extends MapActivity{
	private LocationManager locationManager;
	private String provider;
	private String latitude;
	private String longitude;
	private MapView mapView;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_hikes_activity_layout);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		HikesItemizedOverlay itemizedoverlay = new HikesItemizedOverlay(
				drawable, this);
		GeoPoint point = new GeoPoint(53306160, -6221622);
		OverlayItem overlayitem = new OverlayItem(point, "UCD Trail",
				"Here is an available trail in the UCD campus");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage(R.string.turn_gps_on);
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("yes", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				      startActivity(intent);
				}
			});
			alt_bld.setNegativeButton("No", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					dialog.cancel();
				}
			});
			alt_bld.show();
		}
		
		// Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    // Initialize the location fields
	    if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      point = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
	      OverlayItem current = new OverlayItem(point, "My Current Location", "");
	      itemizedoverlay.addOverlay(current);
	    } else {
	      latitude="Location not available";
	      longitude="Location not available";
	    }
	    mapOverlays.add(itemizedoverlay);
	  //get the MapController object
        MapController controller = mapView.getController();

        //animate to the desired point
        controller.animateTo(point);

        //set the map zoom to 13
        // zoom 1 is top world view
        controller.setZoom(13);

        //invalidate the map in order to show changes
        mapView.invalidate();
	}

}
