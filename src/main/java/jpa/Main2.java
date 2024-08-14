package jpa;
import javax.persistence.EntityManager;
import jpa.config.SpringConfig;
import jpa.entity.OrdersEntity;
import jpa.entity.OrdersDetailsEntity;
import jpa.repository.OrdersRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.*;


public class Main2 {
    static ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    static OrdersRepository ordersRepository = (OrdersRepository) context.getBean("ordersRepository");
    static EntityManagerFactory entityManagerFactory = (EntityManagerFactory) context.getBean("entityManagerFactory");


    public static void main(String[] args) {
        createDatabase();
        createNewOrder();
        listAllOrdersAndDetails();
        getOrderAndDetailsById();
        listOrdersInCurrentMonth();
        listOrdersWithTotalAmountMoreThan1000();
        listOrdersBuyJavaBook();
    }

           private static void createDatabase() {
            OrdersEntity order = new OrdersEntity();
            order.setOrderDate(new Date());
            order.setCustomerName("nguyen van a");
            order.setCustomerAddress("DN");

            OrdersDetailsEntity detail = new OrdersDetailsEntity();
            detail.setProductName("Laptop");
            detail.setQuantity(1);
            detail.setUnitPrice(1000.0);
            detail.setOrders(order);

            OrdersDetailsEntity detail1 = new OrdersDetailsEntity();
            detail1.setProductName("Mouse");
            detail1.setQuantity(2);
            detail1.setUnitPrice(20.0);
            detail1.setOrders(order);

            List<OrdersDetailsEntity> orderDetails = new ArrayList<>();
            orderDetails.add(detail);
            orderDetails.add(detail1);
            order.setOrdersDetailsEntities(orderDetails);

        ordersRepository.save(order);

    }
    private static void createNewOrder() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter customer name:");
        String customerName = scanner.nextLine();

        System.out.println("Enter customer address:");
        String customerAddress = scanner.nextLine();

        OrdersEntity order = new OrdersEntity();
        order.setOrderDate(new Date());
        order.setCustomerName(customerName);
        order.setCustomerAddress(customerAddress);

        List<OrdersDetailsEntity> orderDetailsList = new ArrayList<>();
        while (true) {
            System.out.println("Enter product name (or 'done' to finish):");
            String productName = scanner.nextLine();
            if (productName.equalsIgnoreCase("done")) {
                break;
            }

            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter unit price:");
            double unitPrice = scanner.nextDouble();
            scanner.nextLine();

            OrdersDetailsEntity orderDetail = new OrdersDetailsEntity();
            orderDetail.setProductName(productName);
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(unitPrice);
            orderDetail.setOrders(order);

            orderDetailsList.add(orderDetail);
        }

        order.setOrdersDetailsEntities(orderDetailsList);

        ordersRepository.save(order);

        System.out.println("Order created successfully!");
    }

    private static void listAllOrdersAndDetails() {
        Iterable<OrdersEntity> orders = ordersRepository.findAll();

        for (OrdersEntity order : orders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Customer Address: " + order.getCustomerAddress());

            List<OrdersDetailsEntity> orderDetails = order.getOrdersDetailsEntities();

            System.out.println("Order Details:");
            for (OrdersDetailsEntity orderDetail : orderDetails) {
                System.out.println("  - Product Name: " + orderDetail.getProductName());
                System.out.println("  - Quantity: " + orderDetail.getQuantity());
                System.out.println("  - Unit Price: " + orderDetail.getUnitPrice());
            }
        }
    }
    @Transactional
    private static void getOrderAndDetailsById() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter order ID:");
        int orderId = scanner.nextInt();

        Optional<OrdersEntity> optionalOrder = ordersRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            OrdersEntity order = optionalOrder.get();

            System.out.println("Order ID: " + order.getId());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Customer Address: " + order.getCustomerAddress());

            System.out.println("Order Details:");
            order.getOrdersDetailsEntities().forEach(detail -> {
                System.out.println("  - Product Name: " + detail.getProductName());
                System.out.println("  - Quantity: " + detail.getQuantity());
                System.out.println("  - Unit Price: " + detail.getUnitPrice());
            });
        } else {
            System.out.println("Order not found with ID: " + orderId);
        }
    }

    private static void listOrdersInCurrentMonth() {
        YearMonth currentYearMonth = YearMonth.now();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String jpql = "SELECT o FROM OrdersEntity o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month";
        TypedQuery<OrdersEntity> query = entityManager.createQuery(jpql, OrdersEntity.class);
        query.setParameter("year", currentYearMonth.getYear());
        query.setParameter("month", currentYearMonth.getMonthValue());

        List<OrdersEntity> orders = query.getResultList();

        if (orders.isEmpty()) {
            System.out.println("No orders found in the current month.");
        } else {
            System.out.println("Orders in the current month (" + currentYearMonth + "):");
            orders.forEach(order -> {
                System.out.println("Order ID: " + order.getId());
                System.out.println("Order Date: " + order.getOrderDate());
            });
        }
    }

    private static void listOrdersWithTotalAmountMoreThan1000() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String jpql = "SELECT o FROM OrdersEntity o WHERE (SELECT SUM(od.unitPrice * od.quantity) FROM OrdersDetailsEntity od WHERE od.orders = o) > 1000";
        TypedQuery<OrdersEntity> query = entityManager.createQuery(jpql, OrdersEntity.class);

        List<OrdersEntity> orders = query.getResultList();

        if (orders.isEmpty()) {
            System.out.println("No orders found with total amount more than 1,000USD.");
        } else {
            System.out.println("Orders with total amount more than 1,000USD:");
            orders.forEach(order -> {
                System.out.println("Order ID: " + order.getId());
                System.out.println("Order Date: " + order.getOrderDate());
                System.out.println("Customer Name: " + order.getCustomerName());
                System.out.println("Customer Address: " + order.getCustomerAddress());
                System.out.println("Total Amount: " + order.getOrdersDetailsEntities().stream().mapToDouble(od -> od.getUnitPrice() * od.getQuantity()).sum());
            });
        }
    }
    private static void listOrdersBuyJavaBook() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String jpql = "SELECT o FROM OrdersEntity o JOIN o.ordersDetailsEntities od WHERE od.productName LIKE '%Java%'";
        TypedQuery<OrdersEntity> query = entityManager.createQuery(jpql, OrdersEntity.class);

        List<OrdersEntity> orders = query.getResultList();

        if (orders.isEmpty()) {
            System.out.println("No orders found with Java book.");
        } else {
            System.out.println("Orders with Java book:");
            orders.forEach(order -> {
                System.out.println("Order ID: " + order.getId());
                System.out.println("Order Date: " + order.getOrderDate());
                System.out.println("Customer Name: " + order.getCustomerName());
                System.out.println("Customer Address: " + order.getCustomerAddress());
                order.getOrdersDetailsEntities().forEach(od -> {
                    if (od.getProductName().toLowerCase().contains("java")) {
                        System.out.println("Product Name: " + od.getProductName());
                        System.out.println("Quantity: " + od.getQuantity());
                        System.out.println("Unit Price: " + od.getUnitPrice());
                    }
                });
            });
        }
    }

}