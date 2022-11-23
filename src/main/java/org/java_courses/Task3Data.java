package org.java_courses;

import generated.tables.pojos.Product;

import java.time.LocalDate;
import java.util.Objects;

public class Task3Data {
    private Product product;
    private LocalDate date;
    private int sum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task3Data task3Data)) return false;
        return getSum() == task3Data.getSum() && getAmount() == task3Data.getAmount() && getProduct().equals(task3Data.getProduct()) && Objects.equals(getDate(), task3Data.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getDate(), getSum(), getAmount());
    }

    private int amount;

    public Task3Data(Product product, LocalDate date, int sum, int amount) {
        this.product = product;
        this.date = date;
        this.sum = sum;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Task3Data{" +
                "product=" + product +
                ", date=" + date +
                ", sum=" + sum +
                ", amount=" + amount +
                '}';
    }
}
