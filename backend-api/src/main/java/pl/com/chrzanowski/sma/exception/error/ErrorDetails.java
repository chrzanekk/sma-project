package pl.com.chrzanowski.sma.exception.error;

import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorDetails {
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;


    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    private ErrorDetails(Builder builder) {
        timestamp = builder.timestamp;
        message = builder.message;
        details = builder.details;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ErrorDetails copy) {
        Builder builder = new Builder();
        builder.timestamp = copy.getTimestamp();
        builder.message = copy.getMessage();
        builder.details = copy.getDetails();
        return builder;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDetails that = (ErrorDetails) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(message, that.message) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, message, details);
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                '}';
    }


    public static final class Builder {
        private LocalDateTime timestamp;
        private String message;
        private String details;

        private Builder() {
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(this);
        }
    }
}
