package dao;

import generated.tables.pojos.Invoice;
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

import static generated.Tables.INVOICE;


public final class InvoiceDAO implements DAO<Invoice> {

    @Override
    public @NotNull Invoice get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            INVOICE.ID,
                            INVOICE.DATE,
                            INVOICE.ORGANIZATION_ID
                    )
                    .from(INVOICE).where(INVOICE.ID.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("Invoice with id " + id + " not found");

            return new Invoice(rec.value1(),
                    rec.value2(), rec.value3());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::GET");
        }
    }

    @Override
    public @NotNull List<Invoice> all() {
        final List<Invoice> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            INVOICE.ID,
                            INVOICE.DATE,
                            INVOICE.ORGANIZATION_ID
                    )
                    .from(INVOICE);

            var records = query.fetch();

            for (var rec: records) {
                result.add(new Invoice(rec.value1(),
                        rec.value2(), rec.value3()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::GET");
        }
        return result;
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(INVOICE, INVOICE.ID, INVOICE.DATE, INVOICE.ORGANIZATION_ID)
                    .values(entity.getId(), entity.getDate(), entity.getOrganizationId())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(INVOICE)
                    .set(
                            INVOICE.DATE, entity.getDate()
                    )
                    .set(
                            INVOICE.ORGANIZATION_ID, entity.getOrganizationId()
                    )
                    .where(INVOICE.ID.eq(entity.getId()))
                    .execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(INVOICE)
                    .where(INVOICE.ID.eq(entity.getId()))
                    .execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::DELETE");
        }
    }
}
