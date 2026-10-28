package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Head;
import com.github.jinahya.headsofgovernments.persistence.Office;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.Repository;

import java.util.List;

/**
 * A repository of {@link Head}, whose identifier is a surrogate {@code id}.
 */
@Repository
public interface HeadRepository
        extends BasicRepository<Head, Long> {

    /**
     * Finds all heads, in the specified order.
     *
     * @param order the order of the heads.
     * @return a list of all heads.
     */
    @Find
    List<Head> findAll(Order<Head> order);

    /**
     * Finds a page of all heads, in the specified order.
     *
     * @param order       the order of the heads; make it total, e.g., ending with {@code id}, for
     *                    the pages to be.
     * @param pageRequest the page to find.
     * @return the page of all heads.
     */
    @Find
    Page<Head> findAll(Order<Head> order, PageRequest pageRequest);

    /**
     * Finds all heads of the specified country, ordered by the starts of their terms, then by their
     * identifiers.
     *
     * @param country the country.
     * @return a list of the heads of {@code country}.
     */
    @Find
    @OrderBy("termStart")
    @OrderBy("id")
    List<Head> findByCountryOrderByTermStartAscIdAsc(String country);

    /**
     * Finds a page of the heads of the specified office, in the specified order.
     *
     * @param office      the office.
     * @param order       the order of the heads; make it total, e.g., ending with {@code id}, for
     *                    the pages to be.
     * @param pageRequest the page to find.
     * @return the page of the heads of {@code office}.
     */
    @Find
    Page<Head> findAllByOffice(Office office, Order<Head> order, PageRequest pageRequest);
}
