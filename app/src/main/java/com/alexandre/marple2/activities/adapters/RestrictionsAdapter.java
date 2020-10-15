package com.alexandre.marple2.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandre.marple2.R;
import com.alexandre.marple2.activities.EditRestrictionActivity;
import com.alexandre.marple2.activities.Interfaces.ItemClickListener;
import com.alexandre.marple2.activities.NewRestrictionActivity;
import com.alexandre.marple2.model.Restriction;
import com.alexandre.marple2.repository.RestrictionsDAO;
import com.alexandre.marple2.repository.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class RestrictionsAdapter extends RecyclerView.Adapter<RestrictionsAdapter.RestrictionHolder> {

    private static ItemClickListener itemClickListener;
    private List<Restriction> restrictions;
    private Context context;
    private AppDatabase db;


    public RestrictionsAdapter(Context context, List<Restriction> restrictions) {
        this.context = context;
        this.restrictions = restrictions;
        db = AppDatabase.getInstance(context);
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
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
        restrictionHolder.restriction_description.setText(restriction.getIngredientsString());
        restrictionHolder.restriction_on_of_switch.setChecked(restriction.isEnable());
        restrictionHolder.position = position;
    }

    @Override
    public int getItemCount() {
        return restrictions.size();
    }

    public class RestrictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView restriction_name;
        TextView restriction_description;
        Switch restriction_on_of_switch;
        Integer position;

        public RestrictionHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            restriction_name = itemView.findViewById(R.id.restriction_name);
            restriction_description = itemView.findViewById(R.id.restriction_description);
            restriction_on_of_switch = itemView.findViewById(R.id.on_of_switch);
            restriction_on_of_switch.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Restriction restriction = getRestrictionByName(restriction_name.getText().toString());
                            restriction.setEnable(restriction_on_of_switch.isChecked());
                            db.restrictionDAO().update(restriction);

                            Toast.makeText(RestrictionsAdapter.this.context,
                                    restriction_on_of_switch.isChecked() ? "Enabled" : "Disabled",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
            );

        }
        public Restriction getRestrictionByName(String name) {
            Restriction found_restriction = new Restriction();
            for(Restriction restriction : restrictions){
                if (restriction.getName().compareToIgnoreCase(name) == 0 ){
                    found_restriction = restriction;
                }
            }
            return found_restriction;
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null) {
                itemClickListener.onRestrictionClick(getAdapterPosition(), restrictions.get(position));
            }
        }
    }
}
