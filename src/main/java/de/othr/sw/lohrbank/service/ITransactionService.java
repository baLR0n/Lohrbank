package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;

/**
 *
 * @author Michael
 */
public interface ITransactionService {
    Receipt CreateTransaction(Long customerAccountId, Long targetAccountId, String message, double value);
    Receipt CreateDebit(Debit debit);
}
