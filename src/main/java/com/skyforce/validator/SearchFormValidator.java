package com.skyforce.validator;

import com.skyforce.dto.SearchDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Sulaymon on 12.03.2018.
 */
@Component
public class SearchFormValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals(SearchDTO.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "keyword", "error.keyword","search box at least must have 2 characters");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city","error.city","search box city can't be empty");
    }
}
