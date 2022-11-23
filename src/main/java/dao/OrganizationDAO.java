package dao;

import generated.tables.pojos.Organization;
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

import static generated.Tables.ORGANIZATION;


public final class OrganizationDAO implements DAO<Organization> {

    @Override
    public @NotNull Organization get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            ORGANIZATION.INN,
                            ORGANIZATION.NAME,
                            ORGANIZATION.BANK_ACCOUNT
                    )
                    .from(ORGANIZATION).where(ORGANIZATION.INN.eq(id));

            var rec = query.fetchOne();

            if (rec == null)
                throw new IllegalStateException("Organization with inn " + id + " not found");

            return new Organization(rec.value1(),
                    rec.value2(), rec.value3());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::GET");
        }

    }

    @Override
    public @NotNull List<Organization> all() {
        final List<Organization> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final var query = context.select(
                            ORGANIZATION.INN,
                            ORGANIZATION.NAME,
                            ORGANIZATION.BANK_ACCOUNT
                    )
                    .from(ORGANIZATION);

            var records = query.fetch();

            for (var rec: records) {
                result.add(new Organization(rec.value1(),
                        rec.value2(), rec.value3()));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::ALL");
        }
        return result;
    }

    @Override
    public void save(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .insertInto(ORGANIZATION, ORGANIZATION.INN, ORGANIZATION.NAME, ORGANIZATION.BANK_ACCOUNT)
                    .values(entity.getInn(), entity.getName(), entity.getBankAccount())
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .update(ORGANIZATION)
                    .set(
                            ORGANIZATION.NAME, entity.getName()
                    )
                    .set(
                            ORGANIZATION.BANK_ACCOUNT, entity.getBankAccount()
                    )
                    .where(ORGANIZATION.INN.eq(entity.getInn()))
                    .execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
            context
                    .delete(ORGANIZATION)
                    .where(ORGANIZATION.INN.eq(entity.getInn()))
                    .execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::DELETE");
        }
    }
}
