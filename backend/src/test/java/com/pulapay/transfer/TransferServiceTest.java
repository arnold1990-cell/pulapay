package com.pulapay.transfer;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.transfer.service.TransferService;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class TransferServiceTest {
    @Test
    void serviceConstructs() {
        TransferService service = new TransferService(mock(WalletRepository.class), mock(UserRepository.class), mock(TransactionRepository.class), mock(ReferenceGenerator.class), mock(AuditLogService.class));
        assertNotNull(service);
    }
}
