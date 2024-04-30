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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Goh Ee Cheng
 */
public class CustomerCredit extends UserCustomer {

    // File Path
    private static final String userFilePath = "src/Database/users.txt";
    private static final String creditTransactionFilePath = "src/Database/credit_transaction.txt";

    public CustomerCredit(int id, String username, String password, String role, double credit) {
        super(id, username, password, role, credit);
    }

    public CustomerCredit() {

    }

    // get Credit of Customer
    public double getCustomerCredit(String userName) {
        double credit = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creditData = line.split(",");
                String customerName = creditData[1];
                if (customerName.equals(userName)) {
                    credit = Double.parseDouble(creditData[4].trim());
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credit;
    }

    // check credit
    public boolean checkCredit(double totalAmount, String username) {
        // get Customer Credit 
        double availableCredit = getCustomerCredit(username);
        return availableCredit >= totalAmount;
    }

    // update customer and vendor credit after placing an order
    public void updateCustomerandVendorCredit(int customerUserID, int vendorUserID, double totalAmount, Boolean flag) {
        double customerUpdatedCredit = getCustomerUpdatedCredit(customerUserID, totalAmount, flag);
        double vendorUpdatedCredit = getVendorUpdatedCredit(vendorUserID, totalAmount, flag);
        updateCreditInFile(customerUserID, customerUpdatedCredit);
        updateCreditInFile(vendorUserID, vendorUpdatedCredit);
    }

    // updated credit for a customer
    private double getCustomerUpdatedCredit(int customerID, double totalAmount, Boolean flag) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (flag) {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == customerID) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit - totalAmount;
                        return updatedCredit;
                    }
                } else {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == customerID) {
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

    // updated credit for a vendor
    private double getVendorUpdatedCredit(int vendorID, double totalAmount, Boolean flag) {
        try {
            // Read the content of the file
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (flag) {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == vendorID) {
                        double originalCredit = Double.parseDouble(fields[4]);
                        double updatedCredit = originalCredit + totalAmount;
                        return updatedCredit;
                    }
                } else {
                    if (fields.length > 0 && Integer.parseInt(fields[0]) == vendorID) {
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

    // update credit information in the user file
    public void updateCreditInFile(int userID, double updatedCredit) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0 && Integer.parseInt(fields[0]) == userID) {
                    fields[4] = String.valueOf(updatedCredit);
                }
                String modifiedLine = String.join(",", fields);
                content.append(modifiedLine).append("\n");
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath));
            writer.write(content.toString());
            // Close the writer
            writer.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // write customer transaction data after placing an order into credit_transaction.txt file
    public void generateOrderTransactionData(String transactionID, int customerID, int vendorID, double transactionAmount, String dateTime, String serviceType, int orderID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(creditTransactionFilePath, true))) {
            String transactionCustomerData = transactionID + "," + customerID + "," + transactionAmount + "," + dateTime + "," + "Credit" + "," + "Payment for Order ID: " + orderID;
            writer.write(transactionCustomerData);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int lastTransactionID = Integer.parseInt(transactionID.substring(transactionID.length() - 5)) + 1;
        String lastTransactionId = String.format("T%05d", lastTransactionID);
        String transactionVendorData = lastTransactionId + "," + vendorID + "," + transactionAmount + "," + dateTime + "," + "Debit" + "," + "Payment Received for Order ID: " +orderID;
        generateVendorTransactionData(transactionVendorData);
    }

    // write customer transaction data after cancelling an order into credit_transaction.txt file 
    public void generateOrderCancelledTransactionData(int customerID, int vendorID, double transactionAmount) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        String transactionID = generateLastTransactionID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(creditTransactionFilePath, true))) {
            String transactionCustomerData = transactionID + "," + customerID + "," + transactionAmount + "," + currentTime + "," + "Debit" + "," + "Order Cancellation Refund";
            writer.write(transactionCustomerData);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int lastTransactionID = Integer.parseInt(transactionID.substring(transactionID.length() - 5)) + 1;
        String lastTransactionId = String.format("T%05d", lastTransactionID);
        String transactionVendorData = lastTransactionId + "," + vendorID + "," + transactionAmount + "," + currentTime + "," + "Credit" + "," + "Vendor Refunded";
        generateVendorTransactionData(transactionVendorData);
    }

    // write vendor transaction data into credit_transaction.txt file
    private void generateVendorTransactionData(String transactionVendorData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(creditTransactionFilePath, true))) {
            writer.write(transactionVendorData);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // generate Last TransactionID
    public String generateLastTransactionID() {
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
                // If no line is found, set the default value
                lastTransactionID = "T00001";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerCredit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastTransactionID;
    }

    // get transaction history data 
    public List<List<String>> getTransactionHistoryData(String customerUserID, String transactionTypeFilter, String transactionDateTimeFilter) {
        List<List<String>> transactionHistoryData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(creditTransactionFilePath));
            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionHistory = line.split(",");
                if (transactionHistory[1].equals(customerUserID)
                        && (transactionTypeFilter.equals("All") || transactionHistory[4].equals(transactionTypeFilter))
                        && isValidDate(transactionHistory[3], transactionDateTimeFilter)) {
                    List<String> transactionHistoryInfo = new ArrayList<>();
                    transactionHistoryInfo.add(transactionHistory[0]); // transactionID
                    transactionHistoryInfo.add(transactionHistory[2]); // transaction Amount
                    transactionHistoryInfo.add(transactionHistory[3]); // transaction Date Time
                    transactionHistoryInfo.add(transactionHistory[4]); // transaction Type
                    transactionHistoryInfo.add(transactionHistory[5]); // transaction Remark
                    transactionHistoryData.add(transactionHistoryInfo);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerCredit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerCredit.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transactionHistoryData;
    }

    private boolean isValidDate(String transactionDateTime, String transactionDateTimeFilter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date transactionDate;
        try {
            transactionDate = sdf.parse(transactionDateTime);
            switch (transactionDateTimeFilter) {
                case "All":
                    return true;
                case "Daily":
                    return isSameDay(transactionDate, new Date());
                case "Monthly":
                    return isSameMonth(transactionDate, new Date());
                case "Yearly":
                    return isSameYear(transactionDate, new Date());
                default:
                    return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).equals(sdf.format(date2));
    }

    private boolean isSameMonth(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date1).equals(sdf.format(date2));
    }

    private boolean isSameYear(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date1).equals(sdf.format(date2));
    }

}
