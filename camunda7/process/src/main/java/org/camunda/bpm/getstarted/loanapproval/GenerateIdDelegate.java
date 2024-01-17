package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.UUID;
import java.util.logging.Logger;

public class GenerateIdDelegate implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger("GENERATE-ID");


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        UUID uuid = UUID.randomUUID();

        LOGGER.info("ID Generated: " + uuid.toString());

        delegateExecution.setVariable("id", uuid.toString());
    }
}
