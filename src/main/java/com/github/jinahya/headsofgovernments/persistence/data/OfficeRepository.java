package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Office;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.DataRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A repository of {@link Office}, whose identifier is its {@code country}.
 */
@Repository
public interface OfficeRepository extends DataRepository<Office, String> {

    /**
     * Finds all offices, in the specified order.
     *
     * @param order the order of the offices.
     * @return a list of all offices.
     */
    @Find
    List<Office> findAll(Order<Office> order);

    /**
     * Finds a page of all offices, in the specified order.
     *
     * @param order       the order of the offices; make it total, e.g., ending with
     *                    {@code country}, for the pages to be.
     * @param pageRequest the page to find.
     * @return the page of all offices.
     */
    @Find
    Page<Office> findAll(Order<Office> order, PageRequest pageRequest);

    /**
     * Finds the office of the specified country.
     *
     * @param country the country.
     * @return an optional of the office of {@code country}; empty if none.
     */
    @Find
    Optional<Office> findByCountry(String country);

    /**
     * Finds all offices, ordered by their countries.
     *
     * @return a list of all offices.
     */
    @Find
    @OrderBy("country")
    List<Office> findAllByOrderByCountryAsc();
}
