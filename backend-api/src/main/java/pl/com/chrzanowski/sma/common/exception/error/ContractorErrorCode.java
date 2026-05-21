package pl.com.chrzanowski.sma.common.exception.error;

public enum ContractorErrorCode implements IErrorCode {
    CONTRACTOR_NOT_FOUND("contractors.contractorNotFound"),
    NAME_MISSING("contractors.nameMissing"),
    TAX_NUMBER_MISSING("contractors.taxNumberMissing"),
    STREET_MISSING("contractors.streetMissing"),
    BUILDING_NO_MISSING("contractors.buildingNoMissing"),
    CITY_MISSING("contractors.cityMissing"),
    POSTAL_CODE_MISSING("contractors.postalCodeMissing"),
    COUNTRY_MISSING("contractors.countryMissing"),
    IS_CUSTOMER_MISSING("contractors.isCustomerMissing"),
    IS_SUPPLIER_MISSING("contractors.isSupplierMissing"),
    IS_SCAFFOLDING_USER_MISSING("contractors.isScaffoldingUserMissing"),
    CONTRACTOR_NOT_CUSTOMER("contractors.contractorNotCustomer"),
    CONTRACTOR_NOT_SCAFFOLDING_USER("contractors.contractorNotScaffoldingUser");


    private final String code;

    ContractorErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
