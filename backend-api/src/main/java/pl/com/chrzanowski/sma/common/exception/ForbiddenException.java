package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class ForbiddenException extends CustomException {
  public ForbiddenException(String message, Map<String, Object> details) {
    super(ErrorCode.FORBIDDEN, message, details);
  }

  public ForbiddenException(String message) {
    this(message, null);
  }
}
