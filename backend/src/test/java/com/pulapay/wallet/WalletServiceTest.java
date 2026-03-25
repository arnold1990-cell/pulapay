package com.pulapay.wallet;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.user.entity.User;
import com.pulapay.wallet.dto.WalletBalanceResponse;
import com.pulapay.wallet.dto.WalletResponse;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.entity.WalletStatus;
import com.pulapay.wallet.repository.WalletRepository;
import com.pulapay.wallet.service.WalletService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletServiceTest {
    @Test
    void getWalletAndBalanceReturnsCurrentUserWallet() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);

        WalletService service = new WalletService(walletRepository, transactionRepository, mock(ReferenceGenerator.class), mock(AuditLogService.class));

        User user = new User();
        user.setPhoneNumber("SYS999");

        Wallet wallet = new Wallet();
        wallet.setWalletNumber("2600011111");
        wallet.setBalance(new BigDecimal("150.00"));
        wallet.setCurrency("BWP");
        wallet.setStatus(WalletStatus.ACTIVE);

        when(walletRepository.findByUserPhoneNumber("SYS999")).thenReturn(Optional.of(wallet));

        WalletResponse walletResponse = service.getWallet(user);
        WalletBalanceResponse balanceResponse = service.getBalance(user);

        assertEquals("2600011111", walletResponse.walletNumber());
        assertEquals(new BigDecimal("150.00"), walletResponse.balance());
        assertEquals(new BigDecimal("150.00"), balanceResponse.balance());
    }
}
