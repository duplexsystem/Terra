package com.dfsek.terra.math;

import org.polydev.gaea.util.FastRandom;
import parsii.eval.Expression;
import parsii.eval.Function;

import java.util.List;

/**
 * Provides access to a PRNG ({@link org.polydev.gaea.util.FastRandom})
 * <p>
 * Takes 1 argument, which sets the seed
 */
public class RandomFunction implements Function {
    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public double eval(List<Expression> list) {
        long seed = (long) list.get(0).evaluate();
        return new FastRandom(seed).nextDouble();
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
