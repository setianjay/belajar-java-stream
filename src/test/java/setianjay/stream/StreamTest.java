package setianjay.stream;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "A Stream")
@TestClassOrder(value = ClassOrderer.OrderAnnotation.class)
class StreamTest {

    @Nested
    @DisplayName(value = "When Created")
    @TestMethodOrder(value = MethodOrderer.DisplayName.class)
    @Order(value = 1)
    class CreateStreamTest {

        @Test
        @DisplayName(value = "empty or single")
        void testCreateEmptyOrSingleStream() {
            Stream<String> emptyStringStream = Stream.empty(); // create empty stream
            Stream<String> singleStringStream = Stream.of("Hari"); // create single stream
            // create single stream can hold a null value, if the content is null, stream will return empty stream
            Stream<String> nullableStringStream = Stream.ofNullable(null);

            // isPresent() return false if no stream contents, otherwise return true
            assertFalse(emptyStringStream.findFirst().isPresent()); // return false
            assertTrue(singleStringStream.findFirst().isPresent()); // return true
            assertFalse(nullableStringStream.findFirst().isPresent()); // return false
        }

        @Test
        @DisplayName(value = "from array")
        void testCreateStreamFromArray() {
            // create stream from array
            Stream<Integer> streamNumbers = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            // convert stream to array in the first way, this way return array object
            Object[] arrayNumbers = streamNumbers.toArray();
            System.out.println(Arrays.toString(arrayNumbers)); // result [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            assertTrue(arrayNumbers.length > 0);

            // create stream from array
            Stream<String> streamNames = Arrays.stream(new String[]{"Harry", "Gotham", "Paulo"});
            // convert stream to array in the second way, this method returns an array according to the stream type
            String[] arrayNames = streamNames.toArray(String[]::new);
            System.out.println(Arrays.toString(arrayNames)); // result ["Harry", "Gotham", "Paulo"]
            assertTrue(arrayNames.length > 0);
        }

        @Test
        @DisplayName(value = "from list")
        void testCreateStreamFromList() {
            List<Integer> listNumbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            // create stream from list
            Stream<Integer> streamNumbers = listNumbers.stream();

            // convert stream to list
            List<Integer> resultListNumbers = streamNumbers.toList();
            assertEquals(10, resultListNumbers.size());
        }

        @Test
        @DisplayName(value = "with builder")
        void testCreateStreamWithBuilder() {
            // create stream with builder
            Stream<Integer> streamNumbers = Stream.<Integer>builder()
                    .add(1)
                    .add(2)
                    .add(3)
                    .add(4)
                    .add(5)
                    .add(6)
                    .add(7)
                    .add(8)
                    .add(9)
                    .add(10)
                    .build();

            long countStreamContent = streamNumbers.mapToInt(Integer::intValue).count();
            assertEquals(10, countStreamContent);
        }
    }

    @Nested
    @DisplayName(value = "When Transformed")
    @Order(value = 2)
    @TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
    class TransformationStreamTest {

        @Test
        @DisplayName("using map")
        @Order(value = 1)
        void testTransformedWithMap() {
            // get Collection Map from a Stream in first way
            Map<Integer, String> mapFromStream = Stream.of(
                            "Setyarto",
                            "Sudaryati",
                            "Gurindo Sekti",
                            "Hari Setiaji"
                    )
                    // map from capitalize name to uppercase name like {SETYARTO, SUDARYATI, ...}
                    .map(String::toUpperCase)
                    // collect stream and convert to Map
                    .collect(Collectors.toMap(
                            String::length, // set data for key
                            upperName -> upperName // set data for value
                    ));

            // result {8: SETYARTO, 9: SUDARYATI, ...}
            System.out.println(mapFromStream);
            assertEquals(4, mapFromStream.size());


            // get Collection Map from a Stream in second way (with content of stream is array)
            Map<Integer, String> mapFromStream2 = Stream.of(
                            "Setyarto",
                            "Sudaryati",
                            "Gurindo Sekti",
                            "Hari Setiaji"
                    )
                    // map from capitalize name to uppercase name like {SETYARTO, SUDARYATI, ...}
                    .map(String::toUpperCase)
                    // map content of stream from String to array object
                    .map((upperName) -> new Object[]{upperName.length(), upperName})
                    // collect stream and convert to Map<Integer, String>
                    .collect(Collectors.toMap(
                            lengthName -> (Integer) lengthName[0],
                            upperName -> (String) upperName[1]
                    ));

            // result {8: SETYARTO, 9: SUDARYATI, ...}
            System.out.println(mapFromStream2);
            assertEquals(4, mapFromStream2.size());
        }

        @Test
        @DisplayName("using flatMap")
        @Order(value = 2)
        void testTransformedWithFlatMap() {
            Map<Integer, String> mapFromStream = Stream.of(
                            "Setyarto",
                            "Sudaryati",
                            "Gurindo Sekti",
                            "Hari Setiaji"
                    )
                    /*
                     * Map content of stream from String to Stream<String>, then the result is Stream<Stream<String>>.
                     * because we use flatMap, the result Stream<Stream<String>> will merge or flatten the stream to be
                     * Stream<String>.
                     *
                     * example for the result:
                     * - In process will return:
                     * Stream(Stream("HARI SETIAJI"), Stream("SUDARYATI"), Stream("GURINDO SEKTI"), Stream("SETYARTO")
                     *
                     * - Because we use flatMap, the Final result like below (merge all Stream into single Stream):
                     * Stream("HARI SETIAJI", "SUDARYATI", "GURINDO SEKTI", "SETYARTO")
                     * */
                    .flatMap((name) -> Stream.of(name.toUpperCase()))
                    .collect(Collectors.toMap(
                            data -> Math.abs(new Random().nextInt()),
                            data -> data)
                    );

            // result {RANDOM NUMBER: SETYARTO, ...}
            System.out.println(mapFromStream);
            assertEquals(4, mapFromStream.size());
        }
    }

    @Nested
    @DisplayName(value = "When Filtered")
    @Order(value = 3)
    @TestMethodOrder(value = MethodOrderer.DisplayName.class)
    class FilteringStreamTest {

        @Test
        @DisplayName(value = "using distinct")
        void testFilterStreamUsingDistinct() {
            long streamSize = Stream.of("Hari", "Hari", "Hari", "Gurindo", "Gurindo", "Setyarto", "Sudaryati")
                    .distinct() // delete or drop data if they duplicate
                    .peek(System.out::println) // result : {"Hari", "Gurindo", "Setyarto", "Sudaryati"}
                    .count();

            assertEquals(4, streamSize);
        }

        @Test
        @DisplayName(value = "using filter")
        void testFilterStreamUsingFilter() {
            long streamSize = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .filter(number -> number % 2 == 0) // only take even data
                    .peek(System.out::println) // result {2, 4, 6, 8, 10}
                    .count();

            assertEquals(5, streamSize);
        }

        @Test
        @DisplayName(value = "using groupBy")
        void testFilterStreamUsingGroupBy(){
            Map<String, List<Integer>> groupOfNumberBasedOnEvenOrOdd = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .collect(Collectors.groupingBy((number) -> (number % 2 == 0) ? "Even" : "Odd"));

            // result is map {"Even": [2, 4, 6, 8, 10], "Odd": [1, 3, 5, 7, 9]}
            System.out.println(groupOfNumberBasedOnEvenOrOdd);
            assertEquals(2, groupOfNumberBasedOnEvenOrOdd.size());
        }

        @Test
        @DisplayName(value = "using partitionBy")
        void testFilterStreamUsingPartitionBy(){
            Map<Boolean, List<Integer>> groupOfNumberBasedOnEvenOrOdd = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .collect(Collectors.partitioningBy((number) -> (number % 2 == 0)));

            /*
            * the result is same with Collectors.groupingBy, the only difference is the key. if we use partitionBy then
            * the key is a boolean data type (true or false). But if we use groupingBy, the key can be of any data type
            * (free).
            * */
            //  map {false: [1, 3, 5, 7, 9], true: [2, 4, 6, 8, 10]}
            System.out.println(groupOfNumberBasedOnEvenOrOdd);
            assertEquals(2, groupOfNumberBasedOnEvenOrOdd.size());
        }
    }

    @Nested
    @DisplayName(value = "When Retrieved Data")
    @Order(value = 4)
    @TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
    class RetrievingStreamTest {


        @Test
        @DisplayName(value = "using limit")
        @Order(value = 1)
        void testRetrieveStreamDataUsingLimit() {
            long streamSize = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .limit(5) // take 5 data from the first data
                    .peek(System.out::println) // result {1, 2, 3, 4, 5}
                    .count();

            assertEquals(5, streamSize);
        }

        @Test
        @DisplayName(value = "using skip")
        @Order(value = 2)
        void testRetrieveStreamDataUsingSkip() {
            long streamSize = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .skip(5) // skip 5 data from the first data
                    .peek(System.out::println) // result {6, 7, 8, 9, 10}
                    .count();

            assertEquals(5, streamSize);
        }

        @Test
        @DisplayName(value = "using findAny")
        @Order(value = 3)
        void testRetrieveStreamDataUsingFindAny() {
            Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .findAny() // take one random value in stream. result {random}
                    .ifPresent(Assertions::assertNotNull);
        }

        @Test
        @DisplayName(value = "using findFirst")
        @Order(value = 4)
        void testRetrieveStreamDataUsingFindFirst() {
            Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .findFirst() // find first value in stream. result {1}
                    .ifPresent(Assertions::assertNotNull);
        }

        @Test
        @DisplayName(value = "using takeWhile")
        @Order(value = 5)
        void testRetrieveStreamDataUsingTakeWhile() {
            long streamSize = Stream.of(1, 2, 1, 3, 2, 4, 5, 6, 7, 8, 9, 10)
                    // retrieve data if met with condition, otherwise stop data collection
                    .takeWhile(data -> data < 3)
                    .peek(System.out::println) // result {1, 2, 1}
                    .count();

            assertEquals(3, streamSize);
        }

        @Test
        @DisplayName(value = "using dropWhile")
        @Order(value = 6)
        void testRetrieveStreamDataUsingDropWhile() {
            long streamSize = Stream.of(1, 2, 1, 3, 2, 4, 5, 6, 7, 8, 2, 9, 10)
                    // drop data if met with condition, otherwise stop data collection and retrieve the remaining data
                    .dropWhile(data -> data < 4)
                    .peek(System.out::println) // {1, 2, 1, 3, 2} will be drop, final result is {4, 5, 6, 7, 8, 2, 9, 10}
                    .count();

            assertEquals(8, streamSize);
        }
    }

    @Nested
    @DisplayName(value = "When Ordered")
    @Order(value = 5)
    @TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
    class OrderedStreamTest {

        @Test
        @DisplayName(value = "with sorted with comparator")
        @Order(value = 1)
        void testOrderedDataWithSortedComparator() {
            // string size comparator
            Comparator<String> stringSizeComparator = (obj1, obj2) -> {
                // comparing by length
                System.out.println("Object 1: " + obj1);
                System.out.println("Object 2: " + obj2);
                int lengthComparison = Integer.compare(obj1.length(), obj2.length());

                // if the lengthComparison are equal (return 0 or zero), sort them alphabetically
                if (lengthComparison == 0) {
                    return obj1.compareTo(obj2);
                }

                return lengthComparison;
            };

            List<String> sortedListFromStream = Stream.of(
                            "Hari",
                            "Budi",
                            "Edi",
                            "Gurindo",
                            "Firman",
                            "Setyarto",
                            "Sudaryati",
                            "El",
                            "Al",
                            "Zidan"
                    )
                    .sorted(stringSizeComparator)
                    // result {Al, El, Edi, Budi, Hari, Zidan, Firman, Gurindo, Setyarto, Sudaryati}
                    .peek(System.out::println)
                    .toList();

            assertEquals(10, sortedListFromStream.size());
        }

        @Test
        @DisplayName(value = "with sorted without comparator")
        @Order(value = 2)
        void testOrderedDataWithSortedWithoutComparator() {
            List<String> sortedListFromStream = Stream.of(
                            "Hari",
                            "Budi",
                            "Edi",
                            "Gurindo",
                            "Firman",
                            "Setyarto",
                            "Sudaryati",
                            "El",
                            "Al",
                            "Zidan"
                    )
                    .sorted()
                    // result {Al, Budi, Edi, El, Firman, Gurindo, Hari, Setyarto, Sudaryati, Zidan}
                    .peek(System.out::println)
                    .toList();

            assertEquals(10, sortedListFromStream.size());
        }
    }

    @Nested
    @DisplayName(value = "When Aggregate")
    @Order(value = 6)
    @TestMethodOrder(value = MethodOrderer.DisplayName.class)
    class AggregateStreamTest {

        @Test
        @DisplayName(value = "with min")
        void testMin() {
            int min = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .min(Comparator.naturalOrder()) // result 1, because 10 is max content in a stream
                    .orElse(100);

            assertEquals(1, min);
            assertNotEquals(100, min);
        }

        @Test
        @DisplayName(value = "with max")
        void testMax() {
            int max = Stream.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                    .mapToInt(Integer::parseInt)
                    .max() // result 10, because 10 is max content in a stream
                    .orElse(0);

            assertEquals(10, max);
            assertNotEquals(0, max);
        }

        @Test
        @DisplayName(value = "with reduce")
        void testSum() {
            int result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .reduce(0, Integer::sum); // sum all value in stream

            assertEquals(55, result);
        }

        @Test
        @DisplayName(value = "with average")
        void testAverage() {
            double result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .mapToInt(Integer::intValue)
                    .average() // result is sum content / n = 55 / 10 => 5.5
                    .orElse(0.0);

            assertEquals(5.5, result);
        }

        @Test
        @DisplayName(value = "factorial with reduce")
        void testFactorial() {
            int result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    /*
                     * value (first process value is identity, next is result from operation) * item (content)
                     * 1 x 1        = 1
                     * 1 x 2        = 2
                     * 2 x 3        = 6
                     * 6 x 4        = 24
                     * 24 x 5       = 120
                     * 120 x 6      = 720
                     * 720 x 7      = 5_040
                     * 5040 x 8     = 4_0320
                     * 40320 x 9    = 362_880
                     * 362880 x 10  = 3_628_800
                     * */
                    .reduce(1, (value, item) -> value * item);

            assertEquals(3628800, result);
        }
    }
}