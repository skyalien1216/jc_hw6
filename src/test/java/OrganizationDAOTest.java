import dao.OrganizationDAO;
import generated.tables.pojos.Organization;
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

class OrganizationDAOTest {

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

        dao = new OrganizationDAO();
    }

    private static OrganizationDAO dao;

    @Test
    void get() {
        var o = new Organization(1, "org1", null);
        assertEquals(o, dao.get(1));
    }

    @Test
    void all() {
        var list = new ArrayList<Organization>();
        list.add(new Organization(1, "org1", null));
        list.add(new Organization(2, "org2", null));
        list.add(new Organization(3, "org3", null));
        list.add(new Organization(4, "org4", null));
        list.add(new Organization(5, "org5", null));
        list.add(new Organization(6, "org6", null));
        list.add(new Organization(7, "org7", null));
        list.add(new Organization(8, "org8", null));
        list.add(new Organization(9, "org9", null));
        list.add(new Organization(10, "org10", null));
        list.add(new Organization(11, "org11", null));
        assertEquals(list, dao.all());
    }


   @Test
    void save() {
       var o = new Organization(101, "test", null);
       dao.save(o);
       assertEquals(o, dao.get(101));
       dao.delete(o);
   }

    @Test
    void update() {
        var o = new Organization(101, "test", null);
        dao.save(o);
        o.setName("test11");
        dao.update(o);
        assertEquals(o, dao.get(101));
        dao.delete(o);
    }

    @Test
    void delete() {
        var o = new Organization(101, "test", null);
        dao.save(o);
        assertEquals(o, dao.get(101));
        dao.delete(o);
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