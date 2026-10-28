package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Office;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.PageRequest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A {@link QuarkusTest} of {@link OfficeRepository}, against {@code heads_of_governments.sqlite3}, a
 * test resource, as {@code application.properties} configures it; no {@code persistence.xml}.
 */
@QuarkusTest
class OfficeRepository_QuarkusTest {

    @DisplayName("findAll(Order)")
    @Nested
    class FindAll_Order {

        @Test
        void countryDesc() {
            assertThat(repository.findAll(Order.by(Sort.desc("country"))))
                    .extracting(Office::getCountry)
                    .containsExactly("US", "KR", "JP", "GB", "FR");
        }
    }

    @DisplayName("findAll(Order, PageRequest)")
    @Nested
    class FindAll_Order_PageRequest {

        @Test
        void countryAsc_firstPage() {
            final var page = repository.findAll(
                    Order.by(Sort.asc("country")),
                    PageRequest.ofSize(2)
            );
            assertThat(page.content()).extracting(Office::getCountry).containsExactly("FR", "GB");
            assertThat(page.totalElements()).isEqualTo(5L);
        }
    }

    @DisplayName("findAllByOrderByCountryAsc()")
    @Nested
    class FindAllByOrderByCountryAsc {

        @Test
        void five() {
            assertThat(repository.findAllByOrderByCountryAsc())
                    .extracting(Office::getCountry)
                    .containsExactly("FR", "GB", "JP", "KR", "US");
        }
    }

    @DisplayName("findByCountry(String)")
    @Nested
    class FindByCountry {

        @Test
        void KR() {
            assertThat(repository.findByCountry("KR"))
                    .hasValueSatisfying(o -> assertThat(o.getHeadKind()).isEqualTo("PRESIDENT"));
        }
    }

    @Inject
    OfficeRepository repository;
}
