package com.expense.service.service;

import com.expense.service.dto.ExpenseDto;
import com.expense.service.entities.Expense;
import com.expense.service.repository.ExpenseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public boolean createExpense(ExpenseDto expenseDto) {
        setCurrency(expenseDto);
        try{
            expenseRepository.save(objectMapper.convertValue(expenseDto, Expense.class));
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    public boolean updateExpense(ExpenseDto expenseDto) {
        Optional<Expense> expenseFoundOpt = expenseRepository.findByUserIdAndExternalID(expenseDto.getUserId(), expenseDto.getExternalID());
        if(expenseFoundOpt.isEmpty()){
            return false;
        }
        Expense expenseFound = expenseFoundOpt.get();
        expenseFound.setCurrency(expenseDto.getCurrency());
        expenseFound.setMerchant(expenseDto.getMerchant());
        expenseFound.setAmount(expenseDto.getAmount());

        expenseRepository.save(expenseFound);

        return true;
    }

    public List<ExpenseDto> getExpenses(String userId) {
        List<Expense> expenses = (List<Expense>) expenseRepository.findByUserId(userId);
        return objectMapper.convertValue(expenses, new TypeReference<List<ExpenseDto>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

    private void setCurrency(ExpenseDto expenseDto){
        // Set the currency for the expense
        if(Objects.isNull(expenseDto.getCurrency())){
            expenseDto.setCurrency("USD");
        }

    }


}
