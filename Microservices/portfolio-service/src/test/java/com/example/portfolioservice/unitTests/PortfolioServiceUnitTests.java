//package com.example.portfolioservice.unitTests;
//import com.example.portfolioservice.DAO.UserDAO;
//import com.example.portfolioservice.models.*;
//import static org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//import com.google.common.base.Optional;
//import com.example.portfolioservice.service.PortfolioService;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//public class PortfolioServiceUnitTests {
//    @Mock
//    private UserDAO dao;
//
//    @InjectMocks
//    private PortfolioService portfolioService;
//
//    private User user;
//    private UserParser userParser;
//    private BalanceInfo balance = new BalanceInfo();
//    private String token;
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Before
//    public void initialize() {
//        List<Fund> funds =  new ArrayList<>();
//
//        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb21idWRkaGEyMCIsImF1dGhvcml0"
//                + "aWVzIjpbIlJPTEVfVFJBREVSIl0sImlhdCI6MTU2MjIzNjY4OSwibmFtZSI6"
//                + "IlNvbWJ1ZGRoYSBDaGFrcmF2YXJ0aHkiLCJleHAiOjE1NjIyNDAyODl9.oct7"
//                + "z-0NzNkgXbqW6mipjkiA6_UsLnIrO_yDYZVX8TvM5MqftwN525gtdVYtO45jbY"
//                + "mqFT74DXanArHkYEGLoQ";
//        user = ImmutableUser.builder()
//                .userId(token)
//                .currBal(34)
//                .all_funds(funds)
//                .baseCurr("INR").build();
//        userParser = ImmutableUserParser.builder()
//                .userId(token)
//                .currBal(34)
//                .all_funds(funds)
//                .baseCurr("INR").build();
//        this.balance.setCurrBal((float)34);
//        this.balance.setBaseCurr("INR");
//    }
//
//    @Test
//    public void test_createUser() {
//        Mockito.doReturn("UserParser Created").when(dao).createUser(userParser);
//        String message = portfolioService.createUser(userParser);
//        assertEquals(message, "UserParser Created");
//    }
//
//    @Test
//    public void test_getUser() {
//        Mockito.doReturn(Optional.of(this.user)).when(dao).getUser(Mockito.anyString());
//        Optional<User> user = Optional.of(this.user);
//        User user2 = ImmutableUser.builder()
//                .userId("sombuddha20")
//                .currBal(34)
//                .all_funds(this.user.all_funds())
//                .baseCurr("INR").build();
//        user = portfolioService.getUser(token);
//        assertEquals(user.get(), user2);
//
//    }
//
//    @Test
//    public void test_update() {
//        Optional<User> user = Optional.of(this.user);
//        Mockito.doReturn(user).when(dao).update(this.userParser);
//        Optional<User> user2 = portfolioService.update(this.userParser);
//        assertEquals(user, user2);
//    }
//
//    @Test
//    public void test_delete() {
//        Optional<User> user = Optional.of(this.user);
//        Mockito.doReturn(user).when(dao).delete("1");
//        Optional<User> user2 = portfolioService.delete("1");
//        assertEquals(user, user2);
//
//    }
//
//    @Test
//    public void test_getBalanceById() {
//        Mockito.doReturn(Optional.of(this.balance)).when(dao).getBalance("1");
//        Optional<BalanceInfo> balance = portfolioService.getBalanceById("1");
//        assertEquals(balance.get(), this.balance);
//    }
//
//    @Test
//    public void test_updateBalance() {
//        Mockito.doReturn((float)43).when(dao).updateBalance("1", (float)34);
//        float balance = portfolioService.updateBalance("1", (float)34);
//        assert(balance == 43);
//    }
//
//    @Test
//    public void test_updateBaseCurrency() {
//        Mockito.when(dao.updateBaseCurrency(Mockito.anyString(),Mockito.anyString())).thenReturn
//                (userParser.baseCurr().get());
//        String baseCurr = portfolioService.updateBaseCurrency("1", "INR");
//        assertEquals(baseCurr, userParser.baseCurr().get());
//    }
//
//}
