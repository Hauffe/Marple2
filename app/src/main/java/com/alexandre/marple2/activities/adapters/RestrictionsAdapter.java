package com.alexandre.marple2.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandre.marple2.R;
import com.alexandre.marple2.model.Restriction;

import java.util.ArrayList;
import java.util.List;

public class RestrictionsAdapter extends RecyclerView.Adapter<RestrictionsAdapter.RestrictionHolder> {

    private List<Restriction> restrictions;
    private Context context;


    public RestrictionsAdapter(Context context, List<Restriction> restrictions) {
        this.context = context;
        this.restrictions = restrictions;
    }

    @NonNull
    @Override
    public RestrictionsAdapter.RestrictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_restrictions, viewGroup, false);


        return new RestrictionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestrictionsAdapter.RestrictionHolder restrictionHolder, int position) {
        Restriction restriction = restrictions.get(position);

        restrictionHolder.restriction_name.setText(restriction.getName());
        restrictionHolder.restriction_description.setText(restriction.getIngredients().toString());
        restrictionHolder.restriction_on_of_switch.setChecked(restriction.isEnable());
        restrictionHolder.position = position;
    }

    @Override
    public int getItemCount() {
        return restrictions.size();
    }

    public class RestrictionHolder extends RecyclerView.ViewHolder{

        TextView restriction_name;
        TextView restriction_description;
        Switch restriction_on_of_switch;
        Integer position;

        public RestrictionHolder(@NonNull View itemView) {
            super(itemView);

            restriction_name = itemView.findViewById(R.id.restriction_name);
            restriction_description = itemView.findViewById(R.id.restriction_description);

            restriction_on_of_switch = itemView.findViewById(R.id.on_of_switch);

            restriction_on_of_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RestrictionsAdapter.this.context,
                            "what?",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
