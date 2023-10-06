package setianjay.stream;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

}