package org.yearup.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderID;

    @Column(name = "user_id")
    private int userID;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "shipping_amount")
    private double shippingAmount;

    public Order() {
    }

    public Order(int orderID, int userID, LocalDateTime dateTime, String address, String city, String state, String zip, double shippingAmount) {
        this.orderID = orderID;
        this.userID = userID;
        this.dateTime = dateTime;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public double getShippingAmount() {
        return shippingAmount;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setShippingAmount(double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }
}
