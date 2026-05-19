package pl.com.chrzanowski.sma.common.exception.error;

public enum EmployeeErrorCode implements IErrorCode {
    EMPLOYEE_NOT_FOUND("employees.employeeNotFound"),
    FIRST_NAME_MISSING("employees.nameMissing"),
    LAST_NAME_MISSING("employees.lastNameMissing"),
    POSITION_MISSING("employees.positionMissing"),
    HOUR_RATE_MISSING("employees.hourRateMissing");

    private final String code;

    EmployeeErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
