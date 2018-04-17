package com.example.unsan.gpsdclient;

/**
 * Created by Unsan on 16/4/18.
 */

public class DriverNode {
    Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public DriverNode(Driver driver, String name) {
        this.driver = driver;
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public DriverNode()
    {

    }

    String name;

}
