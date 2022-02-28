package com.example.question2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ExpLVAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> listTeams;
    private Map<String, ArrayList<String>> mapChild;
    private Context context;

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
