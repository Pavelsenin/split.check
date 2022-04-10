package ru.senin.pk.split.check.services;

import ru.senin.pk.split.check.model.Check;

/**
 * Service that calculates optimal transfers for check
 */
public interface CheckTransfersService {

    /**
     * Calculates check transfers. Updates transfers inside of check argument
     *
     * @param check source check
     */
    void calculateTransfers(Check check);
}
