package com.pulapay.wallet;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.wallet.repository.WalletRepository;
import com.pulapay.wallet.service.WalletService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class WalletServiceTest {
    @Test
    void serviceConstructs() {
        WalletService service = new WalletService(mock(WalletRepository.class), mock(TransactionRepository.class), mock(ReferenceGenerator.class), mock(AuditLogService.class));
        assertNotNull(service);
    }
}
