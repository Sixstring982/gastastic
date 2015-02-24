package com.lunagameserve.decarbonator.cars;

import com.lunagameserve.nbt.NBTException;
import com.lunagameserve.nbt.NBTSerializableObject;
import com.lunagameserve.nbt.Tag;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class Car implements NBTSerializableObject {

    private String name;
    private int milesPerGallon;

    /**
     * Constructs a new car with default properties. It is highly recommended
     * that this constructor is only called before reading this Car as an NBT
     * object, because its fields cannot be changed any other way.
     */
    public Car() {
    }

    public Car(String name, int milesPerGallon) {
        this.name = name;
        this.milesPerGallon = milesPerGallon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMilesPerGallon(int mpg) {
        this.milesPerGallon = mpg;
    }

    public int getMilesPerGallon() {
        return milesPerGallon;
    }

    @Override
    public Tag.Compound toCompound() throws NBTException {
        return new Tag.Compound.Builder()
                .addString("name", name)
                .addInt("milesPerGallon", milesPerGallon)
                .toCompound("car");
    }

    @Override
    public void fromCompound(Tag.Compound compound) {
        this.name = compound.getString("name");
        this.milesPerGallon = compound.getInt("milesPerGallon");
    }

    @Override
    public String toString() {
        return name + ": " + milesPerGallon + "mpg";
    }
}
