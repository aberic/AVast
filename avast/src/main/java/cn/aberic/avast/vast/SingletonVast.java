package cn.aberic.avast.vast;

/**
 * Singleton helper class for lazily initialization.
 *
 * @param <T>
 *         泛型
 *
 */
public abstract class SingletonVast<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonVast.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
