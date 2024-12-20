package pl.com.chrzanowski.sma.common.exception.error;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ErrorDetails {
    private final LocalDateTime timestamp;
    private final String code;
    private final String message;
    private final Map<String, Object> details; // Zmiana z String na Map<String, Object>

    private ErrorDetails(Builder builder) {
        timestamp = builder.timestamp;
        code = builder.code;
        message = builder.message;
        details = builder.details;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ErrorDetails copy) {
        Builder builder = new Builder();
        builder.timestamp = copy.getTimestamp();
        builder.code = copy.getCode();
        builder.message = copy.getMessage();
        builder.details = copy.getDetails();
        return builder;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDetails that = (ErrorDetails) o;
        return Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, code, message, details);
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "timestamp=" + timestamp +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", details=" + details +
                '}';
    }

    public static final class Builder {
        private LocalDateTime timestamp;
        private String code;
        private String message;
        private Map<String, Object> details; // Zmiana typu na Map<String, Object>

        private Builder() {
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(Map<String, Object> details) {
            this.details = details;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(this);
        }
    }
}
