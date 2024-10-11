package earth.terrarium.olympus.client.shader;

public interface UniformType<T> {

    void upload(int id, T value);

}
