package com.dfsek.terra.config.loading.loaders;

import com.dfsek.terra.config.loading.ClassLoader;

/**
 * Default loader for Double types.
 */
public class DoubleLoader implements ClassLoader<Double> {
    @Override
    public Double load(Object c) {
        return Double.parseDouble(c.toString());
    }
}