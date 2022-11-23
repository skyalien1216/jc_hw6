import dao.ProductDAO;
import generated.tables.pojos.Product;
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

class ProductDAOTest {

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

        dao = new ProductDAO();
    }
    private static ProductDAO dao;

    @Test
    void get() {
        assertEquals(new Product(1, "table"), dao.get(1));
    }

    @Test
    void all() {
        var list = new ArrayList<Product>();
        list.add(new Product(1, "table"));
        list.add(new Product(2, "chair"));
        list.add(new Product(3, "spoon"));
        assertEquals(list, dao.all());
    }

   @Test
    void save() {
        var p = new Product(101, "test");
        dao.save(p);
        assertEquals(p, dao.get(101));
        dao.delete(p);
    }

    @Test
    void update() {
        var p = new Product(101, "test");
        dao.save(p);
        p.setName("test11");
        dao.update(p);
        assertEquals(p, dao.get(101));
        dao.delete(p);
    }

    @Test
    void delete() {
    var p = new Product(101, "test");
        dao.save(p);
        assertEquals(p, dao.get(101));
        dao.delete(p);
        assertThrows(IllegalStateException.class , ()-> {dao.get(101);});
    }

   @AfterAll
   public static void dropDB(){
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