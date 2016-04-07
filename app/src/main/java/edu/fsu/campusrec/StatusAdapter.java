package edu.fsu.campusrec;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private ArrayList<String> facilityList;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bldgName;
        TextView status;
        ImageView rightArrow;
        RelativeLayout statusContainer;

        public ViewHolder(View v){
            super(v);
            bldgName = (TextView) v.findViewById(R.id.facility);
            status = (TextView) v.findViewById(R.id.status);
            rightArrow = (ImageView) v.findViewById(R.id.arrow_right);
            statusContainer = (RelativeLayout) v.findViewById(R.id.status_container);
        }
    }

    public StatusAdapter(ArrayList<String> bldgs, Context context) {
        facilityList = bldgs;
        this.context = context;
    }

    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_status, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos) {
        String tmp = facilityList.get(pos);

        holder.bldgName.setText(tmp);

        if(isCurrentlyOpen(pos)){
            setOpen(holder.status);
        }
        else{
            setClosed(holder.status);
        }

        holder.statusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, pos);
                }
            }
        });
    }

    private boolean isCurrentlyOpen(int pos){
        return true;
    }

    private void setOpen(TextView status){
        status.setText("OPEN");
        status.setTextColor(context.getResources().getColor(R.color.openText));
    }

    private void setClosed(TextView status){
        status.setText("CLOSED");
        status.setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }
}
