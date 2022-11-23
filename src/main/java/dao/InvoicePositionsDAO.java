package dao;

import generated.tables.pojos.InvoicePositions;
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

import static generated.Tables.INVOICE_POSITIONS;


public final class InvoicePositionsDAO implements DAO<InvoicePositions> {

    @Override
    public @NotNull InvoicePositions get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            INVOICE_POSITIONS.ID,
                            INVOICE_POSITIONS.AMOUNT,
                            INVOICE_POSITIONS.PRICE,
                            INVOICE_POSITIONS.PRODUCT_ID,
                            INVOICE_POSITIONS.INVOICE_ID
                    )
                    .from(INVOICE_POSITIONS).where(INVOICE_POSITIONS.ID.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("InvoicePositions with id " + id + " not found");

            return new InvoicePositions(rec.value1(), rec.value5(), rec.value3(), rec.value2(), rec.value4());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::GET");
        }
    }

    @Override
    public @NotNull List<InvoicePositions> all() {
        final List<InvoicePositions> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            INVOICE_POSITIONS.ID,
                            INVOICE_POSITIONS.AMOUNT,
                            INVOICE_POSITIONS.PRICE,
                            INVOICE_POSITIONS.PRODUCT_ID,
                            INVOICE_POSITIONS.INVOICE_ID
                    )
                    .from(INVOICE_POSITIONS);

            var records = query.fetch();

            for (var rec : records) {
                result.add(new InvoicePositions(rec.value1(), rec.value5(), rec.value3(), rec.value2(), rec.value4()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONS::GET");
        }
        return result;
    }

    @Override
    public void save(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(INVOICE_POSITIONS, INVOICE_POSITIONS.ID, INVOICE_POSITIONS.AMOUNT, INVOICE_POSITIONS.PRICE,
                            INVOICE_POSITIONS.INVOICE_ID, INVOICE_POSITIONS.PRODUCT_ID)
                    .values(entity.getId(), entity.getAmount(), entity.getPrice(), entity.getInvoiceId(), entity.getProductId())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONS::SAVE");
        }
    }

    @Override
    public void update(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(INVOICE_POSITIONS)
                    .set(
                            INVOICE_POSITIONS.AMOUNT, entity.getAmount()
                    )
                    .set(
                            INVOICE_POSITIONS.PRICE, entity.getPrice()
                    )
                    .set(
                            INVOICE_POSITIONS.INVOICE_ID, entity.getInvoiceId()
                    )
                    .set(
                            INVOICE_POSITIONS.PRODUCT_ID, entity.getProductId()
                    )
                    .where(INVOICE_POSITIONS.ID.eq(entity.getId()))
                    .execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(INVOICE_POSITIONS)
                    .where(INVOICE_POSITIONS.ID.eq(entity.getId()))
                    .execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::DELETE");
        }
    }
}

