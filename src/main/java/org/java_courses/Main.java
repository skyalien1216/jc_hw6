package org.java_courses;

import dao.InvoiceDAO;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        final Flyway flyway = Flyway
                .configure()
                .dataSource("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)
                .locations("db")
                .load();

        flyway.migrate();
        System.out.println("Migrations applied successfully");

        System.out.println(new InvoiceDAO().all());
    }
}