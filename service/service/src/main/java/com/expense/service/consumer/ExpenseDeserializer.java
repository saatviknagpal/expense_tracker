package com.expense.service.consumer;

import com.expense.service.dto.ExpenseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {

    @Override
    public void close(){};

    @Override
    public void configure(java.util.Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public ExpenseDto deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseDto expenseDto = null;
        try{
            expenseDto = objectMapper.readValue(data, ExpenseDto.class);
        }
       catch (IOException e) {
            throw new RuntimeException("Failed to deserialize ExpenseDto", e);
        }
        return expenseDto;
    }
}
