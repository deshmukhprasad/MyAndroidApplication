package com.example.hello.myapplication;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Subjectlist> subjectArrayList;
    public RecyclerAdapter recyclerAdapter;
    DatabaseHelper db = new DatabaseHelper(this); //Object for database helper class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        subjectArrayList = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.scrollableview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        setRecyclerViewData(); //To set Data for elements of recycler view

        recyclerAdapter = new RecyclerAdapter(this, subjectArrayList);
        recyclerView.setAdapter(recyclerAdapter);

        fab.setOnClickListener(onAddingListner()); // action for floating action button



    }
        private void setRecyclerViewData() {

        //Adding DataBase data to the ArrayList to Display on RecyclerView
            Cursor res = db.getAllData();
            if(res.getCount() == 0){
                return;
            }
            while (res.moveToNext()){
                subjectArrayList.add(new Subjectlist(res.getString(0),Float.parseFloat(res.getString(4)),Integer.parseInt(res.getString(1)),Integer.parseInt(res.getString(2))));
            }

        }




        private View.OnClickListener onAddingListner(){ //To add new subject...
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.addsubject); //To specify the layout of dialogue
                    dialog.setTitle("Add New Subject");
                    dialog.setCancelable(false); /// cannot dismiss if touched else where

                    ////////// setting layout for component
                    EditText subname = (EditText) dialog.findViewById(R.id.name);
                    Button savebtn = (Button) dialog.findViewById(R.id.btn_ok);
                    Button cancelbtn = (Button) dialog.findViewById(R.id.btn_cancel);

                    //////////// handling event for 2 buttons/////////////////
                    savebtn.setOnClickListener(onConfirmListener(subname,dialog));
                    cancelbtn.setOnClickListener(onCancelListener(dialog));

                    dialog.show();
                }
            };

        }

    private View.OnClickListener onConfirmListener(final EditText subname,  final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subjectlist newsub = new Subjectlist(subname.getText().toString().trim(), 0,0,0);

                //adding new object to database
                db.createsub(subname.getText().toString().trim(),0,0,0,0);


                //adding new object to arraylist
                subjectArrayList.add(newsub);

                //notify data set changed in RecyclerView adapter
                recyclerAdapter.notifyDataSetChanged();
                //.notifyDataSetChanged();


                Toast.makeText(getBaseContext(), "New subject added !" , Toast.LENGTH_SHORT ).show(); //to toast a data

                //close dialog after all
                dialog.dismiss();
            }
        };
    }

    private View.OnClickListener onCancelListener(final Dialog dialog){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
//        setRecyclerViewData();
//        recyclerAdapter.notifyDataSetChanged();

    }
}
