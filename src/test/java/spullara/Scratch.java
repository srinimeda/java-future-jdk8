package spullara;

import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Scratch {

    static String address(InetSocketAddress sa) {
        return elvis(elvis(sa, InetSocketAddress::getAddress), InetAddress::getHostAddress);
    }

    public static <T, V> V elvis(T t, Function<T, V> mapper) {
        return t == null ? null : mapper.apply(t);
    }

    public static void test() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", 1);


        List<String> ss = new ArrayList<>();
        ss.stream().flatMap(x -> {
            Stream.Builder<String> builder = Stream.builder();
            for (String s : x.split(",")) {
                builder.add(s);
            }
            return builder.build();
        });
    }


    @Test
    public void testConvert() {
        Set<Property> properties = new HashSet<>();
        properties.add(new Property("a", "1"));
        properties.add(new Property("b", "2"));
        HashMap<String, Property> map = properties.stream().collect(HashMap::new, (m, p) -> m.put(p.name, p), Map::putAll);
        Map<String, List<Property>> collect = properties.stream().collect(groupingBy(p -> p.name));
    }
}

class TestJ8 {

    interface Func<A, B> {
        B f(A a);
    }

    class List<A> {

        <B> List<B> map(Func<A, B> f) {
            return null;
        }

        <B> List<B> bind(Func<A, List<B>> f) {
            return null;
        }

        <B> List<B> apply(final List<Func<A, B>> lf) {
            return lf.bind(this::map);
        }

        <B, C> List<C> bind(final List<B> lb, final Func<A, Func<B, C>> f) {
            List<Func<B, C>> map = map(f);
            return lb.apply(map); // fails to compile
        }

    }

}

class Property {
    public String name;
    public Object value;

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Property another = (Property) obj;
        return name.equals(another.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

interface Action<T, E extends Throwable> {
    T run() throws E;
}

interface ExceptionalFunction<T, R, E extends Throwable> {
    R apply(T t) throws E;
}

@FunctionalInterface
interface Action1Bug<E1> {
    void act(E1 e1) throws Exception;

    static Action1Bug none = (notUsed) -> {
    };
}