package com.example;
import org.junit.*;
import static org.junit.Assert.*;

public class ShoppingCartTest {

    @Test
    public void testAppendFormatted() {
        StringBuilder sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 14);
        System.out.println("'" + sb.toString() + "'"); 
        assertEquals("   SomeLine   ", sb.toString());
        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 15);
        System.out.println("'" + sb.toString() + "'"); 
        assertEquals("   SomeLine    ", sb.toString());
        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 5);
        System.out.println("'" + sb.toString() + "'"); 
        assertEquals("SomeL", sb.toString());
        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 1, 14);
        System.out.println("'" + sb.toString() + "'"); 
        assertEquals("      SomeLine", sb.toString());
        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", -1, 14);
        System.out.println("'" + sb.toString() + "'"); 
        assertEquals("SomeLine      ", sb.toString());
    }

    @Test
    public void testCalculateDiscount() {
        assertEquals(80, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SALE, 500));
        assertEquals(73, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SALE, 30));
        assertEquals(71, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SALE, 10));
        assertEquals(70, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SALE, 9));
        assertEquals(70, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SALE, 1));
        assertEquals(0, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.NEW, 20));
        assertEquals(0, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.NEW, 10));
        assertEquals(0, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.NEW, 1));
        assertEquals(80, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 500));
        assertEquals(53, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 30));
        assertEquals(51, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 10));
        assertEquals(50, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 9));
        assertEquals(50, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 2));
        assertEquals(0, ShoppingCart.calculateDiscount(
                ShoppingCart.ItemType.SECOND_FREE, 1));
    }
}
