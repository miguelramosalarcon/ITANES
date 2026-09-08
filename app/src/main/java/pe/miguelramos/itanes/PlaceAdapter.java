package pe.miguelramos.itanes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pe.miguelramos.itanes.data.local.entity.PlaceEntity;
import com.bumptech.glide.Glide;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    public interface OnPlaceClickListener {
        void onPlaceClick(PlaceEntity place);
    }

    private List<PlaceEntity> places = new ArrayList<>();
    private OnPlaceClickListener listener;

    public void setPlaces(List<PlaceEntity> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceEntity currentPlace = places.get(position);
        holder.textName.setText(currentPlace.getName());
        holder.textShortDescription.setText(currentPlace.getShortDescription());

        com.bumptech.glide.Glide.with(holder.itemView.getContext())
                .load(currentPlace.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .centerCrop()
                .into(holder.imagePlace);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(currentPlace);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagePlace;
        private final TextView textName;
        private final TextView textShortDescription;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlace = itemView.findViewById(R.id.imagePlace);
            textName = itemView.findViewById(R.id.textPlaceName);
            textShortDescription = itemView.findViewById(R.id.textPlaceDescription);
        }
    }
}
