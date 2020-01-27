package mx.com.sit.pesca.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;

public class AvisoExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<ViajeRecursoEntity>> listHashMap;
    public List<String> seleccionados;
    public Map<String,ViajeRecursoEntity> listado;
    private ChildViewHolder childViewHolder;

    public AvisoExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<ViajeRecursoEntity>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.seleccionados = new ArrayList<String>();
        this.listado = new TreeMap<String, ViajeRecursoEntity>();
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_aviso_group,null);
        }
        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ViajeRecursoEntity vre = (ViajeRecursoEntity)getChild(i,i1);

        final String childText = "" + vre.getRecursoDescripcion() + " - " + vre.getPresentacionDescripcion();
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_aviso_data,null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.chkListChild = (Switch) view.findViewById(R.id.lblListSwitch);

            view.setTag(R.layout.list_view_aviso_data, childViewHolder);
        }
        else {

            childViewHolder = (ChildViewHolder) view.getTag(R.layout.list_view_aviso_data);
        }
        listado.put("" + i +"_"+i1,vre);
        childViewHolder.chkListChild.setText(childText);

        childViewHolder.chkListChild.setOnCheckedChangeListener(null);
        childViewHolder.chkListChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    seleccionados.add("" + i +"_"+i1);
                }
            }
        });
        //Switch chkListChild = view.findViewById(R.id.lblListSwitch);
        //chkListChild.setText(childText);
        /*chkListChild.setId(i1);
        chkListChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    if(isChecked){
                        seleccionados.add("" + i +"_"+i1);
                    }
            }
        });*/

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public final class ChildViewHolder {

        Switch chkListChild;
    }

}
