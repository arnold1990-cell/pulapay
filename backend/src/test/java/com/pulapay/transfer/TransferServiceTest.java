package com.pulapay.transfer;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.InsufficientFundsException;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.transfer.dto.TransferRequest;
import com.pulapay.transfer.service.TransferService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.entity.WalletStatus;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransferServiceTest {
    @Test
    void transferThrowsWhenInsufficientFunds() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TransferService service = new TransferService(walletRepository, userRepository, mock(TransactionRepository.class), mock(ReferenceGenerator.class), mock(AuditLogService.class));

        User sender = new User();
        sender.setPhoneNumber("SYS111");
        sender.setRole(Role.USER);

        User recipient = new User();
        recipient.setPhoneNumber("SYS222");

        Wallet senderWallet = new Wallet();
        senderWallet.setBalance(new BigDecimal("5.00"));
        senderWallet.setStatus(WalletStatus.ACTIVE);

        Wallet recipientWallet = new Wallet();
        recipientWallet.setBalance(new BigDecimal("0.00"));
        recipientWallet.setStatus(WalletStatus.ACTIVE);

        when(walletRepository.findByUserPhoneNumber("SYS111")).thenReturn(Optional.of(senderWallet));
        when(userRepository.findByPhoneNumber("SYS222")).thenReturn(Optional.of(recipient));
        when(walletRepository.findByUserPhoneNumber("SYS222")).thenReturn(Optional.of(recipientWallet));

        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(sender, new TransferRequest("SYS222", new BigDecimal("10.00"), "Rent")));
    }
}
