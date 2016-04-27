package edu.fsu.campusrec;

/**
 * Contact Adapter Module
 * Adapter class to apply contact data to Recycler layout
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    private ArrayList<Contact> contactList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_label;
        TextView title_label;
        ImageView action_phone;
        ImageView action_email;

        public ViewHolder (View v){
            super(v);
            name_label = (TextView) v.findViewById(R.id.label_name);
            title_label = (TextView) v.findViewById(R.id.label_title);
            action_phone = (ImageView) v.findViewById(R.id.action_call);
            action_email = (ImageView) v.findViewById(R.id.action_email);
        }
    }

    public static class Contact {
        private String name;
        private String title;
        private String phone;
        private String email;

        public Contact (@NonNull String name, @NonNull String title, @NonNull String phone, @NonNull String email){
            this.name = name;
            this.title = title;
            this.phone = phone;
            this.email = email;
        }

        public String getName(){
            return this.name;
        }

        public String getTitle(){
            return this.title;
        }

        public String getPhone(){
            return "tel:"+this.phone;
        }

        public String getEmail(){
            return this.email;
        }
    }

    public ContactAdapter(@NonNull ArrayList<Contact> contacts, Context context) {
        this.contactList = contacts;
        this.context = context;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_contact, parent, false);
        RelativeLayout container = (RelativeLayout) item.findViewById(R.id.contact_container);
        switch(viewType){
            case 0:
                container.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
                break;
            case 1:
                container.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondaryBackground));
                break;
        }
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        final Contact person = contactList.get(pos);
        holder.name_label.setText(person.getName());
        holder.title_label.setText(person.getTitle());

        holder.action_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.canCall()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(person.getPhone()));
                    context.startActivity(intent);
                }
                else {
                    Snackbar.make(
                            holder.action_phone,
                            "Your device cannot handle phone calling.",
                            Snackbar.LENGTH_INDEFINITE
                    ).show();
                }
            }
        });

        if(!person.getEmail().equalsIgnoreCase("email")){
            holder.action_email.setVisibility(View.INVISIBLE);
        }
        else {
            holder.action_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launches Browser
                    Intent browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(FacilityData.EMAIL_URL));
                    context.startActivity(browserIntent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position){
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
