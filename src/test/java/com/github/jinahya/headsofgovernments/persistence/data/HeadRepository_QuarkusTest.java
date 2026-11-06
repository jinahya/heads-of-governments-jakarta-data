package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Head;
import com.github.jinahya.headsofgovernments.persistence.HeadId;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A {@link _Repository_QuarkusTest} of {@link HeadRepository}.
 */
@io.quarkus.test.junit.QuarkusTest
class HeadRepository_QuarkusTest
        extends _Repository_QuarkusTest<HeadRepository, Head, HeadId> {

    /**
     * Creates a new instance.
     */
    HeadRepository_QuarkusTest() {
        super(HeadRepository.class, Head.class, HeadId.class);
    }

    // ---------------------------------------------------------------------------------------------

    @DisplayName("findAll(Order)")
    @Nested
    class FindAll_Order {

        @Test
        void countryAscTermStartAsc() {
            final var heads = repositoryInstance().findAll(
                    jakarta.data.Order.by(jakarta.data.Sort.asc("country"),
                                         jakarta.data.Sort.asc("termStart"))
            );
            assertThat(heads).hasSize(285);
            assertThat(heads).isSortedAccordingTo(
                    java.util.Comparator.comparing((Head h) -> h.getOffice().getCountry())
                                            .thenComparing(Head::getTermStart));
        }
    }

    @DisplayName("findAll(PageRequest, Order)")
    @Nested
    class FindAll_PageRequest_Order {

        @Test
        void countryAscTermStartAsc_firstPage() {
            final var page = repositoryInstance().findAll(
                    jakarta.data.page.PageRequest.ofSize(10),
                    jakarta.data.Order.by(jakarta.data.Sort.asc("country"),
                                         jakarta.data.Sort.asc("termStart"))
            );
            assertThat(page.content()).hasSize(10);
            assertThat(page.content()).isSortedAccordingTo(
                    java.util.Comparator.comparing((Head h) -> h.getOffice().getCountry())
                                            .thenComparing(Head::getTermStart));
            assertThat(page.totalElements()).isEqualTo(285L);
        }
    }

    @DisplayName("findByCountryOrderByTermStartAsc(String)")
    @Nested
    class FindByCountryOrderByTermStartAsc {

        @Test
        void KR() {
            final var heads = repositoryInstance().findByCountryOrderByTermStartAsc("KR");
            assertThat(heads).hasSize(21);
            assertThat(heads).extracting(Head::getTermStart).isSorted();
            assertThat(heads.getFirst().getName()).isEqualTo("이승만");
        }
    }

    @DisplayName("findAllByOffice(Office, PageRequest, Order)")
    @Nested
    class FindAllByOffice {

        @Test
        void KR_termStartDesc_firstPage() {
            final var office = officeRepository.findById("KR").orElseThrow();
            final var page = repositoryInstance().findAllByOffice(
                    office,
                    jakarta.data.page.PageRequest.ofSize(10),
                    jakarta.data.Order.by(
                            jakarta.data.Sort.desc("termStart")
                    )
            );
            assertThat(page.content()).hasSize(10);
            assertThat(page.content()).extracting(Head::getName).startsWith("이재명", "윤석열");
            assertThat(page.totalElements()).isEqualTo(21L);
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Inject
    OfficeRepository officeRepository;
}
