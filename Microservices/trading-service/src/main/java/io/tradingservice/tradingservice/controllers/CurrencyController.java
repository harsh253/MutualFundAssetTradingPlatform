package io.tradingservice.tradingservice.controllers;


import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.models.FXRateParser;
import io.tradingservice.tradingservice.services.CurrencyService;
import io.tradingservice.tradingservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/currency")
@Consumes("application/json")
@Produces("application/json")
public class CurrencyController {

    @Autowired
    private CurrencyService service;

    @POST
    @Path(Constants.addCurrencyEndpoint)
    public Response addCurr(FXRateParser fxRate){
        System.out.println("Here");
        return service.addCurrency(fxRate);
    }

    @GET
    @Path(Constants.getCurrencyEndpoint)
    public FXRate getCurr(@QueryParam("currency") String currency){
        return service.getCurrency(currency);
    }

    @GET
    @Path(Constants.getAllCurrencyEndpoint)
    public List<FXRate> getAllCurr(){
        return service.getAll();
    }

    @POST
    @Path(Constants.updateCurrencyEndpoint)
    public Response updateCurr(FXRateParser parser){
        return service.updateCurrency(parser);
    }
}