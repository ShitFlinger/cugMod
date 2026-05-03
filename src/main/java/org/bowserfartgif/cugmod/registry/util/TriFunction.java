package org.bowserfartgif.cugmod.registry.util;

@FunctionalInterface
public interface TriFunction<A, B, C, T> {
    
    T apply(A a, B b, C c);
    
}
