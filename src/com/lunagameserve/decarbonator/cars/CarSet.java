package com.lunagameserve.decarbonator.cars;

import android.app.ListActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.lunagameserve.nbt.NBTException;
import com.lunagameserve.nbt.NBTSerializableListAdapter;
import com.lunagameserve.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class CarSet extends NBTSerializableListAdapter {

    private List<Car> carList = new ArrayList<Car>();

    private ArrayAdapter<Car> carAdapter = null;

    public Car get(int i) {
        return carList.get(i);
    }

    public void add(Car c) {
        carList.add(c);
        onChange();
    }

    public void remove(int idx) {
        carList.remove(idx);
        onChange();
    }

    public void onChange() {
        if (carAdapter != null) {
            carAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Tag listItemToTag(int i) {
        try {
            return carList.get(i).toCompound();
        } catch (NBTException e) {
            Log.e("CarSet", "Could not serialize car at index " + i + ":" +
                    e.getMessage());
            return null;
        }
    }

    @Override
    protected void listItemFromTag(int i, Tag tag) {
        Car c = new Car();
        c.fromCompound((Tag.Compound)tag);
        carList.add(c);
    }

    @Override
    protected int listItemCount() {
        return carList.size();
    }

    @Override
    protected String listName() {
        return "cars";
    }

    public void setListView(ListActivity activity, int listViewID) {
        this.carAdapter = new ArrayAdapter<Car>(activity.getBaseContext(),
                                                listViewID, carList);
        activity.setListAdapter(carAdapter);
    }
}
