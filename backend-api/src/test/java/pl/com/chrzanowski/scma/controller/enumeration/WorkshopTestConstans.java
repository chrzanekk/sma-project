package pl.com.chrzanowski.scma.controller.enumeration;

public enum WorkshopTestConstans {

    FIRST_NAME("FirstName"),
    FIRST_TAX_NUMBER("111-111-11-11"),
    FIRST_STREET("FirstStreet"),
    FIRST_BUILDING_NO("1a"),
    FIRST_APARTMENT_NO("1b"),
    FIRST_POSTAL_CODE("11-111"),
    FIRST_CITY("FirstCity"),
    SECOND_NAME("SecondName"),
    SECOND_TAX_NUMBER("222-222-22-22"),
    SECOND_STREET("SecondStreet"),
    SECOND_BUILDING_NO("2a"),
    SECOND_APARTMENT_NO("2b"),
    SECOND_POSTAL_CODE("22-222"),
    SECOND_CITY("SecondCity"),
    UPDATED_NAME("UpdatedName"),
    UPDATED_TAX_NUMBER("333-333-33-33"),
    UPDATED_STREET("UpdatedStreet"),
    UPDATED_BUILDING_NO("3a"),
    UPDATED_APARTMENT_NO("3b"),
    UPDATED_POSTAL_CODE("33-333"),
    UPDATED_CITY("UpdatedCity");

    String field;

    WorkshopTestConstans(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
