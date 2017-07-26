package java8tutorial.lambda.expression;

@FunctionalInterface
public interface Converter<F, T> {
	T convert(F from);
}
