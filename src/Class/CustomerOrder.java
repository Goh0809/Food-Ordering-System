package Class;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class CustomerOrder extends UserCustomer {

    private static final String orderFilePath = "src/Database/order.txt";
    private static final String orderItemsFilePath = "src/Database/orderItems.txt";
    private static final String userFilePath = "src/Database/users.txt";
    private static final String menuFolderPath = "src/Database/Menu/";

    private CustomerCredit customerCredit;
    DateTime dt = new DateTime();

    public CustomerOrder(int id, String username, String password, String role, double credit) {
        super(id, username, password, role, credit);
    }

    public CustomerOrder(CustomerCredit customerCredit) {
        this.customerCredit = customerCredit;
    }

    public CustomerOrder() {

    }

    // place order 
    public void placeOrder(double orderAmount, int customerUserID, int vendorUserID, String serviceType, List<List<String>> orderItems) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (serviceType.equals("Delivery")) {
            orderAmount += 4;
        }
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        String orderStatus = "Pending";
        int lastOrderItemsID = getLastOrderItemsID();
        String lastTransactionID = customerCredit.generateLastTransactionID();
        int lastOrderID = getLastOrderID();
        String data = lastOrderID + "," + currentTime + "," + lastOrderItemsID + "," + orderAmount + "," + orderStatus + "," + customerUserID + "," + vendorUserID + "," + serviceType + "," + lastTransactionID;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFilePath, true))) {
            writer.write(data);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        writeOrderItemsData(orderItems, lastOrderItemsID);
        customerCredit.updateCustomerandVendorCredit(customerUserID, vendorUserID, orderAmount, true);
        customerCredit.generateOrderTransactionData(lastTransactionID, customerUserID, vendorUserID, orderAmount, currentTime, serviceType, lastOrderID);
        generatePlaceOrderNotification(vendorUserID, lastOrderID);
    }

    private void generatePlaceOrderNotification(int userId, int orderID) {
        String dateTime = dt.getCurrentDateTime();
        String content = "You have received a new order. Order ID: " + orderID;
        String category = "Order";

        String notifications = String.format("%d,%s,%s,%s",
                userId, content, dateTime, category);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Database/notifications.txt", true))) {
            bw.write(notifications);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // update order status to Settled
    public void updateOrderDataToSettled(String orderID) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData.length > 0 && orderData[0].equals(orderID)) {
                    // clear the data after reviewDta[3]
                    if (orderData.length >= 9) {
                        String[] tempOrderwData = new String[9];
                        System.arraycopy(orderData, 0, tempOrderwData, 0, 9);
                        orderData = tempOrderwData;
                    }
                    orderData[4] = "Settled";

                }
                String modifiedLine = String.join(",", orderData);
                content.append(modifiedLine).append("\n");
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(orderFilePath));
            writer.write(content.toString());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Review.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Review.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Write Order items data inside the text file
    private void writeOrderItemsData(List<List<String>> orderItems, int lastOrderItemsID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderItemsFilePath, true))) {
            for (List<String> orderItem : orderItems) {
                String orderItemData = lastOrderItemsID + "," + orderItem.get(0) + "," + orderItem.get(1);
                writer.write(orderItemData);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // calculate last orderID
    public int getLastOrderID() {
        int lastOrderID = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFilePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (!fields[0].isEmpty()) {
                    lastOrderID = Integer.parseInt(fields[0]) + 1;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return lastOrderID;
    }

    // calculate lastorderItemsID and it should be same with last order ID
    private int getLastOrderItemsID() {
        return getLastOrderID();
    }

    // get corressponding Customer Order History 
    public List<List<String>> getOrderHistoryData(String username) {
        List<List<String>> orderHistoryData = new ArrayList<>();
        String customerUserID = String.valueOf(super.getUserID(username));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData.length >= 9) {
                    if (orderData[5].equals(customerUserID)) {
                        List<String> orderInfo = new ArrayList<>();
                        orderInfo.add(orderData[0]);  // OrderID
                        orderInfo.add(orderData[1]);  // OrderPlacementTime
                        orderInfo.add(orderData[3]);  // OrderAmount
                        orderInfo.add(orderData[4]);  // OrderStatus
                        orderInfo.add(orderData[7]);  // ServiceType
                        orderHistoryData.add(orderInfo);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderHistoryData;
    }

    // get OrderItemsData for reorder
    public List<List<String>> getOrderItemsDataForReorder(String orderID) {
        List<List<String>> orderItemsData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderItemsFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderItems = line.split(",");
                if (orderItems[0].equals(orderID)) {
                    List<String> orderItemsInfo = new ArrayList<>();
                    orderItemsInfo.add(orderItems[1]);
                    orderItemsInfo.add(orderItems[2]);
                    orderItemsData.add(orderItemsInfo);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderItemsData;
    }

    // get Order Items Data for items details
    public List<List<String>> getOrderItemsData(String orderID) {
        List<List<String>> orderItemsData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderItemsFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderItems = line.split(",");
                if (orderItems[0].equals(orderID)) {
                    List<String> orderItemsInfo = new ArrayList<>();
                    String vendorUsername = super.getVendorUserName(getVendorID(orderItems[0]));
                    orderItemsInfo.add(getOrderItemName(vendorUsername, orderItems[1]));
                    orderItemsInfo.add(orderItems[2]);
                    orderItemsInfo.add(String.valueOf(calculateOrderItemTotalPrice(vendorUsername, orderItems[1], orderItems[2])));
                    orderItemsData.add(orderItemsInfo);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderItemsData;
    }

    // get Order Item Name
    private String getOrderItemName(String vendorUsername, String FoodID) {
        String itemName = null;
        String menuFilePath = menuFolderPath + vendorUsername + "Menu.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(menuFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] ItemData = line.split(",");
                if (ItemData[0].equals(FoodID)) {
                    itemName = ItemData[1];
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemName;
    }

    // calculate Order Item Total Price
    private double calculateOrderItemTotalPrice(String vendorUsername, String FoodID, String itemQuantities) {
        double totalPrice = 0;
        String menuFilePath = menuFolderPath + vendorUsername + "Menu.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(menuFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] ItemData = line.split(",");
                if (ItemData[0].equals(FoodID)) {
                    double itemPrice = Double.parseDouble(ItemData[2]);
                    double itemQuantity = Double.parseDouble(itemQuantities);
                    totalPrice = itemPrice * itemQuantity;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPrice;
    }

    // get vendorUserID in the order.txt file by orderID
    public String getVendorID(String orderID) {
        String vendorID = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData[0].equals(orderID)) {
                    vendorID = orderData[6];
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vendorID;
    }

    // get customerUserID in the order.txt file by orderID
    public String getCustomerID(String orderID) {
        String customerID = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData[0].equals(orderID)) {
                    customerID = orderData[5];
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerID;
    }

    // update the order status to the Cancelled in the order.txt file
    public void cancelOrder(String orderID) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData[0].equals(orderID)) {
                    orderData[4] = "Cancelled";
                    int customerID = Integer.parseInt(orderData[5]);
                    int vendorID = Integer.parseInt(orderData[6]);
                    double orderAmount = Double.parseDouble(orderData[3]);
                    customerCredit.updateCustomerandVendorCredit(customerID, vendorID, orderAmount, false);
                    customerCredit.generateOrderCancelledTransactionData(customerID, vendorID, orderAmount);
                }
                String modifiedLine = String.join(",", orderData);
                content.append(modifiedLine).append("\n");
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(orderFilePath));
            writer.write(content.toString());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
