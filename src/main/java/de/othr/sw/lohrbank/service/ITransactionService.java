package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.entity.Receipt;
import java.util.Date;

/**
 *
 * @author Michael
 */
public interface ITransactionService {
    Receipt CreateTransaction(Customer customer, Long customerAccountId, Long targetAccountId, String message, double value);
    Receipt CreateDebit(Customer customer, Long customerAccountId, Long targetAccountId, String message, double value, Date date);
}
