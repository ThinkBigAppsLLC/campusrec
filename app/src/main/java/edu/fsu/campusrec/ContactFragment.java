package edu.fsu.campusrec;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ContactFragment extends Fragment {
    private static final String CONTACT_URL = "http://campusrec.fsu.edu/staff/contact-our-team";

    private View v;


    public ContactFragment() { }
    private ArrayList<ArrayList<ContactAdapter.Contact>> contacts;

    public static class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context) {
            super(context);
        }
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GetDetails task = new GetDetails();
        task.execute(CONTACT_URL);
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        this.v = v;

        setVisibilities(View.GONE);
        
        return v;
    }

    private class GetDetails extends AsyncTask<String, Void, ArrayList<ArrayList<ContactAdapter.Contact>>> {
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected ArrayList<ArrayList<ContactAdapter.Contact>> doInBackground(String... urls) {
            ArrayList<ArrayList<ContactAdapter.Contact>> tmpContacts = new ArrayList<>();
            ArrayList<ContactAdapter.Contact> tmp;

            try {
                Document doc  = Jsoup.connect(CONTACT_URL).get();
                Elements tables = doc.select("thead");
                for(Element table : tables){
                    tmp = new ArrayList<>();
                    ContactAdapter.Contact tmpContact = null;
                    String name;
                    String title;
                    String phone;
                    String email;

                    Element body = table.nextElementSibling();
                    Elements rows = body.children();
                    rows.remove(0);
                    for(Element row : rows){
                        Elements fields = row.children().select("td");
                        name = fields.get(0).text();
                        title = fields.get(1).text();
                        phone = fields.get(2).text();
                        email = fields.get(3).text().trim();
                        tmpContact = new ContactAdapter.Contact(name, title, phone, email);
                        tmp.add(tmpContact);
                    }
                    tmpContacts.add(tmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            contacts = tmpContacts;
            return tmpContacts;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<ContactAdapter.Contact>> result) {
            v.findViewById(R.id.loading).setVisibility(View.GONE);

            setVisibilities(View.VISIBLE);

            RecyclerView contactRecycler = (RecyclerView) v.findViewById(R.id.list_crao);
            setRecyclerAdapter(contactRecycler, contacts.get(0));
            contactRecycler = (RecyclerView) v.findViewById(R.id.list_lcfa);
            setRecyclerAdapter(contactRecycler, contacts.get(1));
            contactRecycler = (RecyclerView) v.findViewById(R.id.list_fmcw);
            setRecyclerAdapter(contactRecycler, contacts.get(2));
            contactRecycler = (RecyclerView) v.findViewById(R.id.list_imsp);
            setRecyclerAdapter(contactRecycler, contacts.get(3));
            contactRecycler = (RecyclerView) v.findViewById(R.id.list_cssc);
            setRecyclerAdapter(contactRecycler, contacts.get(4));
            contactRecycler = (RecyclerView) v.findViewById(R.id.list_rocf);
            setRecyclerAdapter(contactRecycler, contacts.get(5));
        }
    }

    private void setRecyclerAdapter(RecyclerView mRecyclerView, ArrayList<ContactAdapter.Contact> contacts){
        CustomLinearLayoutManager mRecyclerManager = new CustomLinearLayoutManager(getContext());
        ContactAdapter contactAdapter;
        contactAdapter = new ContactAdapter(contacts, getContext());
        mRecyclerView.setAdapter(contactAdapter);
        mRecyclerView.setLayoutManager(mRecyclerManager);
        mRecyclerView.setHasFixedSize(true);
    }
    
    private void setVisibilities(int vis){
        v.findViewById(R.id.label_crao).setVisibility(vis);
        v.findViewById(R.id.label_lcfa).setVisibility(vis);
        v.findViewById(R.id.label_fmcw).setVisibility(vis);
        v.findViewById(R.id.label_imsp).setVisibility(vis);
        v.findViewById(R.id.label_cssc).setVisibility(vis);
        v.findViewById(R.id.label_rocf).setVisibility(vis);
        v.findViewById(R.id.list_crao).setVisibility(vis);
        v.findViewById(R.id.list_lcfa).setVisibility(vis);
        v.findViewById(R.id.list_fmcw).setVisibility(vis);
        v.findViewById(R.id.list_imsp).setVisibility(vis);
        v.findViewById(R.id.list_cssc).setVisibility(vis);
        v.findViewById(R.id.list_rocf).setVisibility(vis);
        v.findViewById(R.id.divider_crao).setVisibility(vis);
        v.findViewById(R.id.divider_lcfa).setVisibility(vis);
        v.findViewById(R.id.divider_fmcw).setVisibility(vis);
        v.findViewById(R.id.divider_imsp).setVisibility(vis);
        v.findViewById(R.id.divider_cssc).setVisibility(vis);
    }
}
