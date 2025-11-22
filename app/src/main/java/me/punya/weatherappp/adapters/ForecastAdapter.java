package me.punya.weatherappp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import me.punya.weatherappp.R;
import me.punya.weatherappp.models.ForecastDayModel;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {


    private Context context;
    private List<ForecastDayModel> list;


    public ForecastAdapter(Context context, List<ForecastDayModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastDayModel model = list.get(position);
        holder.tvDay.setText(model.getDayName());
        holder.tvTemp.setText(String.format("%.0f°C", model.getTemperature()));
//        holder.tvCondition.setText(model.getCondition());


        int anim = getWeatherAnimation(model.getIconKey());
        if (anim != 0) holder.icon.setAnimation(anim);
        holder.icon.playAnimation();
    }


    @Override
    public int getItemCount() { return list.size(); }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvCondition, tvTemp;
        LottieAnimationView icon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.textForecastDate);
           // tvCondition = itemView.findViewById(R.id.tvForecastCondition);
            tvTemp = itemView.findViewById(R.id.textForecastTemp);
            icon = itemView.findViewById(R.id.imageForecastIcon);
        }
    }


    private int getWeatherAnimation(String key) {
        if (key == null) return 0;
        key = key.toLowerCase();
        if (key.contains("rain")) return R.raw.rainy;
        if (key.contains("cloud")) return R.raw.rainy;
        if (key.contains("snow")) return R.raw.snowy;
        if (key.contains("storm")) return R.raw.stormy;
        if (key.contains("sun") || key.contains("clear")) return R.raw.sunny;
        return R.raw.sunny;
    }
}
