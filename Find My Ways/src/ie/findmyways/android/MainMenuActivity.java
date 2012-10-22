package ie.findmyways.android;

import ie.findmyways.android.R;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenuActivity extends AbstractAsyncActivity {

	protected static final String TAG = MainMenuActivity.class.getSimpleName();

	private ConnectionRepository connectionRepository;

	private FacebookConnectionFactory connectionFactory;

	// ***************************************
	// Activity methods
	// ***************************************
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_activity_layout);
		this.connectionRepository = getApplicationContext().getConnectionRepository();
		this.connectionFactory = getApplicationContext().getFacebookConnectionFactory();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isConnected()) {
			showFacebookOptions();
		} else {
			showConnectOption();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// ***************************************
	// Private methods
	// ***************************************
	private boolean isConnected() {
		return connectionRepository.findPrimaryConnection(Facebook.class) != null;
	}

	private void disconnect() {
		this.connectionRepository.removeConnections(this.connectionFactory.getProviderId());
	}

	private void showConnectOption() {
		String[] options = { "Connect" };
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.main_menu_activity_options_list);
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				switch (position) {
				case 0:
					displayFacebookAuthorization();
					break;
				default:
					break;
				}
			}
		});
	}

	private void showFacebookOptions() {
		String[] options = { "Disconnect", "Browse Hikes", "Facebook Home (left as an example)"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.main_menu_activity_options_list);
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				Intent intent;
				switch (position) {
				case 0:
					disconnect();
					showConnectOption();
					break;
				case 1:
					intent = new Intent();
					intent.setClass(parentView.getContext(), BrowseHikesActivity.class);
					startActivity(intent);
					break;	
				case 2:
					intent = new Intent();
					intent.setClass(parentView.getContext(), FacebookHomeFeedActivity.class);
					startActivity(intent);
					break;	
				default:
					break;
				}
			}
		});
	}

	private void displayFacebookAuthorization() {
		Intent intent = new Intent();
		intent.setClass(this, FacebookWebOAuthActivity.class);
		startActivity(intent);
		finish();
	}

}
