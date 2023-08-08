package com.abc.onboarding.handlers;

import com.abc.onboarding.common.exception.BadRequestException;
import com.abc.onboarding.common.exception.ConflictedUsernameException;
import com.abc.onboarding.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
@ApiResponses(value = {
        @ApiResponse(responseCode = "400",
                     content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                     content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500",
                     content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ErrorResponse.class)))})
@EnableConfigurationProperties({ServerProperties.class})
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final String BAD_REQUEST_MESSAGE = "400 BadRequest: {}";
    private static final String UNAUTH_REQUEST_MESSAGE = "401 UnauthorizedRequest: {}";
    private static final String CONFLICTED_REQUEST_MESSAGE = "409 ConflictedRequest: {}";

    private static final String CODE = "code";
    private static final String ERROR = "error";
    private static final String STATUS = "status";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;

    @ExceptionHandler(Throwable.class)
    public Map<String, Object> handleAllOtherExceptions(Throwable exception,
                                                        WebRequest request,
                                                        HttpServletResponse response) {
        LOGGER.error("Unhandled exception thrown from controller method: {}", exception.getMessage(), exception);
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus httpStatus = annotation != null ? annotation.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(httpStatus.value());
        return createErrorResponse(request, "", httpStatus);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, Object> handleBadRequestException(BadRequestException e, WebRequest request) {
        LOGGER.error(BAD_REQUEST_MESSAGE, e.getMessage(), e);
        return createErrorResponse(request, e.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictedUsernameException.class)
    public Map<String, Object> handleConflictedUsernameException(ConflictedUsernameException e, WebRequest request) {
        LOGGER.error(CONFLICTED_REQUEST_MESSAGE, e.getMessage(), e);
        return createErrorResponse(request, e.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleRequiredBodyIsMissingException(HttpMessageNotReadableException e,
                                                                    WebRequest request) {
        LOGGER.error(BAD_REQUEST_MESSAGE, e.getMessage(), e);
        return createErrorResponse(request, "", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String, Object> handleBadCredentialsException(BadCredentialsException e,
                                                             WebRequest request) {
        LOGGER.error(UNAUTH_REQUEST_MESSAGE, e.getMessage(), e);
        return createErrorResponse(request, "", HttpStatus.UNAUTHORIZED);
    }

    protected Map<String, Object> createErrorResponse(WebRequest request, String errorCode, HttpStatus status) {
        Map<String, Object> errorResponse = constructErrorResponse(request);
        errorResponse.put(CODE, errorCode);
        errorResponse.put(ERROR, status.getReasonPhrase());
        errorResponse.put(STATUS, status.value());
        return errorResponse;
    }

    protected boolean isIncludeStackTrace() {
        return ErrorProperties.IncludeAttribute.ALWAYS == serverProperties.getError().getIncludeStacktrace();
    }

    private Map<String, Object> constructErrorResponse(WebRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (isIncludeStackTrace()) {
            options = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        try {
            return errorAttributes.getErrorAttributes(request, options);
        } catch (Exception e) {
            LOGGER.warn("Unable to get error attributes", e);
            return new HashMap<>();
        }
    }
}
