
import generated.tables.pojos.Organization;
import generated.tables.pojos.Product;
import javafx.util.Pair;
import org.flywaydb.core.Flyway;
import org.java_courses.SQLTasks;
import org.java_courses.TCREDS;
import org.java_courses.Task3Data;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static generated.Tables.*;
import static generated.tables.Product.PRODUCT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLTasksTest {

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

        sqlTasks = new SQLTasks();
    }

    private static SQLTasks sqlTasks;

    @Test
    void task1() {
        var list = new ArrayList<Pair<Organization, Integer>>();
        list.add(new Pair<>(new Organization(6, "org6", null), 95));
        list.add(new Pair<>(new Organization(1, "org1", null), 46));
        list.add(new Pair<>(new Organization(2, "org2", null), 40));
        list.add(new Pair<>(new Organization(5, "org5", null), 40));
        list.add(new Pair<>(new Organization(3, "org3", null), 17));
        list.add(new Pair<>(new Organization(4, "org4", null), 7));

        assertEquals(list, sqlTasks.task1());
    }

    @Test
    void task2() {
        var params = new ArrayList<Pair<Product, Integer>>();
        params.add(new Pair<>(new Product(1,"table"), 10));
        params.add(new Pair<>(new Product(2,"chair"), 5));

        var list = new ArrayList<Organization>();
        list.add(new Organization(2, "org2", null));
        list.add(new Organization(1, "org1", null));
        assertEquals(list, sqlTasks.task2(params));
    }

    @Test
    void task3() {
        var res = sqlTasks.task3(new Date(118, 1, 1), new Date(119, 4, 1));
        var list1 = new ArrayList<Task3Data>();
        list1.add(new Task3Data(new Product(1, "table"), LocalDate.of(2019,1,1), 2275, 45));
        list1.add(new Task3Data(new Product(1, "table"), LocalDate.of(2019,1,2), 340, 12));
        list1.add(new Task3Data(new Product(1, "table"), LocalDate.of(2019,1,3), 600, 10));
        list1.add(new Task3Data(new Product(2, "chair"), LocalDate.of(2019,1,1), 1340, 53));
        list1.add(new Task3Data(new Product(2, "chair"), LocalDate.of(2019,1,2), 1290, 62));
        list1.add(new Task3Data(new Product(2, "chair"), LocalDate.of(2019,1,3), 245, 7));
        list1.add(new Task3Data(new Product(3, "spoon"), LocalDate.of(2019,1,1), 335, 15));
        list1.add(new Task3Data(new Product(3, "spoon"), LocalDate.of(2019,1,2), 590, 16));
        list1.add(new Task3Data(new Product(3, "spoon"), LocalDate.of(2019,1,3), 550, 25));

        assertEquals(list1, res.getKey());
        var list2 = new ArrayList<Task3Data>();
        list2.add(new Task3Data(new Product(1, "table"), null, 3215, 67));
        list2.add(new Task3Data(new Product(2, "chair"), null, 2875, 122));
        list2.add(new Task3Data(new Product(3, "spoon"), null, 1475, 56));

        assertEquals(list2, res.getValue());
    }

    @Test
    void task4() {
        var res = sqlTasks.task4(new Date(118, 1, 1), new Date(119,4,1));
        var list = new ArrayList<Pair<Product, BigDecimal>>();
        list.add(new Pair<>(new  Product(1, "table"), BigDecimal.valueOf(52)));
        list.add(new Pair<>(new  Product(2, "chair"), BigDecimal.valueOf(28)));
        list.add(new Pair<>(new  Product(3, "spoon"), BigDecimal.valueOf(27)));
        assertEquals(list, res);
    }

    @Test
    void task5() {
        var res = sqlTasks.task5(new Date(118, 1, 1), new Date(119,0,3));
        var map = new HashMap<Organization, List<Product>>();
        map.put(new Organization(1,"org1",null), List.of(new Product(2,"chair"),new Product(1, "table"),new Product(3, "spoon")));
        map.put(new Organization(2,"org2",null), List.of(new Product(2, "chair"), new Product(1, "table"),new Product(3, "spoon")));
        map.put(new Organization(3,"org3",null), List.of(new Product(2, "chair")));
        map.put(new Organization(4,"org4",null), List.of(new Product(1, "table")));
        map.put(new Organization(5,"org5",null), List.of(new Product(2, "chair"), new Product(1, "table")));
        map.put(new Organization(6,"org6",null), List.of(new Product(3, "spoon"), new Product(1, "table"), new Product(2, "chair")));
        map.put(new Organization(7,"org7",null), List.of(new Product(null, null)));
        map.put(new Organization(8,"org8",null), List.of(new Product(null, null)));
        map.put(new Organization(9,"org9",null), List.of(new Product(null, null)));
        map.put(new Organization(10,"org10",null), List.of(new Product(null, null)));
        map.put(new Organization(11,"org11",null), List.of(new Product(null, null)));

        assertEquals(map, res);
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