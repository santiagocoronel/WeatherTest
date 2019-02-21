package com.zencom.weathertest.ui;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zencom.weathertest.BuildConfig;
import com.zencom.weathertest.R;
import com.zencom.weathertest.api.OpenWeatherMapApi;
import com.zencom.weathertest.api.ServiceGenerator;
import com.zencom.weathertest.data.shared.PreferencesManager;
import com.zencom.weathertest.pojo.City;
import com.zencom.weathertest.pojo.ItemWeather;
import com.zencom.weathertest.pojo.Session;
import com.zencom.weathertest.pojo.Weather;
import com.zencom.weathertest.pojo.WeatherForecast;
import com.zencom.weathertest.pojo.WeatherLocation;
import com.zencom.weathertest.ui.base.BaseActivity;
import com.zencom.weathertest.ui.base.InitBasicMethods;
import com.zencom.weathertest.ui.config.ConfigFragment;
import com.zencom.weathertest.ui.weather.WeatherFragment;
import com.zencom.weathertest.utils.Cons;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zencom.weathertest.utils.Cons.CORDOBA;

public class MainActivity extends BaseActivity implements InitBasicMethods, ConfigFragment.ConfigFragmentInterface {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initListeners();
    }

    @Override
    public void init() {
        initSession();
        replaceFragment(WeatherFragment.getInstance(), R.id.container, false);
        obtenerPronostico(PreferencesManager.getInstance(this).getSession().getCity());
        obtenerPronosticoFuturo(PreferencesManager.getInstance(this).getSession().getCity());
    }

    private void initSession() {
        if (PreferencesManager.getInstance(this).getSession() == null) {
            Session session = new Session();
            session.setCity(new City(Cons.CORDOBA_NAME, CORDOBA[0], CORDOBA[1]));
            PreferencesManager.getInstance(this).saveSession(session);
        }
    }

    @Override
    public void initListeners() {

        navigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_weather:
                    replaceFragment(WeatherFragment.getInstance(), R.id.container, false);
                    return true;
                case R.id.navigation_config:
                    replaceFragment(ConfigFragment.getInstance(this), R.id.container, false);
                    return true;
            }
            return false;
        });

    }


    private void obtenerPronostico(City city) {
        showProgressBar(getResources().getString(R.string.waiting), false);
        ServiceGenerator.createService(OpenWeatherMapApi.class)
                .getDataWeather(city.getLat(), city.getLng(), BuildConfig.API_KEY)
                .enqueue(new Callback<WeatherLocation>() {
                    @Override
                    public void onResponse(Call<WeatherLocation> call, Response<WeatherLocation> response) {
                        dismissProgressBar();
                        if (response.isSuccessful()) {

                            if (WeatherFragment.getInstance().isVisible()) {
                                Session session = PreferencesManager.getInstance(MainActivity.this).getSession();
                                session.setWeatherLocation(response.body());
                                PreferencesManager.getInstance(MainActivity.this).saveSession(session);
                                WeatherFragment.getInstance().updateUI();
                            }
                        } else {
                            badResponseHandler(response.code() + " " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherLocation> call, Throwable t) {
                        dismissProgressBar();
                        t.printStackTrace();
                        badResponseHandler(t.getMessage());
                    }
                });
    }

    private void obtenerPronosticoFuturo(City city) {
        ServiceGenerator.createService(OpenWeatherMapApi.class)
                .getDataForecast(city.getLat(), city.getLng(), BuildConfig.API_KEY)
                .enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                        if (response.isSuccessful()) {
                            if (WeatherFragment.getInstance().isVisible()) {
                                WeatherFragment.getInstance().updateRecyclewViewUI(response.body());
                            }
                        } else {
                            badResponseHandler(response.code() + " " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        t.printStackTrace();
                        badResponseHandler(t.getMessage());
                    }
                });
    }

    private void badResponseHandler(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void newCityConfig(City city) {
        Session session = PreferencesManager.getInstance(this).getSession();
        session.setCity(city);
        PreferencesManager.getInstance(this).saveSession(session);
        replaceFragment(WeatherFragment.getInstance(), R.id.container, false);
        navigation.setSelectedItemId(R.id.navigation_weather);
        obtenerPronostico(PreferencesManager.getInstance(this).getSession().getCity());
        obtenerPronosticoFuturo(PreferencesManager.getInstance(this).getSession().getCity());
    }
}
