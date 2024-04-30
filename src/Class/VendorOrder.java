/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class VendorOrder extends User {

    private static final String userFilePath = "src/Database/users.txt";
    private static final String orderFilePath = "src/Database/order.txt/";
    private static final String creditTransactionFilePath = "src/Database/credit_transaction.txt";
    private static final String runnerTaskFilePath = "src/Database/runnerTask.txt";
    private String username;
    DateTime dt = new DateTime();

    public VendorOrder() {
        
    }
    
    public VendorOrder(int id, String username, String password, String role) {

    }

    public VendorOrder(String username) {
        this.username = username;
    }

    //get vendor order from textfile
    public List<List<String>> getVendorOrder(String vendorName) {
        List<List<String>> orderItems = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFilePath));
            reader.readLine(); 
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> orderItem = new ArrayList<>(Arrays.asList(line.split(",")));
                int orderVendorID = Integer.parseInt(orderItem.get(6));

                if (orderVendorID == getVendorUserIdByUsername(vendorName)) {
                    orderItems.add(orderItem);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return orderItems;
    }


    //get vendorid from username
    public int getVendorUserIdByUsername(String vendorName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String userName = userData[1].trim();
                int userID = Integer.parseInt(userData[0].trim());
                if (userName.equalsIgnoreCase(vendorName)) {
                    return userID;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return -1; 
    }

    //display food details in table
    public void displayFoodDetails(DefaultTableModel model, String orderItemID) throws IOException {
        model.setRowCount(0);
        try {
            String menuFolderPath = "src/Database/Menu/";
            String menuFilePath = menuFolderPath + username + "Menu.txt";

            BufferedReader menuReader = new BufferedReader(new FileReader(menuFilePath));
            Map<String, String> foodNameMap = new HashMap<>();

            String menuLine;
            while ((menuLine = menuReader.readLine()) != null) {
                String[] menuData = menuLine.split(",");
                String foodID = menuData[0].trim();
                String foodName = menuData[1].trim(); 
                foodNameMap.put(foodID, foodName);
            }
            BufferedReader orderReader = new BufferedReader(new FileReader("src/Database/orderItems.txt"));
            orderReader.readLine(); 
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                List<String> orderItemDetails = Arrays.asList(orderLine.split(","));
                String currentOrderItemID = orderItemDetails.get(0);

                if (currentOrderItemID.equals(orderItemID)) {
                    String foodID = orderItemDetails.get(1);
                    String foodName = foodNameMap.get(foodID);

                    orderItemDetails.set(1, foodName);
                    model.addRow(orderItemDetails.toArray());
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //assign runner for delivery
    public void assignRunnerForDelivery(String orderID) {
        try {
            String serviceType = getServiceTypeForOrder(orderID);
            if ("Delivery".equals(serviceType)) {
                int vendorID = getVendorUserIdByUsername(username);

                List<String> deliveryRunnerIDs = getUsersWithRole("DeliveryRunner");
                if (!deliveryRunnerIDs.isEmpty()) {
                    Random random = new Random();
                    String randomDeliveryRunnerID = deliveryRunnerIDs.get(random.nextInt(deliveryRunnerIDs.size()));
                    int runnerID = Integer.parseInt(randomDeliveryRunnerID);

                    int newTaskID = generateNewTaskID(runnerTaskFilePath);

                    BufferedWriter runnerTaskWriter = new BufferedWriter(new FileWriter(runnerTaskFilePath, true));
                    String newRunnerTask = newTaskID + "," + randomDeliveryRunnerID + "," + orderID + "," + vendorID + ",Pending";
                    runnerTaskWriter.write(newRunnerTask);
                    runnerTaskWriter.newLine();
                    runnerTaskWriter.close();
                    
                    generateTaskAssignedNotification(runnerID);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //generate task assigned notification
    public void generateTaskAssignedNotification(int userId) {
        String dateTime = dt.getCurrentDateTime();
        String content = "You have received a new delivery task";
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

    //update order status
    public void updateOrderStatus(String orderID, String newStatus) {
        try {
            List<String> updatedOrders = new ArrayList<>();
            BufferedReader orderReader = new BufferedReader(new FileReader(orderFilePath));
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] orderData = orderLine.split(",");
                if (orderData[0].equals(orderID)) {
                    orderData[4] = newStatus; 
                }
                updatedOrders.add(String.join(",", orderData));
            }
            orderReader.close();

            BufferedWriter orderWriter = new BufferedWriter(new FileWriter(orderFilePath));
            for (String updatedOrder : updatedOrders) {
                orderWriter.write(updatedOrder);
                orderWriter.newLine();
            }
            orderWriter.close();
            generateUpdateStatusNotification(orderID, newStatus);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //generate update status notification
    public void generateUpdateStatusNotification(String orderID, String newStatus) {
        String customerUserID = getCustomerUserID(orderID);
        int userID = Integer.parseInt(customerUserID);
        String dateTime = dt.getCurrentDateTime();
        String content = "Order ID: " + orderID + " has been " + newStatus + " by Vendor";
        String category = "Order";

        String notifications = String.format("%d,%s,%s,%s",
                userID, content, dateTime, category);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Database/notifications.txt", true))) {
            bw.write(notifications);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get users role
    public List<String> getUsersWithRole(String role) {
        List<String> userIDs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 4 && role.equals(userData[3].trim())) {
                    userIDs.add(userData[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userIDs;
    }

    //get orderamount from orderid
    public String getOrderAmount(String orderID) {
        try (BufferedReader orderReader = new BufferedReader(new FileReader(orderFilePath))) {
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] orderData = orderLine.split(",");
                if (orderData[0].equals(orderID)) {
                    return orderData[3]; 
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "0.0"; 
    }

    //get vendorid from orderid
    public String getVendorID(String orderID) {
        try (BufferedReader orderReader = new BufferedReader(new FileReader(orderFilePath))) {
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] orderData = orderLine.split(",");
                if (orderData[0].equals(orderID)) {
                    return orderData[6]; 
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "-1"; 
    }
    
    //get customerid from orderid
    public String getCustomerUserID(String orderID) {
        try (BufferedReader orderReader = new BufferedReader(new FileReader(orderFilePath))) {
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] orderData = orderLine.split(",");
                if (orderData[0].equals(orderID)) {
                    return orderData[5].trim();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "-1"; 
    }

    //get service type for order
    public String getServiceTypeForOrder(String orderID) throws IOException {
        try (BufferedReader orderReader = new BufferedReader(new FileReader(orderFilePath))) {
            String orderLine;
            while ((orderLine = orderReader.readLine()) != null) {
                String[] orderData = orderLine.split(",");
                if (orderData.length >= 1 && orderData[0].equals(orderID)) {
                    return orderData[7]; 
                }
            }
        }
        return ""; 
    }

    //create credit transaction
    public void createCreditTransaction(String orderID, String orderAmount) {
        try {
            String transactionID = generateNewTransactionID();
            String customerUserID = getCustomerUserID(orderID);
            String vendorUserID = getVendorID(orderID);
            String currentDateTime = dt.getCurrentDateTime();

            BufferedWriter transactionWriter = new BufferedWriter(new FileWriter(creditTransactionFilePath, true));
            String newTransaction = transactionID + "," + customerUserID + "," + orderAmount + ","
                    + currentDateTime + ",Debit,Payment Refunded";
            transactionWriter.write(newTransaction);
            transactionWriter.newLine();

            int lastTransactionID = Integer.parseInt(transactionID.substring(transactionID.length() - 5)) + 1;
            String lastTransactionId = String.format("T%05d", lastTransactionID);
            String creditTransaction = lastTransactionId + "," + vendorUserID + "," + orderAmount + ","
                    + currentDateTime + ",Credit,Vendor Refunded";
            transactionWriter.write(creditTransaction);
            transactionWriter.newLine();
            transactionWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //generate new transaction id
    public String generateNewTransactionID() {
        String lastTransactionID = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(creditTransactionFilePath));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                lastTransactionID = line.split(",")[0];
            }
            if (lastTransactionID != null) {
                int transactionID = Integer.parseInt(lastTransactionID.substring(lastTransactionID.length() - 5)) + 1;
                lastTransactionID = String.format("T%05d", transactionID);
            } else {
                lastTransactionID = "T00001";
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lastTransactionID;
    }

    //generate new taskid
    public int generateNewTaskID(String filePath) throws IOException {
        int newTaskID = 1; 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();

            while (reader.readLine() != null) {
                newTaskID++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return newTaskID;
    }

    //update customer and vendor credit
    public void updateCustomerandVendorCredit(String customerUserID, int vendorUserID, double totalAmount, Boolean flag) {
        double customerUpdatedCredit = getCustomerUpdatedCredit(customerUserID, totalAmount, flag);
        double vendorUpdatedCredit = getVendorUpdatedCredit(vendorUserID, totalAmount, flag);
        updateCreditInFile(customerUserID, customerUpdatedCredit);
        String vendorID = String.valueOf(vendorUserID);
        updateCreditInFile(vendorID, vendorUpdatedCredit);
    }
    
    //get customer updated credit
    public double getCustomerUpdatedCredit(String customerID, double totalAmount, Boolean flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (flag) {
                    if (fields.length > 0 && fields[0].equals(customerID)) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit + totalAmount;
                        return updatedCredit;
                    }
                } else {
                    if (fields.length > 0 && fields[0].equals(customerID)) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit - totalAmount;
                        return updatedCredit;
                    }
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //get vendor updated credit
    private double getVendorUpdatedCredit(int vendorID, double totalAmount, Boolean flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (flag) {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == vendorID) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit - totalAmount;
                        return updatedCredit;
                    }
                } else {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == vendorID) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit + totalAmount;
                        return updatedCredit;
                    }
                }
            }
            reader.close();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //update credit in file
    public void updateCreditInFile(String userID, double updatedCredit) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 0 && userData[0].equals(userID)) {
                    userData[4] = String.valueOf(updatedCredit);
                }
                String modifiedLine = String.join(",", userData);
                content.append(modifiedLine).append("\n");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
                writer.write(content.toString());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Or log the error
        }
    }

    //get order status from file
    public List<List<String>> getOrderStatusFromFile(String status) {
        List<List<String>> ordersWithStatus = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(orderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderDetails = line.split(",");
                if (orderDetails.length > 4 && orderDetails[4].equals(status)) {
                    List<String> orderItemList = Arrays.asList(orderDetails);
                    ordersWithStatus.add(orderItemList); // Add the order as a list of strings
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ordersWithStatus;
    }

    //filter order by date
    public List<List<String>> filterOrderByDateInterval(List<List<String>> orderItems, String selectedDateInterval) {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime orderTime;
        List<List<String>> filteredOrders = new ArrayList<>();

        for (List<String> orderItem : orderItems) {
            orderTime = LocalDateTime.parse(orderItem.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            switch (selectedDateInterval) {
                case "All":
                    // No date filtering needed, add all orders
                    filteredOrders.add(orderItem);
                    break;
                case "Daily":
                    // Compare orderTime with today's date
                    if (orderTime.toLocalDate().equals(current.toLocalDate())) {
                        filteredOrders.add(orderItem);
                    }
                    break;
                case "Monthly":
                    // Compare orderTime with this month's date
                    if (orderTime.getMonth().equals(current.getMonth()) && orderTime.getYear() == current.getYear()) {
                        filteredOrders.add(orderItem);
                    }
                    break;
                case "Quarterly":
                    // Compare orderTime with current year and quarter
                    if (orderTime.getYear() == current.getYear()) {
                        int orderQuarter = (orderTime.getMonthValue() - 1) / 3 + 1;
                        int currentQuarter = (current.getMonthValue() - 1) / 3 + 1;

                        if (orderQuarter == currentQuarter) {
                            filteredOrders.add(orderItem);
                        }
                    }
                    break;
                case "Yearly":
                    // Compare orderTime year with current year
                    if (orderTime.getYear() == current.getYear()) {
                        filteredOrders.add(orderItem);
                    }
                    break;
            }
        }
        return filteredOrders;
    }
}
