/*
 * This file is generated by jOOQ.
 */
package generated.tables.daos;


import generated.tables.Invoice;
import generated.tables.records.InvoiceRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoiceDao extends DAOImpl<InvoiceRecord, generated.tables.pojos.Invoice, Integer> {

    /**
     * Create a new InvoiceDao without any configuration
     */
    public InvoiceDao() {
        super(Invoice.INVOICE, generated.tables.pojos.Invoice.class);
    }

    /**
     * Create a new InvoiceDao with an attached configuration
     */
    public InvoiceDao(Configuration configuration) {
        super(Invoice.INVOICE, generated.tables.pojos.Invoice.class, configuration);
    }

    @Override
    public Integer getId(generated.tables.pojos.Invoice object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<generated.tables.pojos.Invoice> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Invoice.INVOICE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<generated.tables.pojos.Invoice> fetchById(Integer... values) {
        return fetch(Invoice.INVOICE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public generated.tables.pojos.Invoice fetchOneById(Integer value) {
        return fetchOne(Invoice.INVOICE.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<generated.tables.pojos.Invoice> fetchOptionalById(Integer value) {
        return fetchOptional(Invoice.INVOICE.ID, value);
    }

    /**
     * Fetch records that have <code>date BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<generated.tables.pojos.Invoice> fetchRangeOfDate(LocalDate lowerInclusive, LocalDate upperInclusive) {
        return fetchRange(Invoice.INVOICE.DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>date IN (values)</code>
     */
    public List<generated.tables.pojos.Invoice> fetchByDate(LocalDate... values) {
        return fetch(Invoice.INVOICE.DATE, values);
    }

    /**
     * Fetch records that have <code>organization_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<generated.tables.pojos.Invoice> fetchRangeOfOrganizationId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Invoice.INVOICE.ORGANIZATION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>organization_id IN (values)</code>
     */
    public List<generated.tables.pojos.Invoice> fetchByOrganizationId(Integer... values) {
        return fetch(Invoice.INVOICE.ORGANIZATION_ID, values);
    }
}
