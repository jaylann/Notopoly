package com.company.exceptions;

public class cannotMortgageHousedPropertyException extends  Exception{
    public cannotMortgageHousedPropertyException(String errorMessage) {
        super(errorMessage);
    }
}
