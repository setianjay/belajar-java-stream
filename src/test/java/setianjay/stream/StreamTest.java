package setianjay.stream;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
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

}