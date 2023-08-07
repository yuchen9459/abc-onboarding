package com.abc.onboarding.customer;

import com.abc.onboarding.common.exception.ErrorResponse;
import com.abc.onboarding.customer.entity.Customer;
import com.abc.onboarding.customer.service.CustomerService;
import com.abc.onboarding.payload.request.LoginRequest;
import com.abc.onboarding.payload.request.RegistrationRequest;
import com.abc.onboarding.payload.response.CustomerOverViewResponse;
import com.abc.onboarding.payload.response.CustomerRegistrationResponse;
import com.abc.onboarding.security.jwt.JwtUtils;
import com.abc.onboarding.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name = "ABC Customer Onboarding API")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public CustomerController(CustomerService customerService,
                              AuthenticationManager authenticationManager,
                              JwtUtils jwtUtils) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "Register customer",
               description = "Returns the username and auto-generated password of registered customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409",
                         content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<CustomerRegistrationResponse> createCustomer(@Valid @RequestBody RegistrationRequest request) {
        CustomerRegistrationResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Customer log on",
               description = "Create a successful session if the credentials are correct")
    @PostMapping("/logon")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                                      loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
    }

    @Operation(summary = "Retrieve customer overview",
               description = "Return the customer overview if the session is authenticated")
    @GetMapping("/overview")
    public ResponseEntity<CustomerOverViewResponse> getUserOverview(Principal principal) {
        Customer customer = customerService.findCustomerByUsername(principal.getName());
        return ResponseEntity.ok().body(CustomerOverViewResponse.of(customer));
    }
}
