package pl.com.chrzanowski.sma.common.exception.error;

public enum EmployeeErrorCode implements IErrorCode {
    EMPLOYEE_NOT_FOUND("employeeNotFound"),
    FIRST_NAME_MISSING("nameMissing"),
    LAST_NAME_MISSING("lastNameMissing"),
    POSITION_MISSING("positionMissing"),
    HOUR_RATE_MISSING("hourRateMissing");

    private final String code;

    EmployeeErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
