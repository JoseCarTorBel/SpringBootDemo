package com.netmind.accountsservice.controller;


import com.netmind.accountsservice.model.Account;
import com.netmind.accountsservice.model.AccountBalanceDTO;
import com.netmind.accountsservice.persistence.AccountRepository;
import com.netmind.accountsservice.services.AccountService;
import com.netmind.accountsservice.services.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/account")
@Validated
//@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
@Tag(name = "Account API", description = "API de los productos")
public class AccountControllerService {

    @Autowired
    private IAccountService service;

   // private static final Logger logger = LoggerFactory.getLogger(ProductServiceController.class);

    @GetMapping(value="",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAll(){
        return new ResponseEntity<>(service.getAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    @Operation(summary = "Get a product by id", description = "Returns a product as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    public ResponseEntity<Account> getAProduct(
            @PathVariable(name = "pid") @Min(1) Long id) {
        return new ResponseEntity<>(service.getAccount(id), HttpStatus.OK);
    }

    @PostMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Add a new account", description = "Returns a presisted account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created"),
            @ApiResponse(responseCode = "4XX", description = "Bad request")
    })
    public ResponseEntity<Account> addAccount( @RequestBody Account account){
        return new ResponseEntity<>(service.create(account), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> updateProduct(@PathVariable @Min(1) Long id, @RequestBody Account account) {
        account.setId(id);
        return new ResponseEntity(
                service.updateAccount(id, account),
                HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteProduct(@PathVariable @Min(1) Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/addbalance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> addBalance(
            @RequestBody  @Valid AccountBalanceDTO accountBalanceDTO
    ){
        return new ResponseEntity<>(
                service.addBalance(accountBalanceDTO.id, accountBalanceDTO.amount, accountBalanceDTO.ownerId),
                HttpStatus.ACCEPTED
        );
    }

    @PutMapping(value="/{id}/{amount}/{owner}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> withdrawBalance(
            @PathVariable @Min(1) long id,
            @PathVariable @Min(0) int amount,
            @PathVariable @Min(1) long owner){
        return new ResponseEntity<>(
                service.withdrawBalance(id, amount, owner),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value="/users/{id}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAccountsUsingOwnerId(@PathVariable long id){
        service.deleteAccountsUsingOwnerId(id);
        return ResponseEntity.noContent().build();
    }
}
