package xyz.ivan.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.ivan.demo.po.Address;


@SpringBootTest
class IAddressServiceTest {
    @Autowired
    private IAddressService addressService;

    @Test
    void testDelete(){
        addressService.removeById(59L);

        Address address = addressService.getById(59L);
        System.out.println(address);
    }
}