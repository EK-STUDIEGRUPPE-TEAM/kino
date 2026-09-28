package dk.kino.kino.model;

import jakarta.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "showing_id")
    private Showing showing;

    public Reservation() {
    }

    public Reservation(String customerName, String phone, Showing showing) {
        this.customerName = customerName;
        this.phone = phone;
        this.showing = showing;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }
}