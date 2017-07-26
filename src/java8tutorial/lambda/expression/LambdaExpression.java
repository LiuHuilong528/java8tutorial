package java8tutorial.lambda.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaExpression {

	public static void main(String[] args) {
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		/** java8以前 **/
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});

		/** java8 ---> 1 **/
		Collections.sort(names, (String a, String b) -> {
			return b.compareTo(a);
		});
		/** java8 --> 2 **/
		Collections.sort(names, (String a, String b) -> b.compareTo(a));
		/** java8 --> 3 **/
		Collections.sort(names, (a, b) -> b.compareTo(a));

		/** java8 @FunctionalInterface 函数方法接口 **/
		Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
		Integer convertered = converter.convert("2345");
		System.out.println(convertered);

		/** java8 static method reference 静态方法和构造函数引用 **/
		Converter<String, Integer> converter1 = Integer::valueOf;
		Integer convertered1 = converter1.convert("2345");
		System.out.println(convertered1);
		// Object methods
		SomeThing someThing = new SomeThing();
		Converter<String, String> converter2 = someThing::startsWith;
		String converted = converter2.convert("Java");
		System.out.println(converted);
		// Constructor --
		PersonFactory<Person> personFactory = Person::new;// 这里java编译器会选择正确的构造器
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person);

		/** lambda 作用域 **/
		// 访问lambda外部局部变量必须是 final - 变量不是final的话，它们会被隐含的转为final，这样效率更高
		final int num = 1;
		Converter<Integer, String> stringConveter = (from) -> String.valueOf(from + num);
		stringConveter.convert(2);
		int n = 1;
		Converter<Integer, String> stringConveterNoFinal = (from) -> String.valueOf(from + n);
		stringConveterNoFinal.convert(4);

		// lambda 不能访问 default 方法；
		/**
		 * java 内建Functional 接口有：
		 * 
		 * @link{Predicates}
		 * @link{Functions}
		 * @link{Suppliers}
		 * @link{Consumers}
		 * @link{Comparators}
		 * @link{Optionals}
		 */
		// Predicate
		Predicate<String> predicate = (s) -> s.length() > 0;
		predicate.test("foo");// true
		predicate.negate().test("foo");// false
		Predicate<Boolean> nonNull = Objects::nonNull;
		Predicate<Boolean> isNull = Objects::isNull;

		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> isNotEmpty = isEmpty.negate();

		// Functions 接受一个参数产生一个结果，Default方法可以用于将多个函数链接在一起
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<String, String> backToString = toInteger.andThen(String::valueOf);

		backToString.apply("123"); // "123"

		// Supplier 产生一个制定类型的结果，不接收参数
		Supplier<Person> personSupplier = Person::new;
		personSupplier.get();

		// Consumers 代表对输入参数的操作
		Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
		greeter.accept(new Person("Luke", "Skywalker"));

		// Comparators - 同java8以前的版本类似，java8对接口增加了 defaultMethod
		Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

		Person p1 = new Person("John", "Doe");
		Person p2 = new Person("Alice", "Wonderland");

		comparator.compare(p1, p2); // > 0
		comparator.reversed().compare(p1, p2); // < 0

		/** Optionals - 不是个 Functional 接口，而是
		  * <code>NullPointerException</code>问题的巧妙解决；
		  * 它是个保存 null 或 non-null 值得容器；java8中返回值可null的都可以返回optional
		  */
		Optional<String> optional = Optional.of("bam");
		
		optional.isPresent();		//true
		optional.get();				//'bam'
		optional.orElse("fallback");//"bam"
		
		optional.ifPresent((s)->System.out.println(s.charAt(0)));//"b"
	}
}

// lambda内部的变量则可写可读
class Lambad64 {
	static int outerStaticNum;
	int outerNum;

	void testScope() {
		Converter<Integer, String> stringConverter1 = (from) -> {
			outerNum = 23;
			return String.valueOf(outerNum);
		};

		Converter<Integer, String> stringConverter2 = (from) -> {
			outerStaticNum = 73;
			return String.valueOf(outerStaticNum);

		};
	}
}

class Person {
	String firstName;
	String lastName;

	Person() {
	}

	Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String toString() {
		return "[" + this.firstName + "·" + this.lastName + "]";
	}
}

interface PersonFactory<P extends Person> {
	P create(String firstName, String lastName);
}

class SomeThing {
	String startsWith(String s) {
		return String.valueOf(s.charAt(0));
	}
}
