package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;
import de.othr.sw.lohrbank.entity.Transaction;

/**
 *
 * @author Michael
 */
public interface ITransactionService {
    Receipt CreateTransaction(Transaction transaction);
    Receipt CreateDebit(Debit debit);
}
