package cz.upce.warehouse.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transferNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransferStateEnum state;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "id")
    private Set<TransferItem> transferItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public Set<TransferItem> getTransferItems() {
        return transferItems;
    }

    public void setTransferItems(Set<TransferItem> transferItems) {
        this.transferItems = transferItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TransferStateEnum getState() {
        return state;
    }

    public void setState(TransferStateEnum state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
