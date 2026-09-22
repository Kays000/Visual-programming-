package com.example.lab4;

public class Car {
    private String model;
    private String licensePlate;
    private String year;
    private String fuelType;
    private String note;

    public Car(String model, String licensePlate, String year, String fuelType, String note) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.fuelType = fuelType;
        this.note = note;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
