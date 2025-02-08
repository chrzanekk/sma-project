package pl.com.chrzanowski.sma.common.exception.error;

public enum ContractorErrorCode implements IErrorCode {
    CONTRACTOR_NOT_FOUND("contractorNotFound"),
    NAME_MISSING("nameMissing"),
    TAX_NUMBER_MISSING("taxNumberMissing"),
    STREET_MISSING("streetMissing"),
    BUILDING_NO_MISSING("buildingNoMissing"),
    CITY_MISSING("cityMissing"),
    POSTAL_CODE_MISSING("postalCodeMissing"),
    COUNTRY_MISSING("countryMissing"),
    IS_CUSTOMER_MISSING("isCustomerMissing"),
    IS_SUPPLIER_MISSING("isSupplierMissing"),
    IS_SCAFFOLDING_USER_MISSING("isScaffoldingUserMissing");

    private final String code;

    ContractorErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
