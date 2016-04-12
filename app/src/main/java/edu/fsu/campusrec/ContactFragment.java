package edu.fsu.campusrec;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContactFragment extends Fragment {
    private static final String CONTACT_URL = "http://campusrec.fsu.edu/staff/contact-our-team";

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
        try {
            task.get(7, TimeUnit.SECONDS);
        } catch (InterruptedException|ExecutionException|TimeoutException e) {
            e.printStackTrace();
        }

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

        return v;
    }

    private class GetDetails extends AsyncTask<String, Void, ArrayList<ArrayList<ContactAdapter.Contact>>> {
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
        protected void onPostExecute(ArrayList<ArrayList<ContactAdapter.Contact>> result) { }
    }

    private void setRecyclerAdapter(RecyclerView mRecyclerView, ArrayList<ContactAdapter.Contact> contacts){
        CustomLinearLayoutManager mRecyclerManager = new CustomLinearLayoutManager(getContext());
        ContactAdapter contactAdapter;
        contactAdapter = new ContactAdapter(contacts, getContext());
        mRecyclerView.setAdapter(contactAdapter);
        mRecyclerView.setLayoutManager(mRecyclerManager);
        mRecyclerView.setHasFixedSize(true);
    }
}
