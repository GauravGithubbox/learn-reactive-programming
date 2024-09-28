package com.example.reactive.programming;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReactiveMethods {

    // Returns a Mono emitting a single string value.
    private Mono<String> testMono() {
        return Mono.just("Reactive Programming ")
                .log();
    }

    // Returns a Flux emitting a list of strings.
    private Flux<String> testFlux() {
        List<String> reactive = List.of("Reactive Programming ", "Makes", "Java", "PowerFull");
        return Flux.fromIterable(reactive);
    }

    // Transforms each element to uppercase using map.
    private Flux<String> testMap() {
        Flux<String> flux = Flux.just("Java", "kotlin", "Go lang", "python");
        return flux.map(s -> s.toUpperCase(Locale.ROOT));
    }

    // Transforms each element to lowercase using flatMap.
    private Flux<String> testFlatMap() {
        Flux<String> flux = Flux.just("Java", "kotlin", "Go lang", "python");
        return flux.flatMap(s -> Mono.just(s.toLowerCase(Locale.ROOT)));
    }

    // Skips the first two elements of the Flux.
    private Flux<String> testSkip() {
        Flux<String> flux = Flux.just("Java", "kotlin", "Go lang", "python");
        return flux.skip(2);
    }

    // Skips the last two elements of the delayed Flux.
    private Flux<String> testBasicSkip() {
        Flux<String> flux = Flux.just("Java", "Cpp", "Rust", "Dart")
                .delayElements(Duration.ofSeconds(1));
        return flux.skipLast(2);
    }

    // Skips elements until the condition (integer == 30) is met.
    private Flux<Integer> testComplexSkip() {
        Flux<Integer> flux = Flux.range(1, 20);
        return flux.skipUntil(integer -> integer == 30);
    }

    // Concatenates multiple Flux streams in sequence.
    private Flux<Integer> testConcat() {
        Flux<Integer> flux1 = Flux.range(1, 20);
        Flux<Integer> flux2 = Flux.range(101, 20);
        Flux<Integer> flux3 = Flux.range(1001, 20);
        return Flux.concat(flux3, flux2, flux1);
    }

    // Merges two delayed Flux streams into one, interleaving elements.
    private Flux<Integer> testMerge() {
        Flux<Integer> flux1 = Flux.range(1, 20)
                .delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(101, 20)
                .delayElements(Duration.ofMillis(500));

        return Flux.merge(flux1, flux2);
    }

    // Zips two Flux streams into a Tuple2, combining their elements.
    private Flux<Tuple2<Integer, Integer>> testZip() {
        Flux<Integer> flux1 = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(101, 20)
                .delayElements(Duration.ofMillis(500));
        return Flux.zip(flux1, flux2);
    }

    // Zips a Flux and a Mono into a Tuple2.
    private Flux<Tuple2<Integer, Integer>> testComplexZip() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500));
        Mono<Integer> mono = Mono.just(1);
        return Flux.zip(flux, mono);
    }

    // Collects Flux elements into a Mono containing a List.
    private Mono<List<Integer>> testCollect() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        return flux.collectList();
    }

    // Buffers Flux elements into a List based on a time interval.
    private Flux<List<Integer>> testBuffer() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        return flux.buffer(Duration.ofMillis(3_100));
    }

    // Collects Flux elements into a Map, where the key is 2*element and value is the element squared.
    private Mono<Map<Integer, Integer>> testMapCollection() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.collectMap(integer -> integer * 2, integer -> integer * integer);
    }

    // Executes actions based on each signal, printing each element and a completion message.
    private Flux<Integer> testDoFunctions() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnEach(signal -> {
            if (signal.getType() == SignalType.ON_COMPLETE) {
                System.out.println("I am done!");
            } else {
                System.out.println(signal.get());
            }
        });
    }

    // Executes an action upon completion of the Flux.
    private Flux<Integer> testDoFunctions2() {
        Flux<Integer> flux = Flux.range(1, 10);
        return flux.doOnComplete(() -> System.out.println("I am complete"));
    }

    // Executes an action if the Flux is cancelled.
    private Flux<Integer> testDoFunctions3() {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
        return flux.doOnCancel(() -> System.out.println("Cancelled!"));
    }

    // Demonstrates error handling by continuing on an error.
    private Flux<Integer> testErrorHandling() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux.onErrorContinue((throwable, o) -> System.out.println("Don't worry about " + o));
    }

    // Demonstrates error handling by switching to another Flux upon an error.
    private Flux<Integer> testErrorHandling2() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux.onErrorResume(throwable -> Flux.range(100, 5));
    }

    // Demonstrates error handling by mapping the exception to a new exception type.
    private Flux<Integer> testErrorHandling3() {
        Flux<Integer> flux = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 5) {
                        throw new RuntimeException("Unexpected number!");
                    }
                    return integer;
                });
        return flux.onErrorMap(throwable -> new UnsupportedOperationException(throwable.getMessage()));
    }

    public static void main(String[] args) throws InterruptedException {
        ReactiveMethods reactiveMethods = new ReactiveMethods();
        reactiveMethods.testDoFunctions()
                .subscribe(System.out::println);
        Thread.sleep(10_000);
    }
}
