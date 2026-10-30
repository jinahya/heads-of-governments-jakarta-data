package com.github.jinahya.headsofgovernments.persistence.data;

import com.github.jinahya.headsofgovernments.persistence.Office;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A {@link _Repository_QuarkusTest} of {@link OfficeRepository}.
 */
@Slf4j
@io.quarkus.test.junit.QuarkusTest
class OfficeRepository_QuarkusTest
        extends _Repository_QuarkusTest<OfficeRepository, Office, String> {

    private static List<String> countryList() {
        return List.of("FR", "GB", "JP", "KR", "US");
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    OfficeRepository_QuarkusTest() {
        super(OfficeRepository.class, Office.class, String.class);
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAll(Order)")
    @Nested
    class FindAll_Order {

        @Test
        void countryAsc() {
            assertThat(repositoryInstance().findAll(
                    jakarta.data.Order.by(jakarta.data.Sort.asc("country"))
            ))
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList());
        }

        @Test
        void countryDesc() {
            assertThat(repositoryInstance().findAll(
                    jakarta.data.Order.by(jakarta.data.Sort.desc("country"))
            ))
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList().reversed());
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAll(PageRequest, Order)")
    @Nested
    class FindAll_PageRequest_Order {

        @Test
        void countryAsc() {
            final var pageSize = ThreadLocalRandom.current().nextInt(1, countryList().size() + 1);
            final var order = jakarta.data.Order.by(jakarta.data.Sort.<Office>asc("country"));
            for (var p = jakarta.data.page.PageRequest.ofSize(pageSize); ;
                 p = jakarta.data.page.PageRequest.ofPage(p.page() + 1, pageSize, true)) {
                final var page = repositoryInstance().findAll(p, order);
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
            final var order = jakarta.data.Order.by(jakarta.data.Sort.<Office>desc("country"));
            for (var p = jakarta.data.page.PageRequest.ofSize(pageSize); ;
                 p = jakarta.data.page.PageRequest.ofPage(p.page() + 1, pageSize, true)) {
                final var page = repositoryInstance().findAll(p, order);
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
            assertThat(repositoryInstance().findAllByOrderByCountryAsc())
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList());
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAllFetchHeadsByOrderByCountryAsc()")
    @Nested
    class FindAllFetchHeadsByOrderByCountryAsc {

        @Test
        void five() {
            final var offices = repositoryInstance().findAllFetchHeadsByOrderByCountryAsc();
            assertThat(offices)
                    .extracting(Office::getCountry)
                    .containsExactlyElementsOf(countryList());
            assertThat(offices).allSatisfy(o -> assertThat(o.getHeads())
                    .isNotEmpty()
                    .allSatisfy(h -> assertThat(h.getOffice().getCountry()).isEqualTo(o.getCountry())));
            assertThat(offices)
                    .filteredOn(o -> o.getCountry().equals("KR"))
                    .singleElement()
                    .satisfies(o -> assertThat(o.getHeads()).hasSize(21));
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findAllByHeadKindOrderByCountryAsc(HeadKind, PageRequest)")
    @Nested
    class FindAllByHeadKindOrderByCountryAsc {

        private static Stream<Arguments> headKindAndCountryList() {
            return Stream.of(
                    Arguments.of(Office.HeadKind.PRESIDENT, List.of("FR", "KR", "US")),
                    Arguments.of(Office.HeadKind.PRIME_MINISTER, List.of("GB", "JP"))
            );
        }

        @MethodSource("headKindAndCountryList")
        @ParameterizedTest
        void __(final Office.HeadKind headKind, final List<String> countryList) {
            final var pageSize = ThreadLocalRandom.current().nextInt(1, countryList.size() + 1);
            final var countries = new ArrayList<String>();
            for (var p = jakarta.data.page.PageRequest.ofSize(pageSize); ;
                 p = jakarta.data.page.PageRequest.ofPage(p.page() + 1, pageSize, true)) {
                final var page = repositoryInstance()
                        .findAllByHeadKindOrderByCountryAsc(headKind, p);
                assertThat(page.totalElements()).isEqualTo(countryList.size());
                final var content = page.content();
                log.debug("page: {}, content: {}", p, content);
                assertThat(content).hasSizeLessThanOrEqualTo(pageSize)
                        .allSatisfy(o -> assertThat(o.getHeadKind()).isSameAs(headKind));
                if (content.isEmpty()) {
                    break;
                }
                content.forEach(o -> countries.add(o.getCountry()));
            }
            assertThat(countries).containsExactlyElementsOf(countryList);
        }
    }

    // ---------------------------------------------------------------------------------------------
    @DisplayName("findById(String)")
    @Nested
    class FindById {

        private static List<String> countryList() {
            return OfficeRepository_QuarkusTest.countryList();
        }

        @Test
        void _Empty_KP() {
            assertThat(repositoryInstance().findById("KP")).isEmpty();
        }

        @MethodSource("countryList")
        @ParameterizedTest
        void __(final String country) {
            assertThat(repositoryInstance().findById(country))
                    .hasValueSatisfying(o -> assertThat(o.getCountry()).isEqualTo(country));
        }
    }
}
