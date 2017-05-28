package com.uitnetwork.transactionstatistic.model;

import static com.uitnetwork.transactionstatistic.model.TransactionStatistic.ALL_ZEROS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Test;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class TransactionStatisticUnitTest {

    private static final double TEST_LOWER_TRANSACTION_AMOUNT = 20;
    private static final double TEST_HIGHER_TRANSACTION_AMOUNT = 30;
    private static final double[] TEST_TRANSACTION_AMOUNTS = {100, 80, 10, 200, 500, 300};

    @Test
    public void staticConstantContainsAllZeros() {
        assertThat(ALL_ZEROS.getSum()).isEqualTo(0);
        assertThat(ALL_ZEROS.getAvg()).isEqualTo(0);
        assertThat(ALL_ZEROS.getMax()).isEqualTo(0);
        assertThat(ALL_ZEROS.getMin()).isEqualTo(0);
        assertThat(ALL_ZEROS.getCount()).isEqualTo(0);
    }

    @Test
    public void addSingleTransactionShouldReturnTransactionStatisticWithSameSumAvgMaxMinAsAmount() {
        double testAmount = 10;
        TransactionStatistic transactionStatistic = ALL_ZEROS.addTransaction(createTransactionWithAmount(10));

        assertThat(transactionStatistic.getSum()).isEqualTo(testAmount);
        assertThat(transactionStatistic.getAvg()).isEqualTo(testAmount);
        assertThat(transactionStatistic.getMax()).isEqualTo(testAmount);
        assertThat(transactionStatistic.getMin()).isEqualTo(testAmount);
        assertThat(transactionStatistic.getCount()).isEqualTo(1);
    }

    @Test
    public void addMultipleTransactionsShouldReturnCorrectSumAvgMaxMinAndCount() {
        TransactionStatistic transactionStatistic = ALL_ZEROS;
        List<Transaction> testTransactions = DoubleStream.of(TEST_TRANSACTION_AMOUNTS).mapToObj(this::createTransactionWithAmount).collect(toList());
        double expectedMin = DoubleStream.of(TEST_TRANSACTION_AMOUNTS).min().getAsDouble();
        double expectedMax = DoubleStream.of(TEST_TRANSACTION_AMOUNTS).max().getAsDouble();
        double expectedSum = DoubleStream.of(TEST_TRANSACTION_AMOUNTS).sum();
        double expectedAvg = DoubleStream.of(TEST_TRANSACTION_AMOUNTS).average().getAsDouble();
        long expectedCount = TEST_TRANSACTION_AMOUNTS.length;

        for (Transaction testTransaction : testTransactions) {
            transactionStatistic = transactionStatistic.addTransaction(testTransaction);
        }

        assertThat(transactionStatistic.getSum()).isEqualTo(expectedSum);
        assertThat(transactionStatistic.getAvg()).isEqualTo(expectedAvg);
        assertThat(transactionStatistic.getMax()).isEqualTo(expectedMax);
        assertThat(transactionStatistic.getMin()).isEqualTo(expectedMin);
        assertThat(transactionStatistic.getCount()).isEqualTo(expectedCount);
    }

    private Transaction createTransactionWithAmount(double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        return transaction;
    }

    @Test
    public void add2AllZeroTransactionStatisticsShouldReturnAllZeroTransactionStatistic() {
        TransactionStatistic transactionStatistic = ALL_ZEROS.addTransactionStatistic(ALL_ZEROS);

        assertThat(transactionStatistic).isEqualTo(ALL_ZEROS);
    }

    @Test
    public void returnOtherWhenThisIsAllZeroTransactionStatistic() {
        TransactionStatistic other = ALL_ZEROS.addTransaction(createTransactionWithAmount(TEST_LOWER_TRANSACTION_AMOUNT));

        TransactionStatistic addedTransactionStatistic = ALL_ZEROS.addTransactionStatistic(other);

        assertThat(addedTransactionStatistic == other).isTrue();
    }

    @Test
    public void returnThisWhenOtherIsAllZeroTransactionStatistic() {
        TransactionStatistic thizz = ALL_ZEROS.addTransaction(createTransactionWithAmount(TEST_LOWER_TRANSACTION_AMOUNT));

        TransactionStatistic addedTransactionStatistic = thizz.addTransactionStatistic(ALL_ZEROS);

        assertThat(addedTransactionStatistic == thizz).isTrue();
    }

    @Test
    public void returnTransactionStatisticWithCorrectSumMinMaxAvgAndCountWhenAddHigherToLowerTransactionStatistic() {
        doAndVerifyAdd2TransactionStatistic(TEST_LOWER_TRANSACTION_AMOUNT, TEST_HIGHER_TRANSACTION_AMOUNT);
    }

    @Test
    public void returnTransactionStatisticWithCorrectSumMinMaxAvgAndCountWhenAddLowerToHigherTransactionStatistic() {
        doAndVerifyAdd2TransactionStatistic(TEST_HIGHER_TRANSACTION_AMOUNT, TEST_LOWER_TRANSACTION_AMOUNT);
    }

    private void doAndVerifyAdd2TransactionStatistic(double thizzValue, double otherValue) {
        double expectedSum = thizzValue + otherValue;
        double expectedAvg = expectedSum / 2;
        double expectedMax = thizzValue > otherValue ? thizzValue : otherValue;
        double expectedMin = thizzValue < otherValue ? thizzValue : otherValue;

        TransactionStatistic thizz = ALL_ZEROS.addTransaction(createTransactionWithAmount(thizzValue));
        TransactionStatistic other = ALL_ZEROS.addTransaction(createTransactionWithAmount(otherValue));

        TransactionStatistic addedTransactionStatistic = thizz.addTransactionStatistic(other);

        assertThat(addedTransactionStatistic.getSum()).isEqualTo(expectedSum);
        assertThat(addedTransactionStatistic.getAvg()).isEqualTo(expectedAvg);
        assertThat(addedTransactionStatistic.getMax()).isEqualTo(expectedMax);
        assertThat(addedTransactionStatistic.getMin()).isEqualTo(expectedMin);
        assertThat(addedTransactionStatistic.getCount()).isEqualTo(2);
    }
}
