package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Head;
import com.github.jinahya.headsofgovernments.persistence.Office;

import java.util.List;

/**
 * A repository of {@link Head}, whose identifier is a surrogate {@code id}.
 */
@jakarta.data.repository.Repository
public interface HeadRepository
        extends _Repository<Head, Long>,
                jakarta.data.repository.CrudRepository<Head, Long> {

    /**
     * Finds all heads, in the specified order.
     *
     * @param order the order of the heads.
     * @return a list of all heads.
     */
    @jakarta.data.repository.Find
    List<Head> findAll(jakarta.data.Order<Head> order);

    /**
     * Finds all heads of the specified country, ordered by the starts of their terms, then by their
     * identifiers.
     *
     * @param country the country.
     * @return a list of the heads of {@code country}.
     */
    @jakarta.data.repository.Find
    @jakarta.data.repository.OrderBy("termStart")
    @jakarta.data.repository.OrderBy(value = "id", descending = false)
    List<Head> findByCountryOrderByTermStartAscIdAsc(String country);

    /**
     * Finds a page of the heads of the specified office, in the specified order.
     *
     * @param office      the office.
     * @param pageRequest the page to find.
     * @param order       the order of the heads; make it total, e.g., ending with {@code id}, for
     *                    the pages to be.
     * @return the page of the heads of {@code office}.
     */
    @jakarta.data.repository.Find
    jakarta.data.page.Page<Head> findAllByOffice(
            Office office,
            jakarta.data.page.PageRequest pageRequest,
            jakarta.data.Order<Head> order);
}
