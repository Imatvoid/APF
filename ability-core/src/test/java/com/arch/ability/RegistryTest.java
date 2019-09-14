package com.arch.ability;

import com.arch.ability.mock.DefaultFooPlugin;
import com.arch.ability.mock.FooAbilityPoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegistryTest {

    @Test
    public void getExtPtInterfaceClassRecursive() {
        Class extPt = Registry.getAbilityPointInterfaceClassRecursive(DefaultFooPlugin.class);
        assertEquals(FooAbilityPoint.class, extPt);
        assertEquals(FooAbilityPoint.class, Registry.getAbilityPointInterfaceClassRecursive(DefaultFooPlugin.class));
    }

}