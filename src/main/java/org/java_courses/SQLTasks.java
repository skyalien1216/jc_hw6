package org.java_courses;

import generated.tables.pojos.Organization;
import generated.tables.pojos.Product;
import javafx.util.Pair;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static generated.Tables.*;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.sum;

public final class SQLTasks {

    public List<Pair<Organization, Integer>> task1() {
        final List<Pair<Organization, Integer>> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            var subquery = context.select(
                            INVOICE.ORGANIZATION_ID,
                            sum(INVOICE_POSITIONS.AMOUNT).as("amount")
                    ).from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                    .groupBy(INVOICE.ORGANIZATION_ID);

            final var query = context.select(
                            ORGANIZATION.INN,
                            subquery.field("amount", sum(INVOICE_POSITIONS.AMOUNT).getDataType()),
                            ORGANIZATION.NAME,
                            ORGANIZATION.BANK_ACCOUNT
                    )
                    .from(ORGANIZATION).join(subquery)
                    .on(ORGANIZATION.INN.eq(subquery.field(INVOICE.ORGANIZATION_ID)))
                    .orderBy(subquery.field("amount", sum(INVOICE_POSITIONS.AMOUNT).getDataType())
                            .desc()).limit(10);

            var records = query.fetch();

            for (var rec : records) {
                result.add(new Pair<>(new Organization(rec.value1(), rec.value3(), rec.value4()), rec.value2().intValue()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::1");
        }
        return result;
    }

    private SelectHavingConditionStep<Record3<Integer, BigDecimal, Integer>> createSubquery(int code, int value) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            return context.select(
                            INVOICE.ORGANIZATION_ID, sum(INVOICE_POSITIONS.AMOUNT), INVOICE_POSITIONS.PRODUCT_ID).
                    from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                    .where(INVOICE_POSITIONS.PRODUCT_ID.eq(code))
                    .groupBy(INVOICE.ORGANIZATION_ID, INVOICE_POSITIONS.PRODUCT_ID)
                    .having(sum(INVOICE_POSITIONS.AMOUNT).gt(BigDecimal.valueOf(value)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Organization> task2(List<Pair<Product, Integer>> parameters) {
        final List<Organization> result = new ArrayList<>();
        if (parameters.isEmpty())
            return result;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            var subquery = createSubquery(parameters.get(0).getKey().getCode(), parameters.get(0).getValue());

            var query = context.select(
                    ORGANIZATION.INN, ORGANIZATION.NAME, ORGANIZATION.BANK_ACCOUNT
            ).from(ORGANIZATION).join(subquery).on(ORGANIZATION.INN.eq(subquery.field(INVOICE.ORGANIZATION_ID)));

            parameters.remove(0);
            SelectOrderByStep<Record3<Integer, String, String>> final_query = query;
            for (var p : parameters) {
                var query1 = context.select(
                        ORGANIZATION.INN, ORGANIZATION.NAME, ORGANIZATION.BANK_ACCOUNT
                ).from(ORGANIZATION).join(createSubquery(p.getKey().getCode(), p.getValue())).on(ORGANIZATION.INN.eq(subquery.field(INVOICE.ORGANIZATION_ID)));
                final_query = final_query.intersect(query1);
            }
            var records = final_query.fetch();

            for (var rec : records) {
                result.add(new Organization(rec.value1(), rec.value2(), rec.value3()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::2");
        }
        return result;
    }

        public Pair<List<Task3Data>, List<Task3Data>> task3(Date startDate, Date endDate){
/*            var sql ="select product_id, name, date, sum(price*amount), sum(amount) as amount " +
                    "from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                    "join "+ schema +".product p on p.code = product_id " +
                    "where date > ? and date < ? group by product_id, date, name ;";*/

            final List<Task3Data> result = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
                final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
                var query = context.select(
                        INVOICE_POSITIONS.PRODUCT_ID,
                        PRODUCT.NAME,
                        INVOICE.DATE,
                        sum(INVOICE_POSITIONS.PRICE.mul(INVOICE_POSITIONS.AMOUNT)),
            sum(INVOICE_POSITIONS.AMOUNT)
                ).from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                        .join(PRODUCT).on(PRODUCT.CODE.eq(INVOICE_POSITIONS.PRODUCT_ID))
                        .where(INVOICE.DATE.gt(startDate.toLocalDate()).and(INVOICE.DATE.lt(endDate.toLocalDate())))
                        .groupBy(INVOICE_POSITIONS.PRODUCT_ID, INVOICE.DATE, PRODUCT.NAME);

                var records = query.fetch();

                for (var rec : records) {
                    result.add(new Task3Data(new Product(rec.value1(), rec.value2()),
                            rec.value3(), rec.value4().intValue(), rec.value5().intValue()));
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("SQLTASKS::3");
            }
            return new Pair<>(result, getFinalResForPeriod(startDate, endDate));
        }

        private List<Task3Data> getFinalResForPeriod(Date startDate, Date endDate) {
/*            var sql ="select product_id, name, sum(price*amount), sum(amount) as amount " +
                    "from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                    "join "+ schema +".product p on p.code = product_id " +
                    "where date > ? and date < ? group by product_id, name ;";*/

            final var result = new ArrayList<Task3Data>();
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
                final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
                var query = context.select(
                        INVOICE_POSITIONS.PRODUCT_ID,
                        PRODUCT.NAME,
            sum(INVOICE_POSITIONS.PRICE.mul(INVOICE_POSITIONS.AMOUNT)),
                        sum(INVOICE_POSITIONS.AMOUNT)
                ).from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                        .join(PRODUCT).on(PRODUCT.CODE.eq(INVOICE_POSITIONS.PRODUCT_ID))
                        .where(INVOICE.DATE.gt(startDate.toLocalDate()).and(INVOICE.DATE.lt(endDate.toLocalDate())))
                        .groupBy(INVOICE_POSITIONS.PRODUCT_ID, PRODUCT.NAME);

                var records = query.fetch();

                for (var rec : records) {
                    result.add(new Task3Data(new Product(rec.value1(), rec.value2()),
                            null, rec.value3().intValue(), rec.value4().intValue()));
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("SQLTASKS::3_additional_func");
            }
            return result;
        }

    public List<Pair<Product, BigDecimal>> task4(Date startDate, Date endDate) {
        final var result = new ArrayList<Pair<Product, BigDecimal>>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            var query = context.select(
                            INVOICE_POSITIONS.PRODUCT_ID,
                            PRODUCT.NAME,
                            (sum(INVOICE_POSITIONS.PRICE)).divide(count(INVOICE_POSITIONS.PRICE))
                    ).from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                    .join(PRODUCT).on(PRODUCT.CODE.eq(INVOICE_POSITIONS.PRODUCT_ID))
                    .where(INVOICE.DATE.gt(startDate.toLocalDate()).and(INVOICE.DATE.lt(endDate.toLocalDate())))
                    .groupBy(INVOICE_POSITIONS.PRODUCT_ID, PRODUCT.NAME);
            var records = query.fetch();

            for (var rec : records) {
                result.add(new Pair<Product, BigDecimal>(new Product(rec.value1(), rec.value2()), rec.value3()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::4");
        }
        return result;
    }

    public Map<Organization, List<Product>> task5(Date startDate, Date endDate) {

        final var result = new HashMap<Organization, List<Product>>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            var subquery = context.select(
                            INVOICE.ORGANIZATION_ID,
                            INVOICE_POSITIONS.PRODUCT_ID
                    ).from(INVOICE).join(INVOICE_POSITIONS).on(INVOICE.ID.eq(INVOICE_POSITIONS.INVOICE_ID))
                    .where((INVOICE.DATE.gt(startDate.toLocalDate())).and(INVOICE.DATE.lt(endDate.toLocalDate())))
                    .groupBy(INVOICE.ORGANIZATION_ID, INVOICE_POSITIONS.PRODUCT_ID);

            var query = context.select(
                            ORGANIZATION.INN, ORGANIZATION.NAME, ORGANIZATION.BANK_ACCOUNT, PRODUCT.CODE, PRODUCT.NAME
                    ).from(subquery).join(PRODUCT).on(PRODUCT.CODE.eq(subquery.field(INVOICE_POSITIONS.PRODUCT_ID)))
                    .rightJoin(ORGANIZATION).on(ORGANIZATION.INN.eq(subquery.field(INVOICE.ORGANIZATION_ID)))
                    .orderBy(ORGANIZATION.INN);

            var records = query.fetch();

            var seen = new ArrayList<Integer>();
            for (var rec : records) {
                var inn = rec.value1();
                var product = new Product(rec.value4(), rec.value5());
                var org = new Organization(inn, rec.value2(), rec.value3());
                List<Product> list;
                if (seen.contains(inn)) {
                    var key = result.keySet().stream().filter(x -> Objects.equals(x.getInn(), inn)).findFirst().orElse(null);
                    list = result.get(key);
                    result.remove(key);
                } else {
                    seen.add(inn);
                    list = new ArrayList<>();
                }
                list.add(product);
                result.put(org, list);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::5");
        }
        return result;
    }
}
