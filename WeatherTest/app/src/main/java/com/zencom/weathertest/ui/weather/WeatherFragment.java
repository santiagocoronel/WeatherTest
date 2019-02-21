package com.zencom.weathertest.ui.weather;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zencom.weathertest.R;
import com.zencom.weathertest.adapter.WeatherAdapter;
import com.zencom.weathertest.data.shared.PreferencesManager;
import com.zencom.weathertest.pojo.City;
import com.zencom.weathertest.pojo.ItemWeather;
import com.zencom.weathertest.pojo.Session;
import com.zencom.weathertest.pojo.WeatherForecast;
import com.zencom.weathertest.pojo.WeatherLocation;
import com.zencom.weathertest.ui.base.BaseFragment;
import com.zencom.weathertest.ui.base.InitBasicMethods;
import com.zencom.weathertest.utils.ImageUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends BaseFragment implements InitBasicMethods {

    private static volatile WeatherFragment weatherFragment;

    private View view;

    //region butterknife bind
    @BindView(R.id.textViewCityName)
    TextView textViewCityName;
    @BindView(R.id.imageViewWeatherCondition)
    ImageView imageViewWeatherCondition;
    @BindView(R.id.textViewDescriptionWeather)
    TextView textViewDescriptionWeather;
    @BindView(R.id.textViewPressure)
    TextView textViewPressure;
    @BindView(R.id.textViewTemp)
    TextView textViewTemp;
    @BindView(R.id.textViewTempMax)
    TextView textViewTempMax;
    @BindView(R.id.textViewTempMin)
    TextView textViewTempMin;
    @BindView(R.id.recyclewViewFutureWeather)
    RecyclerView recyclewViewFutureWeather;

    private List<ItemWeather> mList = new ArrayList<>();
    private WeatherAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    //endregion

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment getInstance() {
        if (weatherFragment == null) {
            weatherFragment = new WeatherFragment();
        }
        return weatherFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    @Override
    public void init() {
        updateUI();

        adapter = new WeatherAdapter(mList, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclewViewFutureWeather.setLayoutManager(linearLayoutManager);
        recyclewViewFutureWeather.setAdapter(adapter);


    }

    @Override
    public void initListeners() {

    }

    public void updateUI() {
        Session session = PreferencesManager.getInstance(getActivity()).getSession();
        WeatherLocation data = session.getWeatherLocation();
        textViewCityName.setText(session.getCity().getName());

        if (data != null) {
            WeatherLocation weatherLocation = data;

            String icon = data.getWeather().get(0).getIcon();
            String country = weatherLocation.getSys().getCountry();
            String descriptionWeather = weatherLocation.getWeather().get(0).getDescription();
            double pressure = weatherLocation.getMain().getPressure();
            Double temp = weatherLocation.getMain().getTemp();
            Double tempMax = weatherLocation.getMain().getTempMax();
            Double tempMin = weatherLocation.getMain().getTempMin();
            int weatherList = weatherLocation.getWeather().size();


            textViewDescriptionWeather.setText("Weather: " + descriptionWeather);
            textViewPressure.setText("Pressure: " + pressure);
            textViewTemp.setText("Temp: " + String.valueOf(temp));
            textViewTempMax.setText("Max: " + String.valueOf(tempMax));
            textViewTempMin.setText("Min: " + String.valueOf(tempMin));

            //recyclewViewFutureWeather
            ImageUtils.loadImage(Uri.parse("http://openweathermap.org/img/w/" + icon + ".png"), imageViewWeatherCondition);
        }

    }

    public void updateRecyclewViewUI(WeatherForecast weatherForecast) {
        mList.clear();
        mList.addAll(weatherForecast.getList());
        adapter.notifyDataSetChanged();
    }
}
