package com.nacosfunaabpay.paymentplatform.mappers.implementations;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentDTO;
import com.nacosfunaabpay.paymentplatform.dtos.ReceiptDTO;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper implements Mapper<Receipt, ReceiptDTO> {
    private ModelMapper modelMapper;

    public ReceiptMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ReceiptDTO mapTo(Receipt receipt) {
        return modelMapper.map(receipt, ReceiptDTO.class);
    }

    @Override
    public Receipt mapFrom(ReceiptDTO receiptDTO) {
        return modelMapper.map(receiptDTO, Receipt.class);
    }
}
