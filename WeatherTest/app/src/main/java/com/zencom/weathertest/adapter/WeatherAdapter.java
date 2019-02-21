package com.zencom.weathertest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zencom.weathertest.R;
import com.zencom.weathertest.pojo.ItemWeather;
import com.zencom.weathertest.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter {

    private List<ItemWeather> mList = null;
    private Context context = null;

    public WeatherAdapter(List<ItemWeather> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WeatherViewHolder(LayoutInflater.from(context).inflate(R.layout.item_weather, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof WeatherViewHolder) {
            ((WeatherViewHolder) viewHolder).bindData(mList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewDate)
        TextView textViewDate;
        @BindView(R.id.imageViewWeather)
        ImageView imageViewWeather;
        @BindView(R.id.textViewTemp)
        TextView textViewTemp;
        @BindView(R.id.textViewWind)
        TextView textViewWind;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(ItemWeather itemWeather) {
            textViewDate.setText(itemWeather.getDtTxt());
            ImageUtils.loadImage(
                    Uri.parse("http://openweathermap.org/img/w/" + itemWeather.getWeather().get(0).getIcon() + ".png"),
                    imageViewWeather);
            textViewTemp.setText(String.valueOf(itemWeather.getMain().getTemp()));
            textViewWind.setText(String.valueOf(itemWeather.getWind().getSpeed()));
        }
    }
}
