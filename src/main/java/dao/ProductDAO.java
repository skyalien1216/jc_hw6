package dao;

import generated.tables.pojos.Product;
import org.java_courses.CREDS;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.PRODUCT;


public final class ProductDAO implements DAO<Product> {

    @Override
    public @NotNull Product get(int id) {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            PRODUCT.CODE,
                            PRODUCT.NAME
                    )
                    .from(PRODUCT).where(PRODUCT.CODE.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("Product with code " + id + " not found");

            return new Product(rec.value1(), rec.value2());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::GET");
        }
    }

    @Override
    public @NotNull List<Product> all() {
        final List<Product> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            PRODUCT.CODE,
                            PRODUCT.NAME
                    )
                    .from(PRODUCT);

            var records = query.fetch();

            for (var rec: records) {
                result.add(new Product(rec.value1(), rec.value2()));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::ALL");
        }
        return result;
    }

    @Override
    public void save(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(PRODUCT, PRODUCT.CODE, PRODUCT.NAME)
                    .values(entity.getCode(), entity.getName())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(PRODUCT)
                    .set(
                            PRODUCT.NAME, entity.getName()
                    )
                    .where(PRODUCT.CODE.eq(entity.getCode()))
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(PRODUCT)
                    .where(PRODUCT.CODE.eq(entity.getCode()))
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::DELETE");
        }
    }
}

