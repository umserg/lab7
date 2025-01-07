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
        Item item = new Item();
        item.title = title;
        item.price = price;
        item.quantity = quantity;
        item.type = type;
        items.add(item);
    }

    public String formatTicket() {
        if (items.size() == 0)
            return "No items.";
        List<String[]> lines = new ArrayList<>();
        String[] header = {"#","Item","Price","Quan.","Discount","Total"};
        int[] align = { 1, -1, 1, 1, 1, 1 };
        double total = 0.00;
        int index = 0;
        for (Item item : items) {
            int discount = calculateDiscount(item.type, item.quantity);
            double itemTotal = item.price * item.quantity * (100.00 - discount) / 100.00;
            lines.add(new String[]{
                    String.valueOf(++index),
                    item.title,
                    MONEY.format(item.price),
                    String.valueOf(item.quantity),
                    (discount == 0) ? "-" : (String.valueOf(discount) + "%"),
                    MONEY.format(itemTotal)
            });
            total += itemTotal;
        }
        String[] footer = { String.valueOf(index),"","","","",
                MONEY.format(total) };
        int[] width = {0,0,0,0,0,0};
        for (String[] line : lines) {
            adjustColumnWidth(width, line);
        }
        adjustColumnWidth(width, header);
        adjustColumnWidth(width, footer);

        int lineLength = width.length - 1;
        for (int w : width)
            lineLength += w;
        StringBuilder sb = new StringBuilder();

        // Відформатований заголовок
        appendFormattedLine(sb, header, align, width, true);
        appendSeparator(sb, lineLength);

        // Відформатовані рядки
        for (String[] line : lines) {
            appendFormattedLine(sb, line, align, width, true);
        }

        // Відформатований розділювач
        if (lines.size() > 0) {
            appendSeparator(sb, lineLength);
        }

        // Відформатований підсумок
        appendFormattedLine(sb, footer, align, width, false);

        return sb.toString();
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

        int before = 0;
        int after = 0;

        switch (align) {
            case 0: // Центрований вирівнювання
                before = (width - value.length()) / 2;
                after = width - value.length() - before;
                break;
            case 1: // Вирівнювання праворуч
                before = width - value.length();
                after = 0;
                break;
            case -1: // Вирівнювання ліворуч
                before = 0;
                after = width - value.length();
                break;
        }

        for (int i = 0; i < before; i++) {
            sb.append(" ");
        }

        sb.append(value);

        for (int i = 0; i < after; i++) {
            sb.append(" ");
        }
    }

    public static int calculateDiscount(ItemType type, int quantity) {
        int discount = 0;
        switch (type) {
            case NEW:
                return 0;
            case REGULAR:
                discount = 0;
                break;
            case SECOND_FREE:
                if (quantity > 1)
                    discount = 50;
                break;
            case SALE:
                discount = 70;
                break;
        }
        if (discount < 80) {
            discount += quantity / 10;
            if (discount > 80)
                discount = 80;
        }
        return discount;
    }

    private static class Item {
        String title;
        double price;
        int quantity;
        ItemType type;
    }
}