package com.zencom.weathertest.ui.config;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.zencom.weathertest.R;
import com.zencom.weathertest.data.shared.PreferencesManager;
import com.zencom.weathertest.pojo.City;
import com.zencom.weathertest.pojo.Session;
import com.zencom.weathertest.ui.base.BaseFragment;
import com.zencom.weathertest.ui.base.InitBasicMethods;
import com.zencom.weathertest.utils.Dummys;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends BaseFragment implements InitBasicMethods {

    private static volatile ConfigFragment configFragment;
    private View view;

    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;

    @BindView(R.id.buttonAplicarCiudad)
    Button buttonAplicarCiudad;

    ArrayAdapter<City> arrayAdapter;
    List<City> cityList = Dummys.cityList;

    public static Fragment getInstance(ConfigFragmentInterface configFragmentInterface) {
        if (configFragment == null) {
            configFragment = new ConfigFragment();
            configFragment.setConfigFragmentInterface(configFragmentInterface);
        }
        return configFragment;
    }

    //region interface implementation require
    public interface ConfigFragmentInterface {
        void newCityConfig(City city);
    }

    ConfigFragmentInterface configFragmentInterface = null;

    public void setConfigFragmentInterface(ConfigFragmentInterface configFragmentInterface) {
        this.configFragmentInterface = configFragmentInterface;
    }
    //endregion


    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (configFragmentInterface == null) {
            try {
                throw new Exception("Require implement interface ConfigFragmentInterface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_config, container, false);
        ButterKnife.bind(this, view);

        init();
        initListeners();
        return view;
    }


    @Override
    public void init() {
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cityList);
        spinnerCity.setAdapter(arrayAdapter);
        focusCurrentCity();
    }

    private void focusCurrentCity() {
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getName().equals(PreferencesManager.getInstance(getActivity()).getSession().getCity().getName())) {
                spinnerCity.setSelection(i);
            }
        }
    }

    @Override
    public void initListeners() {
        buttonAplicarCiudad.setOnClickListener(v -> {
            configFragmentInterface.newCityConfig((City) spinnerCity.getSelectedItem());
        });
    }
}
