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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
//List for the students to see their goups
public class ExpLVAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> listTeams;
    private Map<String, ArrayList<String>> mapChild;
    private Context context;
    private String key;


    public ExpLVAdapter(Context context, ArrayList<String> listTeams, Map<String, ArrayList<String>> mapChild) {
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
        ImageView delete = convertView.findViewById(R.id.deleteGroup);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Quieres salir de este grupo?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String teamInfo = listTeams.get(groupPosition);
                        teamInfo = teamInfo.replace("Equipo: ","");
                        String[] splitString = teamInfo.split(System.lineSeparator());

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String mail = user.getEmail();
                        DatabaseReference teams = FirebaseDatabase.getInstance().getReference("teams");
                        Query query = teams.orderByChild("name").equalTo(splitString[0]);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot ds : snapshot.getChildren()){
                                        key = ds.getKey();
                                    }

                                    //find specific user to delete in this team
                                    DatabaseReference teams = FirebaseDatabase.getInstance()
                                            .getReference("teams/"+key+"/students");
                                    Query query = teams;
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String key2;
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    key2 = ds.getKey();
                                                    String str = ds.getValue().toString();
                                                    String email = str.substring(str.indexOf('=') + 1);
                                                    Boolean iguales = email.equals(mail);
                                                    //if the email is the one we want to delete, we delete the user from the team
                                                    if (email.equals(mail)){
                                                        FirebaseDatabase.getInstance()
                                                                .getReference("teams/"+key+"/students/"+key2)
                                                                .removeValue();
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String item = (String)getChild(groupPosition,childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.elv_child,null);
        TextView textView = (TextView) convertView.findViewById(R.id.childExpandableList);
        textView.setText(item);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
