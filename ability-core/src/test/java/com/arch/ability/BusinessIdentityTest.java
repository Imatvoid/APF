package com.arch.ability;

import com.arch.ability.identity.BusinessIdentity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BusinessIdentityTest {

    @Test
    public void build() {
        BusinessIdentity businessIdentity = BusinessIdentity.build();
        assertEquals("BusinessIdentity {sellerNo='null', businessUnitNo='null'}", businessIdentity.toString());
        assertEquals("d", businessIdentity.withBusinessUnitNo("d").getBusinessUnitNo());
        businessIdentity.withSellerAndBusinessUnitNo("s1", "d1");
        assertEquals("s1", businessIdentity.getSellerNo());
        assertEquals("d1", businessIdentity.getBusinessUnitNo());
        assertEquals("BusinessIdentity {sellerNo='s1', businessUnitNo='d1'}", businessIdentity.toString());
    }

}