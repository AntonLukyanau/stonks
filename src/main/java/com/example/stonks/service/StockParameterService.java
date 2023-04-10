package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.RequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StockParameterService implements ParameterService<RequestParameters> {

    private final WorkDaysResolver nyseWorkDaysResolver;

    @Override
    public RequestParameters fillParameters(String... parameters) {
        String company = parameters[0];
        String start = parameters[1];
        String end = parameters[2];
        LocalDate endDate = end == null || end.isBlank()
                ? LocalDate.now()
                : LocalDate.parse(end, NYSEConstants.DATE_FORMAT);
        LocalDate startDate = start == null || start.isBlank()
                ? nyseWorkDaysResolver.resolveLastWorkDayBefore(endDate)
                : LocalDate.parse(start, NYSEConstants.DATE_FORMAT);
        return new RequestParameters(company, NYSEResultFrequency.DAILY, startDate, endDate);
    }
}
