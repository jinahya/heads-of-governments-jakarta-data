package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Head;
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
 * A {@link QuarkusTest} of {@link HeadRepository}, against {@code heads_of_governments.sqlite3}, a
 * test resource, as {@code application.properties} configures it; no {@code persistence.xml}.
 */
@QuarkusTest
class HeadRepository_QuarkusTest {

    @DisplayName("findAll(Order)")
    @Nested
    class FindAll_Order {

        @Test
        void idAsc() {
            final var heads = repository.findAll(Order.by(Sort.asc("id")));
            assertThat(heads).hasSize(285);
            assertThat(heads).extracting(Head::getId).isSorted();
        }
    }

    @DisplayName("findAll(Order, PageRequest)")
    @Nested
    class FindAll_Order_PageRequest {

        @Test
        void idAsc_firstPage() {
            final var page = repository.findAll(
                    Order.by(Sort.asc("id")),
                    PageRequest.ofSize(10)
            );
            assertThat(page.content()).hasSize(10);
            assertThat(page.content()).extracting(Head::getId).isSorted();
            assertThat(page.totalElements()).isEqualTo(285L);
        }
    }

    @DisplayName("findByCountryOrderByTermStartAscIdAsc(String)")
    @Nested
    class FindByCountryOrderByTermStartAscIdAsc {

        @Test
        void KR() {
            final var heads = repository.findByCountryOrderByTermStartAscIdAsc("KR");
            assertThat(heads).hasSize(21);
            assertThat(heads).extracting(Head::getTermStart).isSorted();
            assertThat(heads.getFirst().getName()).isEqualTo("이승만");
        }
    }

    @DisplayName("findAllByOffice(Office, Order, PageRequest)")
    @Nested
    class FindAllByOffice {

        @Test
        void KR_termStartDesc_firstPage() {
            final var office = officeRepository.findByCountry("KR").orElseThrow();
            final var page = repository.findAllByOffice(
                    office,
                    Order.by(Sort.desc("termStart"), Sort.desc("id")),
                    PageRequest.ofSize(10)
            );
            assertThat(page.content()).hasSize(10);
            assertThat(page.content()).extracting(Head::getName).startsWith("이재명", "윤석열");
            assertThat(page.totalElements()).isEqualTo(21L);
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Inject
    HeadRepository repository;

    @Inject
    OfficeRepository officeRepository;
}
