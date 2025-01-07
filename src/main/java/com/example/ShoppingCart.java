package com.example;
import java.util.*;
import java.text.*;

/** Контейнер для товарів та розрахунку ціни */
public class ShoppingCart {

    public static enum ItemType { NEW, REGULAR, SECOND_FREE, SALE };

    private final List<Item> items = new ArrayList<>();

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Apple", 0.99, 5, ItemType.NEW);
        cart.addItem("Banana", 20.00, 4, ItemType.SECOND_FREE);
        cart.addItem("A long piece of toilet paper", 17.20, 1, ItemType.SALE);
        cart.addItem("Nails", 2.00, 500, ItemType.REGULAR);
        System.out.println(cart.formatTicket());
    }

    public void addItem(String title, double price, int quantity, ItemType type) {
        if (title == null || title.length() == 0 || title.length() > 32)
            throw new IllegalArgumentException("Illegal title");
        if (price < 0.01)
            throw new IllegalArgumentException("Illegal price");
        if (quantity <= 0)
            throw new IllegalArgumentException("Illegal quantity");
        items.add(new Item(title, price, quantity, type)); 
    }

    public String formatTicket() {
        return getFormattedTicketTable(0.00);
    }

    private String getFormattedTicketTable(double total) {
        if (items.size() == 0)
            return "No items."; 

        List<String[]> lines = convertItemsToTableLines(total);
        String[] header = {"#", "Item", "Price", "Quan.", "Discount", "Total"};
        int[] align = {1, -1, 1, 1, 1, 1};
        String[] footer = {String.valueOf(lines.size()), "", "", "", "", MONEY.format(total)};

        int[] width = calculateColumnWidths(lines, header, footer);
        int lineLength = calculateLineLength(width);
        StringBuilder sb = new StringBuilder();

        appendFormattedLine(sb, header, align, width, true);
        appendSeparator(sb, lineLength);
        for (String[] line : lines) {
            appendFormattedLine(sb, line, align, width, true);
        }
        if (!lines.isEmpty()) {
            appendSeparator(sb, lineLength);
        }
        appendFormattedLine(sb, footer, align, width, false);

        return sb.toString();
    }

    private List<String[]> convertItemsToTableLines(double total) {
        int index = 0;
        List<String[]> lines = new ArrayList<>();
        for (Item item : items) {
            int discount = calculateDiscount(item.getType(), item.getQuantity());
            double itemTotal = item.getPrice() * item.getQuantity() * (100.00 - discount) / 100.00;
            lines.add(new String[]{String.valueOf(++index), item.getTitle(), MONEY.format(item.getPrice()), String.valueOf(item.getQuantity()), 
                discount == 0 ? "-" : discount + "%", MONEY.format(itemTotal)});
            total += itemTotal;
        }
        return lines;
    }

    private int[] calculateColumnWidths(List<String[]> lines, String[]... additionalLines) {
        int[] width = new int[]{0,0,0,0,0,0};
        for (String[] line : lines) {
            adjustColumnWidth(width, line);
        }
        for (String[] line : additionalLines) {
            adjustColumnWidth(width, line);
        }
        return width;
    }

    private int calculateLineLength(int[] width) {
        int lineLength = width.length - 1;
        for (int w : width)
            lineLength += w;
        return lineLength;
    }

    private void appendSeparator(StringBuilder sb, int lineLength) {
        for (int i = 0; i < lineLength; i++)
            sb.append("-");
        sb.append("\n");
    }

    private void adjustColumnWidth(int[] width, String[] columns) {
        for (int i = 0; i < width.length; i++)
            width[i] = Math.max(width[i], columns[i].length());
    }

    private void appendFormattedLine(StringBuilder sb, String[] line, int[] align, int[] width, boolean newLine) {
        for (int i = 0; i < line.length; i++)
            appendFormatted(sb, line[i], align[i], width[i]);
        if (newLine)
            sb.append("\n");
    }

    private static final NumberFormat MONEY;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        MONEY = new DecimalFormat("$#.00", symbols);
    }

    public static void appendFormatted(StringBuilder sb, String value, int align, int width) {
        if (value.length() > width)
            value = value.substring(0, width); 

        int before = 0, after = 0;
        if (align == 0) {
            before = (width - value.length()) / 2;
            after = width - value.length() - before;
        } else if (align == 1) { 
            before = width - value.length();
        } else { 
            after = width - value.length();
        }

        for (int i = 0; i < before; i++)
            sb.append(" ");
        sb.append(value);
        for (int i = 0; i < after; i++)
            sb.append(" ");
    }

    public static int calculateDiscount(ItemType type, int quantity) {
        int discount = 0;
        switch (type) {
            case NEW: return 0;
            case REGULAR: discount = 0; break;
            case SECOND_FREE:
                if (quantity > 1) discount = 50;
                break;
            case SALE: discount = 70; break;
        }
        if (discount < 80)
            discount += quantity / 10;
        return Math.min(discount, 80);
    }

    private static class Item {
        private final String title;
        private final double price;
        private final int quantity;
        private final ItemType type;
        private double totalPrice;

        public Item(String title, double price, int quantity, ItemType type) {
            this.title = title;
            this.price = price;
            this.quantity = quantity;
            this.type = type;
            this.totalPrice = price * quantity;
        }

        public String getTitle() { return title; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public ItemType getType() { return type; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    }
}
