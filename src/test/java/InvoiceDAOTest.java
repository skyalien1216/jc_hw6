import dao.InvoiceDAO;
import generated.tables.pojos.Invoice;
import org.flywaydb.core.Flyway;
import org.java_courses.TCREDS;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static generated.Tables.*;
import static generated.tables.Product.PRODUCT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoiceDAOTest {

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

        dao = new InvoiceDAO();
    }

    private static InvoiceDAO dao;

    @Test
    void get() {
        var i = new Invoice(1, LocalDate.of(2019, 1,1), 1);
        assertEquals(i, dao.get(1));
    }

    @Test
    void all() {
        var list = new ArrayList<Invoice>();
        list.add(new Invoice(1, LocalDate.of(2019,1,1),1));
        list.add(new Invoice(2, LocalDate.of(2019,1,2),1));
        list.add(new Invoice(3, LocalDate.of(2019,1,1),2));
        list.add(new Invoice(4, LocalDate.of(2019,1,2),2));
        list.add(new Invoice(5, LocalDate.of(2019,1,3),2));
        list.add(new Invoice(6, LocalDate.of(2019,1,1),3));
        list.add(new Invoice(7, LocalDate.of(2019,1,3),3));
        list.add(new Invoice(8, LocalDate.of(2019,1,2),4));
        list.add(new Invoice(9, LocalDate.of(2019,1,3),4));
        list.add(new Invoice(10, LocalDate.of(2019,1,1),5));
        list.add(new Invoice(11, LocalDate.of(2019,1,2),5));
        list.add(new Invoice(12, LocalDate.of(2019,1,3),5));
        list.add(new Invoice(13, LocalDate.of(2019,1,1),6));
        list.add(new Invoice(14, LocalDate.of(2019,1,2),6));
        list.add(new Invoice(15, LocalDate.of(2019,1,3),6));
        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        var i = new Invoice(101, new Date(119, 0,2).toLocalDate(), 1);
        dao.save(i);
        assertEquals(i ,dao.get(101) );
        dao.delete(i);
    }

    @Test
    void update() {
        var i = new Invoice(101, new Date(119, 0,2).toLocalDate(), 1);
        dao.save(i);
        i.setOrganizationId(3);
        dao.update(i);
        assertEquals(i ,dao.get(101));
        dao.delete(i);
    }

    @Test
    void delete() {
        var i = new Invoice(101, new Date(119, 0,2).toLocalDate(), 1);
        dao.save(i);
        assertEquals(i ,dao.get(101));
        dao.delete(i);
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