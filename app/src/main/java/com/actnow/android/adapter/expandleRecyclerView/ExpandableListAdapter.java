package com.actnow.android.adapter.expandleRecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.sdk.responses.TaskListRecords;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String > _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<TaskListRecords>> _listDataChild;



    public ExpandableListAdapter(ThisWeekActivity context, List<String> listDataHeader, HashMap<String, List<TaskListRecords>> listDataChild) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;

    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get( this._listDataHeader.get( groupPosition ) ).get( childPosition );
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild( groupPosition, childPosition );
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = infalInflater.inflate( R.layout.task_list_cutsom, null );
        }

        TextView mTaskWeekTaskName = (TextView) convertView.findViewById( R.id.tv_WeektaskListName );
        TextView mWeekDudate = convertView.findViewById( R.id.tv_weektaskListDate );
        TextView mTaskWeekProjectName = convertView.findViewById( R.id.tv_WeekprojectNameTaskList );
        TextView mWeekRemdnier = convertView.findViewById( R.id.tv_WeektaskRaminder );
        TextView mWeekTaskPriorty = convertView.findViewById( R.id.tv_WeektaskListPriority );
        TextView mWeekTaskCode = convertView.findViewById( R.id.tv_WeektaskCode );
        TextView mWeekTaskStatus = convertView.findViewById( R.id.tv_Weektaskstatus );
        TextView mWeekProjectCode = convertView.findViewById( R.id.tv_WeekprojectCodeTaskList );
        TextView mWeekRepeat_type = convertView.findViewById( R.id.tv_WeektaskRepeatType );
        mTaskWeekTaskName.setText( childText );
        mWeekDudate.setText( childText );
        mTaskWeekProjectName.setText( childText );
        mWeekRemdnier.setText( childText );
        mWeekTaskCode.setText( childText );
        mWeekTaskPriorty.setText( childText );
        mWeekTaskStatus.setText( childText );
        mWeekProjectCode.setText( childText );
        mWeekRepeat_type.setText( childText );
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
