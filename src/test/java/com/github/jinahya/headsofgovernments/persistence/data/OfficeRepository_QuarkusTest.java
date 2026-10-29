package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Office;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.PageRequest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A {@link QuarkusTest} of {@link OfficeRepository}, against {@code heads_of_governments.sqlite3},
 * a test resource, as {@code application.properties} configures it; no {@code persistence.xml}.
 */
@Slf4j
@QuarkusTest
class OfficeRepository_QuarkusTest {

    private static List<String> countryList() {
        return List.of("FR", "GB", "JP", "KR", "US");
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAll(Order)")
    @Nested
    class FindAll_Order {

        @Test
        void countryAsc() {
            assertThat(repository.findAll(Order.by(Sort.asc("country"))))
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList());
        }

        @Test
        void countryDesc() {
            assertThat(repository.findAll(Order.by(Sort.desc("country"))))
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList().reversed());
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAll(Order, PageRequest)")
    @Nested
    class FindAll_Order_PageRequest {

        @Test
        void countryAsc() {
            final var pageSize = ThreadLocalRandom.current().nextInt(1, countryList().size() + 1);
            final var order = Order.by(Sort.<Office>asc("country"));
            for (var p = PageRequest.ofSize(pageSize); ;
                 p = PageRequest.ofPage(p.page() + 1, pageSize, true)) {
                final var page = repository.findAll(order, p);
                assertThat(page.totalElements()).isEqualTo(countryList().size());
                final var content = page.content();
                log.debug("page: {}, content: {}", p, content);
                assertThat(content).hasSizeLessThanOrEqualTo(pageSize);
                if (content.isEmpty()) {
                    break;
                }
                assertThat(content).extracting(Office::getCountry).isSorted();
            }
        }

        @Test
        void countryDesc() {
            final var pageSize = ThreadLocalRandom.current().nextInt(1, countryList().size() + 1);
            final var order = Order.by(Sort.<Office>desc("country"));
            for (var p = PageRequest.ofSize(pageSize); ;
                 p = PageRequest.ofPage(p.page() + 1, pageSize, true)) {
                final var page = repository.findAll(order, p);
                assertThat(page.totalElements()).isEqualTo(countryList().size());
                final var content = page.content();
                log.debug("page: {}, content: {}", p, content);
                assertThat(content).hasSizeLessThanOrEqualTo(pageSize);
                if (content.isEmpty()) {
                    break;
                }
                assertThat(content).extracting(Office::getCountry)
                        .isSortedAccordingTo(Comparator.reverseOrder());
            }
        }
    }

    @DisplayName("findAllByOrderByCountryAsc()")
    @Nested
    class FindAllByOrderByCountryAsc {

        @Test
        void five() {
            assertThat(repository.findAllByOrderByCountryAsc())
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList());
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findByCountry(String)")
    @Nested
    class FindByCountry {

        private static List<String> countryList() {
            return OfficeRepository_QuarkusTest.countryList();
        }

        @Test
        void _Empty_KP() {
            assertThat(repository.findByCountry("KP")).isEmpty();
        }

        @MethodSource("countryList")
        @ParameterizedTest
        void __(final String country) {
            assertThat(repository.findByCountry(country))
                    .hasValueSatisfying(o -> assertThat(o.getCountry()).isEqualTo(country));
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Inject
    OfficeRepository repository;
}
