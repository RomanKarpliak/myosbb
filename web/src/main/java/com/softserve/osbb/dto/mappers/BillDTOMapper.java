/*
 * Project “OSBB” – a web-application which is a godsend for condominium head, managers and 
 * residents. It offers a very easy way to manage accounting and residents, events and 
 * organizational issues. It represents a simple design and great functionality that is needed 
 * for managing. 
 */
package com.softserve.osbb.dto.mappers;

import com.softserve.osbb.dto.BillDTO;
import com.softserve.osbb.model.Bill;
import com.softserve.osbb.model.enums.BillStatus;
import com.softserve.osbb.service.BillService;

/**
 * Created by nazar.dovhyy on 18.08.2016.
 */
public class BillDTOMapper {

    public static BillDTO mapEntityToDTO(Bill bill) {
        BillDTO billDTO = null;
        
        if (bill != null) {
            billDTO = new BillDTO.BillDTOBuilder()
                    .setBillId(bill.getBillId())
                    .setName(bill.getName())
                    .setDate(bill.getDate())
                    .setApartmentNumber(bill.getApartment())
                    .setPaid(bill.getPaid())
                    .setToPay(bill.getToPay())
                    .setTariff(bill.getTariff())
                    .setStatus(bill.getBillStatus())
                    .setApartmentId(bill.getApartment())
                    .setProviderId(bill.getProvider())
                    .setParentBillId(bill.getParentBill())
                    .build();
        }
        return billDTO;
    }

    public static Bill mapDTOtoEntity(BillDTO billDTO, BillService billService) {
        Bill bill = null;
        
        if (billDTO != null) {
            if (billDTO.getBillId() == null) {
                bill = new Bill();
                createNewBillFromBillDTO(billDTO, bill);
                return bill;
            }
            if (billService == null) {
                throw new IllegalArgumentException("no instance of billService provided");
            }
            bill = billService.findOneBillByID(billDTO.getBillId());
            mapFromBillDTOTOBill(billDTO, bill);
        }
        return bill;
    }

    private static void createNewBillFromBillDTO(BillDTO billDTO, Bill bill) {
    	bill.setName(billDTO.getName());
        bill.setTariff(billDTO.getTariff());
        bill.setToPay(billDTO.getToPay());
        bill.setPaid(billDTO.getPaid());
        bill.setDate(billDTO.getDate());
        bill.setBillStatus(BillStatus.NOT_PAID);
    }

    private static void mapFromBillDTOTOBill(BillDTO billDTO, Bill bill) {
    	bill.setName(billDTO.getName());
    	bill.setTariff(billDTO.getTariff());
        bill.setBillStatus(billDTO.getStatus().equals(BillStatus.PAID.toString()) 
                ? BillStatus.PAID 
                : BillStatus.NOT_PAID);
        bill.setDate(billDTO.getDate());
        bill.setPaid(billDTO.getPaid());
        bill.setToPay(billDTO.getToPay());
    }
}
