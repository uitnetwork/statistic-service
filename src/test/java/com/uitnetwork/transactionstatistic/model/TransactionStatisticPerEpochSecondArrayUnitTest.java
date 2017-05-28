package com.uitnetwork.transactionstatistic.model;

import static com.uitnetwork.transactionstatistic.model.TransactionStatistic.ALL_ZEROS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.DoubleStream;

import org.junit.Test;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class TransactionStatisticPerEpochSecondArrayUnitTest {
    private static final int TEST_ARRAY_SIZE = 5;
    private static final long[] TEST_EPOCH_SECONDS_SORT_ASC = {1478192204, 1478192205, 1478192206, 1478192207, 1478192208};
    private static final double[] TEST_AMOUNTS = {50, 20, 30, 40, 70};
    private static final int TEST_INDEX = 1;

    @Test
    public void constructorShouldInitialzeArrayWithCorespondingSizeAndAllZeroTransactionStatistic() {
        TransactionStatisticPerEpochSecondArray transactionStatisticPerEpochSecondArray = new TransactionStatisticPerEpochSecondArray(TEST_ARRAY_SIZE);

        for (int i = 0; i < TEST_ARRAY_SIZE; ++i) {
            TransactionStatisticPerEpochSecond transactionStatisticPerEpochSecond = transactionStatisticPerEpochSecondArray.get(i);
            assertThat(transactionStatisticPerEpochSecond.getEpochSecond()).isEqualTo(0);
            assertThat(transactionStatisticPerEpochSecond.getTransactionStatistic().getCount()).isEqualTo(0);
            assertThat(transactionStatisticPerEpochSecond.getTransactionStatistic().getSum()).isEqualTo(0);
            assertThat(transactionStatisticPerEpochSecond.getTransactionStatistic().getMin()).isEqualTo(0);
            assertThat(transactionStatisticPerEpochSecond.getTransactionStatistic().getMax()).isEqualTo(0);
            assertThat(transactionStatisticPerEpochSecond.getTransactionStatistic().getAvg()).isEqualTo(0);
        }
    }

    @Test
    public void sumAllAboveOrEqualEpochSecondShouldReturnTransactionStatisticInCorrectEpochSecond() {
        TransactionStatisticPerEpochSecondArray transactionStatisticPerEpochSecondArray = new TransactionStatisticPerEpochSecondArray(TEST_ARRAY_SIZE);
        double[] expectedAmountsArray = Arrays.copyOfRange(TEST_AMOUNTS, TEST_INDEX, TEST_AMOUNTS.length);
        long expectedCount = expectedAmountsArray.length;
        double expectedSum = DoubleStream.of(expectedAmountsArray).sum();
        double expectedMax = DoubleStream.of(expectedAmountsArray).max().getAsDouble();
        double expectedMin = DoubleStream.of(expectedAmountsArray).min().getAsDouble();
        double expectedAvg = DoubleStream.of(expectedAmountsArray).average().getAsDouble();

        for (int i = 0; i < TEST_EPOCH_SECONDS_SORT_ASC.length; ++i) {
            transactionStatisticPerEpochSecondArray.set(i, createTransactionStatisticPerEpochSecond(TEST_EPOCH_SECONDS_SORT_ASC[i], TEST_AMOUNTS[i]));
        }

        TransactionStatistic transactionStatistic =
                transactionStatisticPerEpochSecondArray.sumAllAboveOrEqualEpochSecond(TEST_EPOCH_SECONDS_SORT_ASC[TEST_INDEX]);
        assertThat(transactionStatistic.getCount()).isEqualTo(expectedCount);
        assertThat(transactionStatistic.getSum()).isEqualTo(expectedSum);
        assertThat(transactionStatistic.getMax()).isEqualTo(expectedMax);
        assertThat(transactionStatistic.getMin()).isEqualTo(expectedMin);
        assertThat(transactionStatistic.getAvg()).isEqualTo(expectedAvg);

    }


    private TransactionStatisticPerEpochSecond createTransactionStatisticPerEpochSecond(long epochSecond, double amount) {
        TransactionStatistic transactionStatistic = ALL_ZEROS.addTransaction(createTransactionWithAmount(amount));
        return new TransactionStatisticPerEpochSecond(epochSecond, transactionStatistic);
    }

    private Transaction createTransactionWithAmount(double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        return transaction;
    }

}
