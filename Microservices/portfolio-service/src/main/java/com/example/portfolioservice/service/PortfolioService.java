package com.example.portfolioservice.service;


import com.example.portfolioservice.DAO.UserDAO;
import com.example.portfolioservice.models.*;
import com.example.portfolioservice.utils.ServiceUtils;
import com.google.common.base.Optional;
import com.sun.javafx.robot.impl.FXRobotHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
@Service
public class PortfolioService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private WebClient client;

    public String createUser(final UserParser userParser) {
        return userDAO.createUser(userParser);
    }

    public Optional<User> getUser(final String token) {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        Optional<User> user = userDAO.getUser(userId);
        if (user.isPresent()){
            List<Fund> currFunds = user.get().all_funds();
            List<Fund> updateProfitsOfFunds = new ArrayList<>();
            for (Fund fund: currFunds) {
                ClientResponse response = client.get()
                                .uri("http://localhost:8762/fund-handling/api/entitlements/get/fund?fundNumber="
                                        + fund.fundNumber())
                                .header("Authorization", "Bearer " + token)
                                .exchange()
                                .block();

                FundParser parsedFund = response.bodyToMono(FundParser.class).block();
                if (parsedFund != null) {
                    float profit = (parsedFund.nav().get()
                            - fund.originalNav().get()) * fund.quantity().get();
                    float profitPercent = (parsedFund.nav().get() - fund.originalNav().get())
                            / fund.originalNav().get();
                    Fund updatedFund = ImmutableFund.builder().fundNumber(fund.fundNumber())
                            .fundName(parsedFund.fundName()).invCurrency(parsedFund.invCurrency())
                            .invManager(parsedFund.invManager())
                            .moodysRating(parsedFund.moodysRating())
                            .originalNav(fund.originalNav())
                            .presentNav(parsedFund.nav())
                            .quantity(fund.quantity())
                            .sAndPRating(parsedFund.sAndPRating())
                            .setCycle(parsedFund.setCycle())
                            .profitAmount(profit).profitPercent(profitPercent)
                            .build();
                    updateProfitsOfFunds.add(updatedFund);
                }else {
                    updateProfitsOfFunds.add(fund);
                }
            }
            UserParser u = ImmutableUserParser.builder()
                    .userId(userId).currBal(user.get().currBal())
                    .baseCurr(user.get().baseCurr())
                    .all_funds(updateProfitsOfFunds).build();
            update(u);
            User user1 = ImmutableUser.builder()
                    .userId(userId).currBal(user.get().currBal())
                    .baseCurr(user.get().baseCurr())
                    .all_funds(updateProfitsOfFunds).build();
            user = Optional.of(user1);
            return user;
        }
        return user;
    }

    public Optional<User> update(final UserParser userParser) {
        return userDAO.update(userParser);
    }

    public Optional<User> delete(final String userId) {
        return userDAO.delete(userId);
    }


    public Optional<BalanceInfo> getBalanceById(final String userId) {
        return userDAO.getBalance(userId);
    }

    public float updateBalance(final String userId, final float balance) {
        return userDAO.updateBalance(userId, balance);
    }

    public String updateBaseCurrency(final String userId,
                                     final String newCurrency, String token)
    {
        Optional<User> user = userDAO.getUser(userId);
        float originalValue=0, newValue=0;
        ClientResponse response = client.get()
                .uri("http://localhost:8762/trade/currency/get/currency="+user.get().baseCurr())
                .header("Authorization", "Bearer "+token)
                .exchange()
                .block();
        FXRate parsedRate = response.bodyToMono(FXRate.class).block();
        if(parsedRate!=null)
        {
            originalValue = parsedRate.rate();
        }
        response = client.get()
                .uri("http://localhost:8762/trade/currency/get/currency="+newCurrency)
                .header("Authorization", "Bearer "+token)
                .exchange()
                .block();
        parsedRate = response.bodyToMono(FXRate.class).block();
        if(parsedRate!=null)
        {
            newValue = parsedRate.rate();
        }
        return userDAO.updateBaseCurrency(userId, originalValue, newValue, newCurrency);
    }
}
//String currency, float rate