package com.example.question2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ExpLVAdapterTeacher extends BaseExpandableListAdapter {

    private ArrayList<String> listTeams;
    private Map<String, ArrayList<String>> mapChild;
    private Context context;

    public ExpLVAdapterTeacher(Context context, ArrayList<String> listTeams, Map<String, ArrayList<String>> mapChild) {
        this.listTeams = listTeams;
        this.mapChild = mapChild;
        this.context = context;
    }

    public ArrayList<String> getListTeams() {
        return listTeams;
    }

    public void setListTeams(ArrayList<String> listTeams) {
        this.listTeams = listTeams;
    }

    public Map<String, ArrayList<String>> getMapChild() {
        return mapChild;
    }

    public void setMapChild(Map<String, ArrayList<String>> mapChild) {
        this.mapChild = mapChild;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return listTeams.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapChild.get(listTeams.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTeams.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapChild.get(listTeams.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String titleTeam = (String)getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.elv_team,null);
        TextView textView = (TextView) convertView.findViewById(R.id.teamName);
        textView.setText(titleTeam);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String item = (String)getChild(groupPosition,childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.elv_child_teacher,null);
        TextView textView = (TextView) convertView.findViewById(R.id.childExpandableList);
        textView.setText(item);
        ImageView delete = convertView.findViewById(R.id.deleteFromGroup);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Quieres eliminar a esta persona del grupo?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String teamInfo = listTeams.get(groupPosition);
                        teamInfo = teamInfo.replace("Equipo: ","");
                        String[] splitString = teamInfo.split(System.lineSeparator());


                        DatabaseReference teams = FirebaseDatabase.getInstance().getReference("teams");
                        Query query = teams.orderByChild("name").equalTo(splitString[0]);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()) {
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                                    Map<String, String> map = ds.child("students").getValue(genericTypeIndicator );
                                    for(Object o : map.keySet()) {
                                        if (map.get(o).equals((String)getChild(groupPosition,childPosition))) {
                                            String student = (String) o;
                                            FirebaseDatabase.getInstance().getReference("teams/"+ds.getKey()+"/students/"+student).removeValue();
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
