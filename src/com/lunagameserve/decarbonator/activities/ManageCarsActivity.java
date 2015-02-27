package com.lunagameserve.decarbonator.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.cars.Car;
import com.lunagameserve.decarbonator.cars.CarSet;
import com.lunagameserve.decarbonator.statistics.StatisticType;
import com.lunagameserve.nbt.NBTException;
import com.lunagameserve.nbt.NBTSerializableObject;
import com.lunagameserve.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class ManageCarsActivity extends ListActivity
                                implements NBTSerializableObject {

    private static final String CARS_FILE_PATH = "cars.nbt";

    @NotNull
    private CarSet carSet = new CarSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        carSet.setListView(this, android.R.layout.simple_list_item_1);

        setContentView(R.layout.manage_cars_activity);

        ListView lv = (ListView)findViewById(android.R.id.list);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(onCarSelect());
        if (!loadCars()) {
            toastLong("No vehicle data found. Please add a vehicle.");
        } else {
            toastLong("Select a vehicle for this drive.");
        }
    }

    @NotNull
    private AdapterView.OnItemClickListener onCarSelect() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Car selected = carSet.get(position);
                pushNextActivity(selected, false);
            }
        };
    }

    private void pushNextActivity(@NotNull Car car, boolean debug) {
        Intent intent = new Intent(
                getBaseContext(), TripActivity.class);

        intent.putExtra("car_mpg", car.getMilesPerGallon());
        intent.putExtra("car_name", car.getName());
        intent.putExtra("debug", debug);
        intent.putExtra("setType", StatisticType.Drive.ordinal());
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, @NotNull View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == android.R.id.list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.manage_cars_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NotNull MenuItem item) {
        int idx =((AdapterView.AdapterContextMenuInfo)item.getMenuInfo())
                .position;
        switch(item.getItemId()) {
            case R.id.edit_car:
                onEditCarClick(idx);
                return true;
            case R.id.delete_car:
                onDeleteCarClick(idx);
                return true;
            case R.id.debug_car:
                onDebugCarClick(idx);
                return true;
            default: return super.onContextItemSelected(item);
        }
    }

    /**
     * Loads cars from an NBT formatted file located at {@link #CARS_FILE_PATH}
     * relative to this {@link android.content.Context}'s base file directory.
     *
     * @return {@code true} if the cars were loaded correctly, else
     *         {@code false}.
     */
    private boolean loadCars() {
        Log.d("ManageCarsActivity", "Loading cars file...");
        File carsFile = getCarsFile();
        try {
            GZIPInputStream in =
                    new GZIPInputStream(new FileInputStream(carsFile));

            this.fromCompound(Tag.readCompound(in));

            in.close();
        } catch (IOException e) {
            return false;
        } catch (NBTException e) {
            return false;
        }
        return true;
    }

    private boolean saveCars() {
        Log.d("ManageCarsActivity", "Saving cars file...");
        File carsFile = getCarsFile();
        try {
            GZIPOutputStream out =
                    new GZIPOutputStream(new FileOutputStream(carsFile));

            carsFile.createNewFile();
            this.toCompound().writeNamed(out);

            out.close();
        } catch (IOException e) {
            return false;
        } catch (NBTException e) {
            return false;
        }
        return true;
    }

    @NotNull
    private File getCarsFile() {
        return new File(getBaseContext().getFilesDir(), CARS_FILE_PATH);
    }

    @Override
    public Tag.Compound toCompound() throws NBTException {
        return new Tag.Compound.Builder()
                .addList("cars", carSet.toList())
                .toCompound("manageCarsActivity");
    }

    @Override
    public void fromCompound(@NotNull Tag.Compound compound) {
        carSet.fromList(compound.getList("cars"));
    }

    public void onEditCarClick(final int idx) {
        final NewCarFragment ncfrag = new NewCarFragment();
        ncfrag.setOnAcceptCallback(new Runnable() {
            @Override
            public void run() {
                Car oldCar = carSet.get(idx);
                oldCar.setName(ncfrag.getNewCarName());
                oldCar.setMilesPerGallon(ncfrag.getNewCarMPG());
                carSet.onChange();
                if (!saveCars()) {
                    Log.e("ManageCarsActivity", "Could not save car list!");
                }
            }
        });
        ncfrag.show(getFragmentManager(), "EditCarFragment");
    }

    private void onDebugCarClick(final int idx) {
        Car car = carSet.get(idx);
        pushNextActivity(car, true);
    }

    private void onDeleteCarClick(final int idx) {
        carSet.remove(idx);
        if (!saveCars()) {
            Log.e("ManageCarsActivity", "Could not save car list!");
        }
    }

    public void onAddCarClick(View view) {
        final NewCarFragment ncfrag = new NewCarFragment();
        ncfrag.setOnAcceptCallback(new Runnable() {
            @Override
            public void run() {
                Car c = new Car(ncfrag.getNewCarName(),
                                ncfrag.getNewCarMPG());

                carSet.add(c);
                if (!saveCars()) {
                    Log.e("ManageCarsActivity", "Could not save car list!");
                }
            }
        });
        ncfrag.show(getFragmentManager(), "NewCarFragment");
    }

    /* No multiple inheritance, so here's these methods. */
    protected void toastLong(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void toastShort(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
