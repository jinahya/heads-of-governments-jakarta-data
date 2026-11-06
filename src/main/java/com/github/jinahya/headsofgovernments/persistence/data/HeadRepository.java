package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Head;
import com.github.jinahya.headsofgovernments.persistence.HeadId;
import com.github.jinahya.headsofgovernments.persistence.Office;

import java.util.List;

/**
 * A repository of {@link Head}, whose identifier is a {@link HeadId}, of the key
 * {@code (country, term_start)}.
 */
@jakarta.data.repository.Repository
public interface HeadRepository
        extends _Repository<Head, HeadId>,
                jakarta.data.repository.CrudRepository<Head, HeadId> {

    /**
     * Finds all heads, in the specified order.
     *
     * @param order the order of the heads.
     * @return a list of all heads.
     */
    @jakarta.data.repository.Find
    List<Head> findAll(jakarta.data.Order<Head> order);

    /**
     * Finds all heads of the specified country, ordered by the starts of their terms, which are
     * unique within a country.
     *
     * @param country the country.
     * @return a list of the heads of {@code country}.
     */
    @jakarta.data.repository.Find
    @jakarta.data.repository.OrderBy("termStart")
    List<Head> findByCountryOrderByTermStartAsc(String country);

    /**
     * Finds a page of the heads of the specified office, in the specified order.
     *
     * @param office      the office.
     * @param pageRequest the page to find.
     * @param order       the order of the heads; make it total, e.g., with {@code termStart}, for
     *                    the pages to be.
     * @return the page of the heads of {@code office}.
     */
    @jakarta.data.repository.Find
    jakarta.data.page.Page<Head> findAllByOffice(
            Office office,
            jakarta.data.page.PageRequest pageRequest,
            jakarta.data.Order<Head> order);
}
