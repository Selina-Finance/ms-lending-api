package com.selina.lending.api.mapper.common;

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import org.mapstruct.MapperConfig;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpendituresMapper {

    @Named("mapExpenditures")
    static List<Expenditure> mapExpenditures(List<ExpenditureDto> expenditures) {
        if (expenditures == null) {
            return null;
        }

        return mergeExpenditures(expenditures).stream()
                .map(ExpenditureMapper.INSTANCE::mapToExpenditure)
                .collect(toList());
    }

    private static Collection<ExpenditureDto> mergeExpenditures(List<ExpenditureDto> expenditures) {
        return expenditures.stream().collect(Collectors.toMap(
                ExpenditureDto::getExpenditureType,
                Function.identity(),
                ExpendituresMapper::mergeExpenditureDtoProperties
        )).values();
    }

    private static ExpenditureDto mergeExpenditureDtoProperties(ExpenditureDto left, ExpenditureDto right) {
        mergeIntegerPropertyValue(left::setBalanceDeclared, left::getBalanceDeclared, right::getBalanceDeclared);
        mergeDoublePropertyValue(left::setAmountDeclared, left::getAmountDeclared, right::getAmountDeclared);
        mergeDoublePropertyValue(left::setPaymentVerified, left::getPaymentVerified, right::getPaymentVerified);
        mergeDoublePropertyValue(left::setAmountVerified, left::getAmountVerified, right::getAmountVerified);
        return left;
    }

    private static void mergeIntegerPropertyValue(Consumer<Integer> leftValueSetter, Supplier<Integer> leftValueGetter, Supplier<Integer> rightValueGetter) {
        Double summed = sumNullableNumbers(leftValueGetter.get(), rightValueGetter.get());
        leftValueSetter.accept(summed == null ? null : summed.intValue());
    }

    private static void mergeDoublePropertyValue(Consumer<Double> leftValueSetter, Supplier<Double> leftValueGetter, Supplier<Double> rightValueGetter) {
        leftValueSetter.accept(sumNullableNumbers(leftValueGetter.get(), rightValueGetter.get()));
    }

    private static Double sumNullableNumbers(Number a, Number b) {
        if (a == null && b == null)
            return null;

        return (a == null ? 0.0 : a.doubleValue()) + (b == null ? 0.0 : b.doubleValue());
    }
}
