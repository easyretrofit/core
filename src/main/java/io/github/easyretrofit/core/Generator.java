package io.github.easyretrofit.core;

/**
 * used internally, when you want to generate a complex object.
 * @author liuziyuan
 */
public interface Generator<T> {
    /**
     * To generate what you want
     * @return T
     */
    T generate();
}
