import dao.InvoicePositionsDAO;
import generated.tables.pojos.InvoicePositions;
import org.flywaydb.core.Flyway;
import org.java_courses.TCREDS;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static generated.Tables.*;
import static generated.tables.Product.PRODUCT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoicePositionsDAOTest {

    @BeforeAll
    static void prepare(){
        final Flyway flyway = Flyway
                .configure().cleanDisabled(false)
                .dataSource("jdbc:postgresql://localhost/" + TCREDS.dbName, TCREDS.user, TCREDS.password)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Migrations applied successfully");

        dao = new InvoicePositionsDAO();
    }

    private static InvoicePositionsDAO dao;

    @Test
    void get() {
        var i = new InvoicePositions(1, 1, 50, 20, 1);
        assertEquals(i, dao.get(1));
    }

    @Test
    void all() {
        var list = new ArrayList<InvoicePositions>();

        list.add(new InvoicePositions(1, 1, 50, 20, 1));
        list.add(new InvoicePositions(2, 1, 30, 3, 2));
        list.add(new InvoicePositions(3, 1, 27, 5, 3));
        list.add(new InvoicePositions(4, 2, 20, 12, 2));
        list.add(new InvoicePositions(5, 2, 20, 6, 3));
        list.add(new InvoicePositions(6, 3, 45, 15, 1));
        list.add(new InvoicePositions(7, 3, 25, 10, 2));
        list.add(new InvoicePositions(8, 4, 47, 10, 3));
        list.add(new InvoicePositions(9, 5, 55, 5, 1));
        list.add(new InvoicePositions(10, 6, 40, 10, 2));
        list.add(new InvoicePositions(11, 7, 35, 7, 2));
        list.add(new InvoicePositions(12, 8, 70, 2, 1));
        list.add(new InvoicePositions(13, 9, 65, 5, 1));
        list.add(new InvoicePositions(14, 10, 60, 10, 1));
        list.add(new InvoicePositions(15, 11, 45, 10, 2));
        list.add(new InvoicePositions(16, 12, 20, 20, 3));
        list.add(new InvoicePositions(17, 13, 20, 30, 2));
        list.add(new InvoicePositions(18, 13, 20, 10, 3));
        list.add(new InvoicePositions(19, 14, 15, 40, 2));
        list.add(new InvoicePositions(20, 14, 20, 10, 1));
        list.add(new InvoicePositions(21, 15, 30, 5, 3));

        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        var ip = new InvoicePositions( 101, 1, 10, 1, 1);
        dao.save(ip);
        assertEquals(ip, dao.get(101));
        dao.delete(ip);
    }

    @Test
    void update() {
        var ip = new InvoicePositions( 101, 1, 10, 1, 1);
        dao.save(ip);
        ip.setAmount(20);
        dao.update(ip);
        assertEquals(ip, dao.get(101));
        dao.delete(ip);
    }

    @Test
    void delete() {
        var ip = new InvoicePositions( 101, 1, 10, 1, 1);
        dao.save(ip);
        assertEquals(ip, dao.get(101));
        dao.delete(ip);
        assertThrows(IllegalStateException.class , ()-> {dao.get(101);});
    }

    @AfterAll
    public static void dropDB() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + TCREDS.dbName, TCREDS.user, TCREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context.dropTable(INVOICE).cascade().execute();
            context.dropTable(PRODUCT).cascade().execute();
            context.dropTable(INVOICE_POSITIONS).cascade().execute();
            context.dropTable(ORGANIZATION).cascade().execute();
        } catch (SQLException e) {
            System.out.println("Cleaning after test failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
