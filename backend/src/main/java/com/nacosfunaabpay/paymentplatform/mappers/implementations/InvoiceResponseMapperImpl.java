package com.nacosfunaabpay.paymentplatform.mappers.implementations;

import com.nacosfunaabpay.paymentplatform.dtos.InvoiceResponseDTO;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResponseMapperImpl implements Mapper<Invoice, InvoiceResponseDTO> {

    private ModelMapper modelMapper;

    public InvoiceResponseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public InvoiceResponseDTO mapTo(Invoice invoice) {
       return  modelMapper.map(invoice, InvoiceResponseDTO.class);
    }

    @Override
    public Invoice mapFrom(InvoiceResponseDTO invoiceResponseDTO) {
        return modelMapper.map(invoiceResponseDTO, Invoice.class);
    }
}
