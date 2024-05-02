package com.esg.console.csv;

import com.esg.console.model.CustomerDetails;
import io.reactivex.rxjava3.functions.Function;
import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Optional;

public class LineCSVParser implements Function<String, Optional<CustomerDetails>> {
    private static final Logger logger = LoggerFactory.getLogger(LineCSVParser.class);

    private static final String CUSTOMER_REF = "Customer Ref";
    private static final String CUSTOMER_NAME = "Customer Name";
    private static final String ADDRESS_LINE_1 = "Address Line 1";
    private static final String ADDRESS_LINE_2 = "Address Line 2";
    private static final String TOWN = "Town";
    private static final String COUNTY = "County";
    private static final String COUNTRY = "Country";
    private static final String POSTCODE = "Postcode";

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader(CUSTOMER_REF, CUSTOMER_NAME,
                    ADDRESS_LINE_1, ADDRESS_LINE_2, TOWN,
                    COUNTY, COUNTRY, POSTCODE)
            .setSkipHeaderRecord(false)
            .build();

    @Override
    public Optional<CustomerDetails> apply(String input) {
        try {
            return CSV_FORMAT.parse(new StringReader(input)).stream()
                    .findFirst().map(record -> new CustomerDetails(
                            record.get(CUSTOMER_REF),
                            record.get(CUSTOMER_NAME),
                            record.get(ADDRESS_LINE_1),
                            record.get(ADDRESS_LINE_2),
                            record.get(TOWN),
                            record.get(COUNTY),
                            record.get(COUNTRY),
                            record.get(POSTCODE)
                    ));
        } catch (Throwable e) {
            logger.warn("Could not process line: {}", input, e);
            return Optional.empty();
        }
    }
}
