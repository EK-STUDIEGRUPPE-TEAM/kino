package dk.kino.kino.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EmployeeType type;


    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    public Employee() {
    }

    public Employee(Long id, String name, EmployeeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }
}
