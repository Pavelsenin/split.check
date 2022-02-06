package ru.senin.pk.split.check.services;

import lombok.Data;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.Transfer;
import ru.senin.pk.split.check.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service that calculates optimal transfers for check
 */
@Service
public class CheckTransfersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckTransfersService.class);

    /**
     * Calculates check transfers. Updates transfers inside of check argument
     *
     * @param check source check
     */
    public void calculateTransfers(Check check) {
        LOGGER.info("Calculate transfers for check. check: {}", check);
        // Simplify purchases matrix for each user and dividing them by payers and consumers
        List<InnerTransfer> summaryTransfers = getSummaryTransfers(check.getUsers(), check.getPurchases());
        List<InnerTransfer> payerInnerTransfers = summaryTransfers.stream()
                .filter(transfer -> Objects.nonNull(transfer.getPayerId()))
                .collect(Collectors.toList());
        List<InnerTransfer> consumerInnerTransfers = summaryTransfers.stream()
                .filter(transfer -> Objects.nonNull(transfer.getConsumerId()))
                .collect(Collectors.toList());
        // Sorting by amount to better match large amounts
        payerInnerTransfers.sort(Comparator.comparing(InnerTransfer::getAmount));
        consumerInnerTransfers.sort(Comparator.comparing(InnerTransfer::getAmount));
        // Forming payers and consumers scales for geometric comparision
        List<LinearInnerTransfer> payerLinearTransfers = getLinearTransfers(payerInnerTransfers);
        List<LinearInnerTransfer> consumerLinearTransfers = getLinearTransfers(consumerInnerTransfers);
        // Forming single scale for payers and consumers
        List<LinearInnerTransfer> unitedLinearTransfers = getUnitedLinearTransfers(payerLinearTransfers, consumerLinearTransfers);
        // Sorting transfers for better user presentation
        sortTransfers(unitedLinearTransfers, summaryTransfers);
        check.setTransfers(toResult(check.getUsers(), unitedLinearTransfers));
        LOGGER.info("Transfers calculated for check. check: {}", check);
    }

    @Data
    private static class InnerTransfer {

        private Long payerId;

        private Long consumerId;

        private long amount;
    }

    @Data
    private static class LinearInnerTransfer extends InnerTransfer {

        private long linearAmount;
    }

    private List<InnerTransfer> getSummaryTransfers(List<User> users, List<Purchase> purchases) {
        LOGGER.debug("Get summary transfers. users: {}, purchases: {}", users, purchases);
        int dimension = users.size();
        BidiMap<Long, Integer> userIndices = new DualHashBidiMap<>();
        for (int i = 0; i < dimension; i++) {
            userIndices.put(users.get(i).getId(), i);
        }
        long[][] matrix = new long[dimension][dimension];
        for (Purchase purchase : purchases) {
            int payerIndex = userIndices.get(purchase.getPayer().getId());
            long transfer = purchase.getCost()
                    .divide(new BigDecimal(purchase.getConsumers().size()), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .longValue();
            for (User consumer : purchase.getConsumers()) {
                int consumerIndex = userIndices.get(consumer.getId());
                matrix[payerIndex][consumerIndex] += transfer;
                matrix[consumerIndex][payerIndex] -= transfer;
            }
        }
        long[] accumulators = new long[dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                accumulators[i] += matrix[i][j];
            }
        }
        List<InnerTransfer> summaryTransfers = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            long summaryTransfer = accumulators[i];
            if (summaryTransfer != 0) {
                InnerTransfer transfer = new InnerTransfer();
                if (summaryTransfer > 0) {
                    transfer.setPayerId(userIndices.inverseBidiMap().get(i));
                    transfer.setAmount(summaryTransfer);
                } else if (summaryTransfer < 0) {
                    transfer.setConsumerId(userIndices.inverseBidiMap().get(i));
                    transfer.setAmount(-1 * summaryTransfer);
                }
                summaryTransfers.add(transfer);
            }
        }
        LOGGER.debug("Summary transfers received. summaryTransfers: {}", summaryTransfers);
        return summaryTransfers;
    }

    private List<LinearInnerTransfer> getUnitedLinearTransfers(List<LinearInnerTransfer> payerLinearTransfers, List<LinearInnerTransfer> consumerLinearTransfers) {
        LOGGER.debug("Get united linear transfers. payerLinearTransfers: {}, consumerLinearTransfers: {}", payerLinearTransfers, consumerLinearTransfers);
        List<Long> unitedLinearAmounts = new ArrayList<>(payerLinearTransfers.size() + consumerLinearTransfers.size());
        unitedLinearAmounts.addAll(payerLinearTransfers.stream().map(LinearInnerTransfer::getLinearAmount).collect(Collectors.toList()));
        unitedLinearAmounts.addAll(consumerLinearTransfers.stream().map(LinearInnerTransfer::getLinearAmount).collect(Collectors.toList()));
        unitedLinearAmounts = unitedLinearAmounts.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        List<LinearInnerTransfer> unitedLinearTransfers = new ArrayList<>(unitedLinearAmounts.size());
        for (int ui = 0, pi = 0, ci = 0; ui < unitedLinearAmounts.size(); ui++) {
            LinearInnerTransfer linearTransfer = new LinearInnerTransfer();
            linearTransfer.setLinearAmount(unitedLinearAmounts.get(ui));
            while (payerLinearTransfers.get(pi).getLinearAmount() < linearTransfer.getLinearAmount()) {
                pi++;
            }
            linearTransfer.setPayerId(payerLinearTransfers.get(pi).getPayerId());
            while (consumerLinearTransfers.get(ci).getLinearAmount() < linearTransfer.getLinearAmount()) {
                ci++;
            }
            linearTransfer.setConsumerId(consumerLinearTransfers.get(ci).getConsumerId());
            if (ui == 0) {
                linearTransfer.setAmount(linearTransfer.getLinearAmount());
            } else {
                linearTransfer.setAmount(linearTransfer.getLinearAmount() - unitedLinearTransfers.get(ui - 1).getLinearAmount());
            }
            unitedLinearTransfers.add(linearTransfer);
        }
        LOGGER.debug("United linear transfer received. unitedLinearTransfers: {}", unitedLinearTransfers);
        return unitedLinearTransfers;
    }

    private List<LinearInnerTransfer> getLinearTransfers(List<InnerTransfer> innerTransfers) {
        LOGGER.debug("Get linear transfers. innerTransfers: {}", innerTransfers);
        List<LinearInnerTransfer> linearTransfers = new ArrayList<>(innerTransfers.size());
        long previousLinearAmount = 0;
        for (InnerTransfer innerTransfer : innerTransfers) {
            LinearInnerTransfer linearTransfer = new LinearInnerTransfer();
            linearTransfer.setPayerId(innerTransfer.getPayerId());
            linearTransfer.setConsumerId(innerTransfer.getConsumerId());
            linearTransfer.setAmount(innerTransfer.getAmount());
            linearTransfer.setLinearAmount(previousLinearAmount + innerTransfer.getAmount());
            linearTransfers.add(linearTransfer);
            previousLinearAmount = linearTransfer.getLinearAmount();
        }
        LOGGER.debug("Linear transfers formed. linearTransfers: {}", linearTransfers);
        return linearTransfers;
    }
    
    /**
     * Sorting transfers for better user presentation
     * 1) payer id - to group transfers by consumer
     * 2) amount desc
     *
     * @param unitedLinearTransfers
     * @param summaryTransfers
     */
    private void sortTransfers(List<? extends InnerTransfer> unitedLinearTransfers, List<InnerTransfer> summaryTransfers) {
        LOGGER.debug("Sort transfers. unitedLinearTransfers: {}, summaryTransfers: {}", unitedLinearTransfers, summaryTransfers);
        Map<Long, Long> summaryAmountsByConsumer = summaryTransfers.stream()
                .filter(transfer -> Objects.nonNull(transfer.getConsumerId()))
                .collect(Collectors.toMap(innerTransfer -> innerTransfer.getConsumerId(), innerTransfer -> innerTransfer.getAmount()));
        Collections.sort(unitedLinearTransfers, (o1, o2) -> {
            if (Objects.equals(o1.getConsumerId(), o2.getConsumerId())) {
                return Long.compare(o1.getAmount(), o2.getAmount());
            } else {
                return summaryAmountsByConsumer.get(o2.getConsumerId()).compareTo(summaryAmountsByConsumer.get(o1.getConsumerId()));
            }
        });
        LOGGER.debug("Transfers sorted. unitedLinearTransfers: {}", unitedLinearTransfers);
    }

    private List<Transfer> toResult(List<User> users, List<? extends InnerTransfer> innerTransfers) {
        List<Transfer> result = new ArrayList<>(innerTransfers.size());
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        for (InnerTransfer innerTransfer : innerTransfers) {
            Transfer resultTransfer = new Transfer();
            resultTransfer.setPayer(userMap.get(innerTransfer.getPayerId()));
            resultTransfer.setConsumer(userMap.get(innerTransfer.getConsumerId()));
            resultTransfer.setAmount(BigDecimal.valueOf(innerTransfer.getAmount(), 2));
            result.add(resultTransfer);
        }
        return result;
    }
}
