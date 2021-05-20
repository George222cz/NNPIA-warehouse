package cz.upce.warehouse.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String description;

    private Integer amount;

    private Integer unitWeight;

    @ManyToOne
    @JsonIgnore
    private Warehouse warehouse;

    @Column(name = "warehouse_id",insertable = false, updatable = false)
    private Long warehouseId;

    @OneToMany(mappedBy = "id")
    @JsonIgnore
    private Set<TransferItem> productsInTransfers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Set<TransferItem> getProductsInTransfers() {
        return productsInTransfers;
    }

    public void setProductsInTransfers(Set<TransferItem> productsInTransfers) {
        this.productsInTransfers = productsInTransfers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Integer unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(description, product.description) &&
                Objects.equals(amount, product.amount) &&
                Objects.equals(unitWeight, product.unitWeight) &&
                Objects.equals(warehouseId, product.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, description, amount, unitWeight, warehouseId);
    }
}
