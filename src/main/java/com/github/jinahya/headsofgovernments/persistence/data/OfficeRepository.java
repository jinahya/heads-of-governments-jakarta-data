package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Office;

import java.util.List;

/**
 * A repository of {@link Office}, whose identifier is its {@code country}.
 */
@jakarta.data.repository.Repository
public interface OfficeRepository
        extends _Repository<Office, String>,
                jakarta.data.repository.CrudRepository<Office, String> {

    /**
     * Finds all offices, in the specified order.
     *
     * @param order the order of the offices.
     * @return a list of all offices.
     */
    @jakarta.data.repository.Find
    List<Office> findAll(jakarta.data.Order<Office> order);

    /**
     * Finds all offices, ordered by their countries.
     *
     * @return a list of all offices.
     */
    @jakarta.data.repository.Find
    @jakarta.data.repository.OrderBy(value = "country", descending = false)
    List<Office> findAllByOrderByCountryAsc();

    /**
     * Finds all offices, ordered by their countries, each with its heads fetched in the same query.
     *
     * @return a list of all offices, with their heads.
     */
    @jakarta.data.repository.Query("""
            SELECT o
            FROM Office AS o
                 LEFT JOIN FETCH o.heads
            ORDER BY o.country ASC""")
    List<Office> findAllFetchHeadsByOrderByCountryAsc();

    /**
     * Finds a page of the offices of the specified head kind, ordered by their countries.
     *
     * @param headKind    the head kind.
     * @param pageRequest the page to find.
     * @return the page of the offices of {@code headKind}.
     */
    @jakarta.data.repository.Find
    @jakarta.data.repository.OrderBy(value = "country", descending = false)
    jakarta.data.page.Page<Office> findAllByHeadKindOrderByCountryAsc(
            Office.HeadKind headKind,
            jakarta.data.page.PageRequest pageRequest);
}
