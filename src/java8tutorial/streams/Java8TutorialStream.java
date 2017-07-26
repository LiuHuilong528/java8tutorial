package java8tutorial.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Java8TutorialStream {

	public static void main(String[] args) {
		List<String> stringCollection = new ArrayList<>();
		stringCollection.add("ddd2");
		stringCollection.add("aaa2");
		stringCollection.add("bbb1");
		stringCollection.add("aaa1");
		stringCollection.add("bbb3");
		stringCollection.add("ccc");
		stringCollection.add("bbb2");
		stringCollection.add("ddd1");

		// Stream - Filter 接收一个 Predicate 对象过滤；返回的还是Stream对象还能再调用Stream的方法
		stringCollection.stream()
						.filter((s) -> s.startsWith("a"))
						.forEach(System.out::println);
		System.out.println(stringCollection.size());
		
		// Sorted - 按自然排序除非传递自定义的 Comparator;不该变原集合的结构
		stringCollection.stream()
						.sorted((a,b)->b.compareTo(a))
						//.filter((s)->s.startsWith("a"))
						.forEach(System.out::println);
		
		// Map  - 将每个元素转换成另个对象；
		stringCollection.stream()
						.map(String::toUpperCase)
						.sorted((a,b)->b.compareTo(a))
						.forEach(System.out::println);
		
		// Matchkh - Predicate 是否匹配 Stream
		boolean anyStartsWithA =
			    stringCollection
			        .stream()
			        .anyMatch((s) -> s.startsWith("a"));	
		System.out.println(anyStartsWithA);
		
		boolean anyStartsWithZ = 
				stringCollection.stream()
								.noneMatch((s)->s.startsWith("z"));
		System.out.println(anyStartsWithZ);
		
		// Count - 返回 stream 的元素总数
		long startsWithB =
			    stringCollection
			        .stream()
			        .filter((s) -> s.startsWith("b"))
			        .count();

		System.out.println(startsWithB);    // 3
		
		// Reduce 对比 JS Reduce
		List<String> nullList = new ArrayList<String>();
		Optional<String> reduced = nullList.stream()
													.sorted()
													.reduce((s1,s2)->s1+"#"+s2);
		reduced.ifPresent(System.out::println);
		
		//Parallel Streams- 并行Stream 多线程操作stream
		int max = 1000000;
		List<String> values = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
		    UUID uuid = UUID.randomUUID();
		    values.add(uuid.toString());
		}
		
		//Sequential Sort
		long t0 = System.nanoTime();

		long count = values.stream().sorted().count();
		System.out.println(count);

		long t1 = System.nanoTime();

		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("sequential sort took: %d ms", millis));
		
		
		//Parallel Sort
		
		long t2 = System.nanoTime();

		long count2 = values.parallelStream().sorted().count();
		System.out.println(count2);

		long t3 = System.nanoTime();

		long millis1 = TimeUnit.NANOSECONDS.toMillis(t3 - t2);
		System.out.println(String.format("parallel sort took: %d ms", millis1));
	}
}
