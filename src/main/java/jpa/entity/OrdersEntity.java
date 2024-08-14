package jpa.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "orderDate")
    private Date orderDate;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "customerAddress")
    private String customerAddress;


    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrdersDetailsEntity> ordersDetailsEntities;

    public OrdersEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrdersDetailsEntity> getOrdersDetailsEntities() {
        return ordersDetailsEntities;
    }

    public void setOrdersDetailsEntities(List<OrdersDetailsEntity> ordersDetailsEntities) {
        this.ordersDetailsEntities = ordersDetailsEntities;
    }

    @Override
    public String toString() {
        return "OrdersEntity{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", ordersDetailsEntities=" + ordersDetailsEntities +
                '}';
    }
}
